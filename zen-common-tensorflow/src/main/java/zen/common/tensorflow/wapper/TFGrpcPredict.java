package zen.common.tensorflow.wapper;

import com.google.protobuf.Int64Value;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tensorflow.serving.Model;
import tensorflow.serving.Predict;
import tensorflow.serving.PredictionServiceGrpc;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @Author: xiongjun
 * @Date: 2020/7/10 11:13
 * @description
 * @reviewer
 */
public class TFGrpcPredict {
    private final Logger logger = LoggerFactory.getLogger(TFGrpcPredict.class);
    private final String[] hosts;
    private final int port;
    private final String modelName;
    private final int version;
    private String localIp;
    //网段
    private String networkSeg;
    private Int64Value versionInt64;
    private Model.ModelSpec modelSpec;

    private ExecutorService singleThead;
    private volatile List<PredictionServiceGrpc.PredictionServiceBlockingStub> wakeupBlockingStubList;
    private volatile List<PredictionServiceGrpc.PredictionServiceBlockingStub> wakeupLocalStub;
    private volatile List<PredictionServiceGrpc.PredictionServiceBlockingStub> blockingStubs;
    private volatile List<PredictionServiceGrpc.PredictionServiceBlockingStub> localStub;

    public TFGrpcPredict(String[] hosts, int port, String modelName, int version) throws Exception {
        this.hosts = hosts;
        this.port = port;
        this.modelName = modelName;
        this.version = version;
        init();
    }

    private void init() throws Exception {
        localIp = InetAddress.getLocalHost().getHostAddress();
        logger.info(String.format("localIp:%s", localIp));
        String[] seg = localIp.split("\\.");
        logger.info(String.format("seg:%s", String.join(",", seg)));
        networkSeg = seg[0] + "." + seg[1];
        versionInt64 = Int64Value.newBuilder().setValue(version).build();
        modelSpec = Model.ModelSpec.newBuilder()
                .setName(modelName)
                .setVersion(versionInt64)
                .setSignatureName("classification")
                .build();
        wakeupBlockingStubList = new ArrayList<>();
        wakeupLocalStub = new ArrayList<>();
        blockingStubs = buildRemoteStubs();
        localStub = buildLocalStubs();
        if (blockingStubs.size() + localStub.size() == 0) {
            throw new Exception("all model load failure");
        } else {
            listening();
        }
    }

    public void listening() {
        singleThead = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1));
        singleThead.execute(new WakeupGrpcChannel(hosts, port, modelName, version, localIp,
                wakeupBlockingStubList, wakeupLocalStub, blockingStubs, localStub));
    }

    /**
     * 构建远程通道集合
     *
     * @return
     */
    private List<PredictionServiceGrpc.PredictionServiceBlockingStub> buildRemoteStubs() throws Exception {
        logger.info(String.format("hosts is %s", String.join(",", hosts)));

        Stream<String> remoteHosts = Arrays.stream(hosts).filter(host -> !host.startsWith(networkSeg));
        List<PredictionServiceGrpc.PredictionServiceBlockingStub> stubs = new ArrayList<>((int) remoteHosts.count());
        remoteHosts.forEach(host -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            PredictionServiceGrpc.PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);
            if (MetaUtil.isLoaded(stub, modelName, version)) {
                stubs.add(stub);
            } else {
                wakeupBlockingStubList.add(stub);
                logger.warn(String.format("host:%s,port:%s,modelName:%s,version:%s build remote channel failure", host, port, modelName, version));
            }
        });
        logger.info(String.format("hosts length: %s,blockingStubs length is %s, wakupBlockingStubList length is %s", hosts.length, stubs.size(), wakeupBlockingStubList.size()));
        if (wakeupBlockingStubList.size() == remoteHosts.count()) {
            logger.info("all remote model load failure");
        }
        return stubs;
    }

    /**
     * 构建同机房连接集合
     *
     * @return
     */
    private List<PredictionServiceGrpc.PredictionServiceBlockingStub> buildLocalStubs() {
        logger.info(String.format("local ip: %s, networdSeg: %s", localIp, networkSeg));

        Stream<String> segHosts = Arrays.stream(hosts).filter(host -> host.startsWith(networkSeg));
        int segHostSize = (int) segHosts.count();
        List<PredictionServiceGrpc.PredictionServiceBlockingStub> stubs = new ArrayList<>(segHostSize);
        if (segHostSize == 0) {
            logger.warn("current networkSeg not deploy tensorflow serving");
            return stubs;
        }
        Arrays.stream(hosts).filter(host -> host.startsWith(networkSeg)).forEach(host -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            PredictionServiceGrpc.PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);
            if (MetaUtil.isLoaded(stub, modelName, version)) {
                stubs.add(stub);
            } else {
                wakeupLocalStub.add(stub);
                logger.warn(String.format("host:%s,port:%s,modelName:%s,version:%s build local channel failure", localIp, port, modelName, version));
            }
        });
        return stubs;
    }

    public List<PredictionServiceGrpc.PredictionServiceBlockingStub> getBlockingStubs() {
        return blockingStubs;
    }

    public List<PredictionServiceGrpc.PredictionServiceBlockingStub> getLocalStub() {
        return localStub;
    }

    public Int64Value getVersionInt64() {
        return versionInt64;
    }

    public Model.ModelSpec getModelSpec() {
        return modelSpec;
    }

    /**
     * 预测方法
     *
     * @param request
     * @return
     */
    public Predict.PredictResponse predict(Predict.PredictRequest request) {
        boolean isLocal = localStub.size() > 0;
        PredictionServiceGrpc.PredictionServiceBlockingStub stub;
        Predict.PredictResponse response = null;
        int index;
        if (isLocal) {
            index = new Random().nextInt(localStub.size());
            stub = localStub.get(index);
        } else {
            if (blockingStubs.size() == 0) {
                logger.warn("No channels available");
                return response;
            }
            index = new Random().nextInt(blockingStubs.size());
            stub = blockingStubs.get(index);
        }
        try {
            response = stub.predict(request);
        } catch (StatusRuntimeException e) {
            switch (e.getStatus().getCode()) {
                case INVALID_ARGUMENT://非法参数
                    logger.warn(e.getMessage());
                case UNAVAILABLE:
                    if (isLocal) {
                        wakeupLocalStub.add(localStub.remove(index));
                    } else {
                        wakeupBlockingStubList.add(blockingStubs.remove(index));
                    }
                    break;
                default:
                    logger.warn(e.getMessage());
                    break;
            }
        }
        return response;
    }
}

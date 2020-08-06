package zen.common.tensorflow.wapper;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tensorflow.serving.PredictionServiceGrpc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * @Author: xiongjun
 * @Date: 2020/7/15 10:53
 * @description 故障恢复线程
 * @reviewer
 */
public class WakeupGrpcChannel implements Runnable {
    private Logger logger = LoggerFactory.getLogger(WakeupGrpcChannel.class);

    private String[] hosts;
    private int port;
    private String modelName;
    private int version;
    private List<PredictionServiceGrpc.PredictionServiceBlockingStub> wakeupBlockingStubList;
    private List<PredictionServiceGrpc.PredictionServiceBlockingStub> wakeupLocalStub;
    private List<PredictionServiceGrpc.PredictionServiceBlockingStub> blockingStubs;
    private List<PredictionServiceGrpc.PredictionServiceBlockingStub> localStub;
    private String localIp;
    private String networkSeg;//网段
    public WakeupGrpcChannel(String[] hosts, int port, String modelName, int version, String localIp,
                             List<PredictionServiceGrpc.PredictionServiceBlockingStub> wakeupBlockingStubList,
                             List<PredictionServiceGrpc.PredictionServiceBlockingStub> wakeupLocalStub,
                             List<PredictionServiceGrpc.PredictionServiceBlockingStub> blockingStubs,
                             List<PredictionServiceGrpc.PredictionServiceBlockingStub> localStub) {
        this.hosts = hosts;
        this.port = port;
        this.modelName = modelName;
        this.version = version;
        this.wakeupBlockingStubList = wakeupBlockingStubList;
        this.wakeupLocalStub = wakeupLocalStub;
        this.blockingStubs = blockingStubs;
        this.localStub = localStub;
        this.localIp = localIp;
        String[] seg = localIp.split("\\.");
        this.networkSeg = seg[0]+"."+seg[1];
    }

    /**
     * 重新建立本地连接
     * @return
     */
    private boolean rebulidLocalChannel(){
        wakeupLocalStub.forEach(wlStub->{
            ManagedChannel c = (ManagedChannel)wlStub.getChannel();
            c.shutdownNow();
        });
        logger.info("close old local channel");
        wakeupLocalStub.clear();
        Stream<String> segHosts = Arrays.stream(hosts).filter(host->host.startsWith(networkSeg));
        if (segHosts.count()==0){
            logger.info("current networkSeg not deploy tensorflow serving");
            return false;
        }
        Arrays.stream(hosts).filter(host->host.startsWith(networkSeg)).forEach(host->{
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
            PredictionServiceGrpc.PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);
            if (MetaUtil.isLoaded(stub,modelName, version)) {
                localStub.add(stub);
                logger.info(format("host:%s,port:%s,modelName:%s,version:%s rebuild local channel successful",host,port,modelName, version));
            }else{
                wakeupLocalStub.add(stub);
                logger.warn(format("host:%s,port:%s,modelName:%s,version:%s rebuild local channel failure",host,port,modelName, version));
            }
        });
        if (localStub.size()>0){
            logger.info(format("%s %s local channels rebuild sucessful",modelName,blockingStubs.size()));
            return true;
        }else{
            return false;
        }
    }

    /**
     * 重新建立远程连接
     * @return
     */
    private boolean rebuildRomateStubs() {
        wakeupBlockingStubList.forEach(wbStub->{
            ManagedChannel c = (ManagedChannel)wbStub.getChannel();
            c.shutdownNow();
        });
        logger.info("close old romate channel");
        wakeupBlockingStubList.clear();
        if (hosts.length > 0) {
            Arrays.stream(hosts).filter(host->!host.startsWith(networkSeg)).forEach(host->{
                ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                PredictionServiceGrpc.PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);
                if (MetaUtil.isLoaded(stub,modelName, version)) {
                    blockingStubs.add(stub);
                    logger.info(format("host:%s,port:%s,modelName:%s,version:%s rebuild remote channel successful",host,port,modelName, version));
                }else {
                    wakeupBlockingStubList.add(stub);
                    logger.warn(format("host:%s,port:%s,modelName:%s,version:%s rebuild remote channel failure",host,port,modelName, version));
                }
            });
        }
        if (blockingStubs.size()>0){
            logger.info(format("%s %s remote channels rebuild sucessful",modelName,blockingStubs.size()));
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void run() {
        int show =0;
        int reconnectCount = 0;
        while (true){
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            show+=1;
            for(int i=0;i<wakeupLocalStub.size(); i++){
                if(MetaUtil.isLoaded(wakeupLocalStub.get(i),modelName, version)){
                    reconnectCount = 0;
                    logger.info(format("%s find a local channel reconnection successful",modelName));
                    localStub.add(wakeupLocalStub.remove(i));
                }
            }
            for(int i=0;i<wakeupBlockingStubList.size(); i++){
                if (MetaUtil.isLoaded(wakeupBlockingStubList.get(i),modelName, version)){
                    reconnectCount = 0;
                    logger.info(format("%s found a remote channel reconnection successful",modelName));
                    blockingStubs.add(wakeupBlockingStubList.remove(i));
                }
            }
            for(int i=0;i<localStub.size(); i++){
                if(!MetaUtil.isLoaded(localStub.get(0),modelName, version)){
                    reconnectCount = 0;
                    logger.info(format("%s found a local channel disconnected",modelName));
                    wakeupLocalStub.add(localStub.remove(i));
                }
            }
            for(int i=0;i<blockingStubs.size(); i++){
                if (!MetaUtil.isLoaded(blockingStubs.get(i),modelName, version)){
                    reconnectCount = 0;
                    logger.info(format("%s found a remote channel disconnected",modelName));
                    wakeupBlockingStubList.add(blockingStubs.remove(i));
                }
            }
            if (show%5==0){
                logger.info(format("%s localStub length is %s",modelName,localStub.size()));
                logger.info(format("%s blockingStubs length is %s",modelName,blockingStubs.size()));
                show =0;
            }
            if(localStub.size()==0 && blockingStubs.size()==0){
                logger.info(format("%s all channels are disconnected",modelName));
                reconnectCount+=1;
                if (reconnectCount>=3){
                    logger.info(format("%s start rebuild channels",modelName));
                    if (rebulidLocalChannel() || rebuildRomateStubs()){
                        reconnectCount=0;
                    }else{
                        logger.info(format("%s rebuild channels failure",modelName));
                    }
                }
            }
        }
    }

}

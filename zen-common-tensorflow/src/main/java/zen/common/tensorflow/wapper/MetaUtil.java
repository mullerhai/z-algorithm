package zen.common.tensorflow.wapper;

import com.google.protobuf.Int64Value;
import io.grpc.StatusRuntimeException;
import tensorflow.serving.GetModelMetadata;
import tensorflow.serving.Model;
import tensorflow.serving.PredictionServiceGrpc;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Author: xiongjun
 * @Date: 2020/6/28 10:33
 * @description 模型元数据工具类
 * @reviewer
 */
public class MetaUtil {
    public static boolean isLoaded(PredictionServiceGrpc.PredictionServiceBlockingStub stub,
                                   String modelName,
                                   int version) {
        Model.ModelSpec modelSpec = Model.ModelSpec.newBuilder().setName(modelName)
                .setVersion(Int64Value.newBuilder().setValue(version).build()).build();
        GetModelMetadata.GetModelMetadataRequest modelMetadataRequest = GetModelMetadata.GetModelMetadataRequest.newBuilder()
                .setModelSpec(modelSpec)
                .addAllMetadataField(Collections.singletonList("signature_def"))
                .build();
        try {
            stub.getModelMetadata(modelMetadataRequest);
            return true;
        } catch (StatusRuntimeException e) {
            return false;
        }
    }

    /*public static boolean replaceVersion(String host,int port,
                                         String modelPath,String modelName,int newVersion){
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        PredictionServiceGrpc.PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);
        if (!isLoaded(stub,modelName,newVersion)){
            return false;
        }
        FileSystemStoragePathSource.FileSystemStoragePathSourceConfig.ServableVersionPolicy versionPolicy = FileSystemStoragePathSource
                .FileSystemStoragePathSourceConfig.ServableVersionPolicy
                .newBuilder().setSpecific(FileSystemStoragePathSource.FileSystemStoragePathSourceConfig.
                        ServableVersionPolicy.Specific
                        .newBuilder().addVersions(newVersion).build()).build();
        ModelServerConfigOuterClass.ModelConfig modelConfig = ModelServerConfigOuterClass.ModelConfig.newBuilder()
                .setBasePath(modelPath)
                .setName(modelName)
                .setModelVersionPolicy(versionPolicy)
                .setModelPlatform("tensorflow")
                .build();
        ModelServerConfigOuterClass.ModelConfigList modelConfigList = ModelServerConfigOuterClass.ModelConfigList.newBuilder()
                .addConfig(modelConfig).build();
        // 添加ModelConfigList到ModelServerConfig对象当中
        ModelServerConfigOuterClass.ModelServerConfig modelServerConfig = ModelServerConfigOuterClass.ModelServerConfig.newBuilder()
                .setModelConfigList(modelConfigList).build();
        // 构建ReloadConfigRequest并绑定ModelServerConfig对象。
        ModelManagement.ReloadConfigRequest reloadConfigRequest = ModelManagement.ReloadConfigRequest.newBuilder()
                .setConfig(modelServerConfig).build();

        // 构建modelServiceBlockingStub访问句柄

        ModelServiceGrpc.ModelServiceBlockingStub modelServiceBlockingStub = ModelServiceGrpc.newBlockingStub(channel);

        ModelManagement.ReloadConfigResponse reloadConfigResponse = modelServiceBlockingStub.handleReloadConfigRequest(reloadConfigRequest);

        return reloadConfigResponse.getStatus().getErrorCode() == null;
    }*/
}

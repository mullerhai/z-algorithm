// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/example/example_parser_configuration.proto

package org.tensorflow.example;

public interface FeatureConfigurationOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tensorflow.FeatureConfiguration)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.tensorflow.FixedLenFeatureProto fixed_len_feature = 1;</code>
   */
  boolean hasFixedLenFeature();
  /**
   * <code>.tensorflow.FixedLenFeatureProto fixed_len_feature = 1;</code>
   */
  FixedLenFeatureProto getFixedLenFeature();
  /**
   * <code>.tensorflow.FixedLenFeatureProto fixed_len_feature = 1;</code>
   */
  FixedLenFeatureProtoOrBuilder getFixedLenFeatureOrBuilder();

  /**
   * <code>.tensorflow.VarLenFeatureProto var_len_feature = 2;</code>
   */
  boolean hasVarLenFeature();
  /**
   * <code>.tensorflow.VarLenFeatureProto var_len_feature = 2;</code>
   */
  VarLenFeatureProto getVarLenFeature();
  /**
   * <code>.tensorflow.VarLenFeatureProto var_len_feature = 2;</code>
   */
  VarLenFeatureProtoOrBuilder getVarLenFeatureOrBuilder();

  public FeatureConfiguration.ConfigCase getConfigCase();
}

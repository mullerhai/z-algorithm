// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/example/example_parser_configuration.proto

package org.tensorflow.example;

public interface FixedLenFeatureProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tensorflow.FixedLenFeatureProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.tensorflow.DataType dtype = 1;</code>
   */
  int getDtypeValue();
  /**
   * <code>.tensorflow.DataType dtype = 1;</code>
   */
  org.tensorflow.framework.DataType getDtype();

  /**
   * <code>.tensorflow.TensorShapeProto shape = 2;</code>
   */
  boolean hasShape();
  /**
   * <code>.tensorflow.TensorShapeProto shape = 2;</code>
   */
  org.tensorflow.framework.TensorShapeProto getShape();
  /**
   * <code>.tensorflow.TensorShapeProto shape = 2;</code>
   */
  org.tensorflow.framework.TensorShapeProtoOrBuilder getShapeOrBuilder();

  /**
   * <code>.tensorflow.TensorProto default_value = 3;</code>
   */
  boolean hasDefaultValue();
  /**
   * <code>.tensorflow.TensorProto default_value = 3;</code>
   */
  org.tensorflow.framework.TensorProto getDefaultValue();
  /**
   * <code>.tensorflow.TensorProto default_value = 3;</code>
   */
  org.tensorflow.framework.TensorProtoOrBuilder getDefaultValueOrBuilder();

  /**
   * <code>string values_output_tensor_name = 4;</code>
   */
  String getValuesOutputTensorName();
  /**
   * <code>string values_output_tensor_name = 4;</code>
   */
  com.google.protobuf.ByteString
      getValuesOutputTensorNameBytes();
}

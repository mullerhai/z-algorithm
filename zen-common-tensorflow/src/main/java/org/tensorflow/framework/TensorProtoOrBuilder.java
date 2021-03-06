// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/tensor.proto

package org.tensorflow.framework;

public interface TensorProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tensorflow.TensorProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.tensorflow.DataType dtype = 1;</code>
   */
  int getDtypeValue();
  /**
   * <code>.tensorflow.DataType dtype = 1;</code>
   */
  DataType getDtype();

  /**
   * <pre>
   * Shape of the tensor.  TODO(touts): sort out the 0-rank issues.
   * </pre>
   *
   * <code>.tensorflow.TensorShapeProto tensor_shape = 2;</code>
   */
  boolean hasTensorShape();
  /**
   * <pre>
   * Shape of the tensor.  TODO(touts): sort out the 0-rank issues.
   * </pre>
   *
   * <code>.tensorflow.TensorShapeProto tensor_shape = 2;</code>
   */
  TensorShapeProto getTensorShape();
  /**
   * <pre>
   * Shape of the tensor.  TODO(touts): sort out the 0-rank issues.
   * </pre>
   *
   * <code>.tensorflow.TensorShapeProto tensor_shape = 2;</code>
   */
  TensorShapeProtoOrBuilder getTensorShapeOrBuilder();

  /**
   * <pre>
   * Version number.
   * In version 0, if the "repeated xxx" representations contain only one
   * element, that element is repeated to fill the shape.  This makes it easy
   * to represent a constant Tensor with a single value.
   * </pre>
   *
   * <code>int32 version_number = 3;</code>
   */
  int getVersionNumber();

  /**
   * <pre>
   * Serialized raw tensor content from either Tensor::AsProtoTensorContent or
   * memcpy in tensorflow::grpc::EncodeTensorToByteBuffer. This representation
   * can be used for all tensor types. The purpose of this representation is to
   * reduce serialization overhead during RPC call by avoiding serialization of
   * many repeated small items.
   * </pre>
   *
   * <code>bytes tensor_content = 4;</code>
   */
  com.google.protobuf.ByteString getTensorContent();

  /**
   * <pre>
   * DT_HALF, DT_BFLOAT16. Note that since protobuf has no int16 type, we'll
   * have some pointless zero padding for each value here.
   * </pre>
   *
   * <code>repeated int32 half_val = 13 [packed = true];</code>
   */
  java.util.List<Integer> getHalfValList();
  /**
   * <pre>
   * DT_HALF, DT_BFLOAT16. Note that since protobuf has no int16 type, we'll
   * have some pointless zero padding for each value here.
   * </pre>
   *
   * <code>repeated int32 half_val = 13 [packed = true];</code>
   */
  int getHalfValCount();
  /**
   * <pre>
   * DT_HALF, DT_BFLOAT16. Note that since protobuf has no int16 type, we'll
   * have some pointless zero padding for each value here.
   * </pre>
   *
   * <code>repeated int32 half_val = 13 [packed = true];</code>
   */
  int getHalfVal(int index);

  /**
   * <pre>
   * DT_FLOAT.
   * </pre>
   *
   * <code>repeated float float_val = 5 [packed = true];</code>
   */
  java.util.List<Float> getFloatValList();
  /**
   * <pre>
   * DT_FLOAT.
   * </pre>
   *
   * <code>repeated float float_val = 5 [packed = true];</code>
   */
  int getFloatValCount();
  /**
   * <pre>
   * DT_FLOAT.
   * </pre>
   *
   * <code>repeated float float_val = 5 [packed = true];</code>
   */
  float getFloatVal(int index);

  /**
   * <pre>
   * DT_DOUBLE.
   * </pre>
   *
   * <code>repeated double double_val = 6 [packed = true];</code>
   */
  java.util.List<Double> getDoubleValList();
  /**
   * <pre>
   * DT_DOUBLE.
   * </pre>
   *
   * <code>repeated double double_val = 6 [packed = true];</code>
   */
  int getDoubleValCount();
  /**
   * <pre>
   * DT_DOUBLE.
   * </pre>
   *
   * <code>repeated double double_val = 6 [packed = true];</code>
   */
  double getDoubleVal(int index);

  /**
   * <pre>
   * DT_INT32, DT_INT16, DT_INT8, DT_UINT8.
   * </pre>
   *
   * <code>repeated int32 int_val = 7 [packed = true];</code>
   */
  java.util.List<Integer> getIntValList();
  /**
   * <pre>
   * DT_INT32, DT_INT16, DT_INT8, DT_UINT8.
   * </pre>
   *
   * <code>repeated int32 int_val = 7 [packed = true];</code>
   */
  int getIntValCount();
  /**
   * <pre>
   * DT_INT32, DT_INT16, DT_INT8, DT_UINT8.
   * </pre>
   *
   * <code>repeated int32 int_val = 7 [packed = true];</code>
   */
  int getIntVal(int index);

  /**
   * <pre>
   * DT_STRING
   * </pre>
   *
   * <code>repeated bytes string_val = 8;</code>
   */
  java.util.List<com.google.protobuf.ByteString> getStringValList();
  /**
   * <pre>
   * DT_STRING
   * </pre>
   *
   * <code>repeated bytes string_val = 8;</code>
   */
  int getStringValCount();
  /**
   * <pre>
   * DT_STRING
   * </pre>
   *
   * <code>repeated bytes string_val = 8;</code>
   */
  com.google.protobuf.ByteString getStringVal(int index);

  /**
   * <pre>
   * DT_COMPLEX64. scomplex_val(2*i) and scomplex_val(2*i+1) are real
   * and imaginary parts of i-th single precision complex.
   * </pre>
   *
   * <code>repeated float scomplex_val = 9 [packed = true];</code>
   */
  java.util.List<Float> getScomplexValList();
  /**
   * <pre>
   * DT_COMPLEX64. scomplex_val(2*i) and scomplex_val(2*i+1) are real
   * and imaginary parts of i-th single precision complex.
   * </pre>
   *
   * <code>repeated float scomplex_val = 9 [packed = true];</code>
   */
  int getScomplexValCount();
  /**
   * <pre>
   * DT_COMPLEX64. scomplex_val(2*i) and scomplex_val(2*i+1) are real
   * and imaginary parts of i-th single precision complex.
   * </pre>
   *
   * <code>repeated float scomplex_val = 9 [packed = true];</code>
   */
  float getScomplexVal(int index);

  /**
   * <pre>
   * DT_INT64
   * </pre>
   *
   * <code>repeated int64 int64_val = 10 [packed = true];</code>
   */
  java.util.List<Long> getInt64ValList();
  /**
   * <pre>
   * DT_INT64
   * </pre>
   *
   * <code>repeated int64 int64_val = 10 [packed = true];</code>
   */
  int getInt64ValCount();
  /**
   * <pre>
   * DT_INT64
   * </pre>
   *
   * <code>repeated int64 int64_val = 10 [packed = true];</code>
   */
  long getInt64Val(int index);

  /**
   * <pre>
   * DT_BOOL
   * </pre>
   *
   * <code>repeated bool bool_val = 11 [packed = true];</code>
   */
  java.util.List<Boolean> getBoolValList();
  /**
   * <pre>
   * DT_BOOL
   * </pre>
   *
   * <code>repeated bool bool_val = 11 [packed = true];</code>
   */
  int getBoolValCount();
  /**
   * <pre>
   * DT_BOOL
   * </pre>
   *
   * <code>repeated bool bool_val = 11 [packed = true];</code>
   */
  boolean getBoolVal(int index);

  /**
   * <pre>
   * DT_COMPLEX128. dcomplex_val(2*i) and dcomplex_val(2*i+1) are real
   * and imaginary parts of i-th double precision complex.
   * </pre>
   *
   * <code>repeated double dcomplex_val = 12 [packed = true];</code>
   */
  java.util.List<Double> getDcomplexValList();
  /**
   * <pre>
   * DT_COMPLEX128. dcomplex_val(2*i) and dcomplex_val(2*i+1) are real
   * and imaginary parts of i-th double precision complex.
   * </pre>
   *
   * <code>repeated double dcomplex_val = 12 [packed = true];</code>
   */
  int getDcomplexValCount();
  /**
   * <pre>
   * DT_COMPLEX128. dcomplex_val(2*i) and dcomplex_val(2*i+1) are real
   * and imaginary parts of i-th double precision complex.
   * </pre>
   *
   * <code>repeated double dcomplex_val = 12 [packed = true];</code>
   */
  double getDcomplexVal(int index);

  /**
   * <pre>
   * DT_RESOURCE
   * </pre>
   *
   * <code>repeated .tensorflow.ResourceHandleProto resource_handle_val = 14;</code>
   */
  java.util.List<ResourceHandleProto>
      getResourceHandleValList();
  /**
   * <pre>
   * DT_RESOURCE
   * </pre>
   *
   * <code>repeated .tensorflow.ResourceHandleProto resource_handle_val = 14;</code>
   */
  ResourceHandleProto getResourceHandleVal(int index);
  /**
   * <pre>
   * DT_RESOURCE
   * </pre>
   *
   * <code>repeated .tensorflow.ResourceHandleProto resource_handle_val = 14;</code>
   */
  int getResourceHandleValCount();
  /**
   * <pre>
   * DT_RESOURCE
   * </pre>
   *
   * <code>repeated .tensorflow.ResourceHandleProto resource_handle_val = 14;</code>
   */
  java.util.List<? extends ResourceHandleProtoOrBuilder>
      getResourceHandleValOrBuilderList();
  /**
   * <pre>
   * DT_RESOURCE
   * </pre>
   *
   * <code>repeated .tensorflow.ResourceHandleProto resource_handle_val = 14;</code>
   */
  ResourceHandleProtoOrBuilder getResourceHandleValOrBuilder(
          int index);

  /**
   * <pre>
   * DT_VARIANT
   * </pre>
   *
   * <code>repeated .tensorflow.VariantTensorDataProto variant_val = 15;</code>
   */
  java.util.List<VariantTensorDataProto>
      getVariantValList();
  /**
   * <pre>
   * DT_VARIANT
   * </pre>
   *
   * <code>repeated .tensorflow.VariantTensorDataProto variant_val = 15;</code>
   */
  VariantTensorDataProto getVariantVal(int index);
  /**
   * <pre>
   * DT_VARIANT
   * </pre>
   *
   * <code>repeated .tensorflow.VariantTensorDataProto variant_val = 15;</code>
   */
  int getVariantValCount();
  /**
   * <pre>
   * DT_VARIANT
   * </pre>
   *
   * <code>repeated .tensorflow.VariantTensorDataProto variant_val = 15;</code>
   */
  java.util.List<? extends VariantTensorDataProtoOrBuilder>
      getVariantValOrBuilderList();
  /**
   * <pre>
   * DT_VARIANT
   * </pre>
   *
   * <code>repeated .tensorflow.VariantTensorDataProto variant_val = 15;</code>
   */
  VariantTensorDataProtoOrBuilder getVariantValOrBuilder(
          int index);

  /**
   * <pre>
   * DT_UINT32
   * </pre>
   *
   * <code>repeated uint32 uint32_val = 16 [packed = true];</code>
   */
  java.util.List<Integer> getUint32ValList();
  /**
   * <pre>
   * DT_UINT32
   * </pre>
   *
   * <code>repeated uint32 uint32_val = 16 [packed = true];</code>
   */
  int getUint32ValCount();
  /**
   * <pre>
   * DT_UINT32
   * </pre>
   *
   * <code>repeated uint32 uint32_val = 16 [packed = true];</code>
   */
  int getUint32Val(int index);

  /**
   * <pre>
   * DT_UINT64
   * </pre>
   *
   * <code>repeated uint64 uint64_val = 17 [packed = true];</code>
   */
  java.util.List<Long> getUint64ValList();
  /**
   * <pre>
   * DT_UINT64
   * </pre>
   *
   * <code>repeated uint64 uint64_val = 17 [packed = true];</code>
   */
  int getUint64ValCount();
  /**
   * <pre>
   * DT_UINT64
   * </pre>
   *
   * <code>repeated uint64 uint64_val = 17 [packed = true];</code>
   */
  long getUint64Val(int index);
}

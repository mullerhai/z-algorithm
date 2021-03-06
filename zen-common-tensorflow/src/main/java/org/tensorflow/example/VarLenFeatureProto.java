// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/example/example_parser_configuration.proto

package org.tensorflow.example;

/**
 * Protobuf type {@code tensorflow.VarLenFeatureProto}
 */
public  final class VarLenFeatureProto extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:tensorflow.VarLenFeatureProto)
    VarLenFeatureProtoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use VarLenFeatureProto.newBuilder() to construct.
  private VarLenFeatureProto(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private VarLenFeatureProto() {
    dtype_ = 0;
    valuesOutputTensorName_ = "";
    indicesOutputTensorName_ = "";
    shapesOutputTensorName_ = "";
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private VarLenFeatureProto(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {
            int rawValue = input.readEnum();

            dtype_ = rawValue;
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();

            valuesOutputTensorName_ = s;
            break;
          }
          case 26: {
            String s = input.readStringRequireUtf8();

            indicesOutputTensorName_ = s;
            break;
          }
          case 34: {
            String s = input.readStringRequireUtf8();

            shapesOutputTensorName_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return ExampleParserConfigurationProtos.internal_static_tensorflow_VarLenFeatureProto_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return ExampleParserConfigurationProtos.internal_static_tensorflow_VarLenFeatureProto_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            VarLenFeatureProto.class, Builder.class);
  }

  public static final int DTYPE_FIELD_NUMBER = 1;
  private int dtype_;
  /**
   * <code>.tensorflow.DataType dtype = 1;</code>
   */
  public int getDtypeValue() {
    return dtype_;
  }
  /**
   * <code>.tensorflow.DataType dtype = 1;</code>
   */
  public org.tensorflow.framework.DataType getDtype() {
    @SuppressWarnings("deprecation")
    org.tensorflow.framework.DataType result = org.tensorflow.framework.DataType.valueOf(dtype_);
    return result == null ? org.tensorflow.framework.DataType.UNRECOGNIZED : result;
  }

  public static final int VALUES_OUTPUT_TENSOR_NAME_FIELD_NUMBER = 2;
  private volatile Object valuesOutputTensorName_;
  /**
   * <code>string values_output_tensor_name = 2;</code>
   */
  public String getValuesOutputTensorName() {
    Object ref = valuesOutputTensorName_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      valuesOutputTensorName_ = s;
      return s;
    }
  }
  /**
   * <code>string values_output_tensor_name = 2;</code>
   */
  public com.google.protobuf.ByteString
      getValuesOutputTensorNameBytes() {
    Object ref = valuesOutputTensorName_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      valuesOutputTensorName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int INDICES_OUTPUT_TENSOR_NAME_FIELD_NUMBER = 3;
  private volatile Object indicesOutputTensorName_;
  /**
   * <code>string indices_output_tensor_name = 3;</code>
   */
  public String getIndicesOutputTensorName() {
    Object ref = indicesOutputTensorName_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      indicesOutputTensorName_ = s;
      return s;
    }
  }
  /**
   * <code>string indices_output_tensor_name = 3;</code>
   */
  public com.google.protobuf.ByteString
      getIndicesOutputTensorNameBytes() {
    Object ref = indicesOutputTensorName_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      indicesOutputTensorName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SHAPES_OUTPUT_TENSOR_NAME_FIELD_NUMBER = 4;
  private volatile Object shapesOutputTensorName_;
  /**
   * <code>string shapes_output_tensor_name = 4;</code>
   */
  public String getShapesOutputTensorName() {
    Object ref = shapesOutputTensorName_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      shapesOutputTensorName_ = s;
      return s;
    }
  }
  /**
   * <code>string shapes_output_tensor_name = 4;</code>
   */
  public com.google.protobuf.ByteString
      getShapesOutputTensorNameBytes() {
    Object ref = shapesOutputTensorName_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      shapesOutputTensorName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (dtype_ != org.tensorflow.framework.DataType.DT_INVALID.getNumber()) {
      output.writeEnum(1, dtype_);
    }
    if (!getValuesOutputTensorNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, valuesOutputTensorName_);
    }
    if (!getIndicesOutputTensorNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, indicesOutputTensorName_);
    }
    if (!getShapesOutputTensorNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, shapesOutputTensorName_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (dtype_ != org.tensorflow.framework.DataType.DT_INVALID.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(1, dtype_);
    }
    if (!getValuesOutputTensorNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, valuesOutputTensorName_);
    }
    if (!getIndicesOutputTensorNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, indicesOutputTensorName_);
    }
    if (!getShapesOutputTensorNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, shapesOutputTensorName_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof VarLenFeatureProto)) {
      return super.equals(obj);
    }
    VarLenFeatureProto other = (VarLenFeatureProto) obj;

    if (dtype_ != other.dtype_) return false;
    if (!getValuesOutputTensorName()
        .equals(other.getValuesOutputTensorName())) return false;
    if (!getIndicesOutputTensorName()
        .equals(other.getIndicesOutputTensorName())) return false;
    if (!getShapesOutputTensorName()
        .equals(other.getShapesOutputTensorName())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + DTYPE_FIELD_NUMBER;
    hash = (53 * hash) + dtype_;
    hash = (37 * hash) + VALUES_OUTPUT_TENSOR_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getValuesOutputTensorName().hashCode();
    hash = (37 * hash) + INDICES_OUTPUT_TENSOR_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getIndicesOutputTensorName().hashCode();
    hash = (37 * hash) + SHAPES_OUTPUT_TENSOR_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getShapesOutputTensorName().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static VarLenFeatureProto parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static VarLenFeatureProto parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static VarLenFeatureProto parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static VarLenFeatureProto parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static VarLenFeatureProto parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static VarLenFeatureProto parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static VarLenFeatureProto parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static VarLenFeatureProto parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static VarLenFeatureProto parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static VarLenFeatureProto parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static VarLenFeatureProto parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static VarLenFeatureProto parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(VarLenFeatureProto prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code tensorflow.VarLenFeatureProto}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:tensorflow.VarLenFeatureProto)
      VarLenFeatureProtoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ExampleParserConfigurationProtos.internal_static_tensorflow_VarLenFeatureProto_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ExampleParserConfigurationProtos.internal_static_tensorflow_VarLenFeatureProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              VarLenFeatureProto.class, Builder.class);
    }

    // Construct using org.tensorflow.example.VarLenFeatureProto.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      dtype_ = 0;

      valuesOutputTensorName_ = "";

      indicesOutputTensorName_ = "";

      shapesOutputTensorName_ = "";

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return ExampleParserConfigurationProtos.internal_static_tensorflow_VarLenFeatureProto_descriptor;
    }

    @Override
    public VarLenFeatureProto getDefaultInstanceForType() {
      return VarLenFeatureProto.getDefaultInstance();
    }

    @Override
    public VarLenFeatureProto build() {
      VarLenFeatureProto result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public VarLenFeatureProto buildPartial() {
      VarLenFeatureProto result = new VarLenFeatureProto(this);
      result.dtype_ = dtype_;
      result.valuesOutputTensorName_ = valuesOutputTensorName_;
      result.indicesOutputTensorName_ = indicesOutputTensorName_;
      result.shapesOutputTensorName_ = shapesOutputTensorName_;
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof VarLenFeatureProto) {
        return mergeFrom((VarLenFeatureProto)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(VarLenFeatureProto other) {
      if (other == VarLenFeatureProto.getDefaultInstance()) return this;
      if (other.dtype_ != 0) {
        setDtypeValue(other.getDtypeValue());
      }
      if (!other.getValuesOutputTensorName().isEmpty()) {
        valuesOutputTensorName_ = other.valuesOutputTensorName_;
        onChanged();
      }
      if (!other.getIndicesOutputTensorName().isEmpty()) {
        indicesOutputTensorName_ = other.indicesOutputTensorName_;
        onChanged();
      }
      if (!other.getShapesOutputTensorName().isEmpty()) {
        shapesOutputTensorName_ = other.shapesOutputTensorName_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      VarLenFeatureProto parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (VarLenFeatureProto) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int dtype_ = 0;
    /**
     * <code>.tensorflow.DataType dtype = 1;</code>
     */
    public int getDtypeValue() {
      return dtype_;
    }
    /**
     * <code>.tensorflow.DataType dtype = 1;</code>
     */
    public Builder setDtypeValue(int value) {
      dtype_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.tensorflow.DataType dtype = 1;</code>
     */
    public org.tensorflow.framework.DataType getDtype() {
      @SuppressWarnings("deprecation")
      org.tensorflow.framework.DataType result = org.tensorflow.framework.DataType.valueOf(dtype_);
      return result == null ? org.tensorflow.framework.DataType.UNRECOGNIZED : result;
    }
    /**
     * <code>.tensorflow.DataType dtype = 1;</code>
     */
    public Builder setDtype(org.tensorflow.framework.DataType value) {
      if (value == null) {
        throw new NullPointerException();
      }

      dtype_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.tensorflow.DataType dtype = 1;</code>
     */
    public Builder clearDtype() {

      dtype_ = 0;
      onChanged();
      return this;
    }

    private Object valuesOutputTensorName_ = "";
    /**
     * <code>string values_output_tensor_name = 2;</code>
     */
    public String getValuesOutputTensorName() {
      Object ref = valuesOutputTensorName_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        valuesOutputTensorName_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string values_output_tensor_name = 2;</code>
     */
    public com.google.protobuf.ByteString
        getValuesOutputTensorNameBytes() {
      Object ref = valuesOutputTensorName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        valuesOutputTensorName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string values_output_tensor_name = 2;</code>
     */
    public Builder setValuesOutputTensorName(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      valuesOutputTensorName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string values_output_tensor_name = 2;</code>
     */
    public Builder clearValuesOutputTensorName() {

      valuesOutputTensorName_ = getDefaultInstance().getValuesOutputTensorName();
      onChanged();
      return this;
    }
    /**
     * <code>string values_output_tensor_name = 2;</code>
     */
    public Builder setValuesOutputTensorNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      valuesOutputTensorName_ = value;
      onChanged();
      return this;
    }

    private Object indicesOutputTensorName_ = "";
    /**
     * <code>string indices_output_tensor_name = 3;</code>
     */
    public String getIndicesOutputTensorName() {
      Object ref = indicesOutputTensorName_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        indicesOutputTensorName_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string indices_output_tensor_name = 3;</code>
     */
    public com.google.protobuf.ByteString
        getIndicesOutputTensorNameBytes() {
      Object ref = indicesOutputTensorName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        indicesOutputTensorName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string indices_output_tensor_name = 3;</code>
     */
    public Builder setIndicesOutputTensorName(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      indicesOutputTensorName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string indices_output_tensor_name = 3;</code>
     */
    public Builder clearIndicesOutputTensorName() {

      indicesOutputTensorName_ = getDefaultInstance().getIndicesOutputTensorName();
      onChanged();
      return this;
    }
    /**
     * <code>string indices_output_tensor_name = 3;</code>
     */
    public Builder setIndicesOutputTensorNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      indicesOutputTensorName_ = value;
      onChanged();
      return this;
    }

    private Object shapesOutputTensorName_ = "";
    /**
     * <code>string shapes_output_tensor_name = 4;</code>
     */
    public String getShapesOutputTensorName() {
      Object ref = shapesOutputTensorName_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        shapesOutputTensorName_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string shapes_output_tensor_name = 4;</code>
     */
    public com.google.protobuf.ByteString
        getShapesOutputTensorNameBytes() {
      Object ref = shapesOutputTensorName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        shapesOutputTensorName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string shapes_output_tensor_name = 4;</code>
     */
    public Builder setShapesOutputTensorName(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      shapesOutputTensorName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string shapes_output_tensor_name = 4;</code>
     */
    public Builder clearShapesOutputTensorName() {

      shapesOutputTensorName_ = getDefaultInstance().getShapesOutputTensorName();
      onChanged();
      return this;
    }
    /**
     * <code>string shapes_output_tensor_name = 4;</code>
     */
    public Builder setShapesOutputTensorNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      shapesOutputTensorName_ = value;
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:tensorflow.VarLenFeatureProto)
  }

  // @@protoc_insertion_point(class_scope:tensorflow.VarLenFeatureProto)
  private static final VarLenFeatureProto DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new VarLenFeatureProto();
  }

  public static VarLenFeatureProto getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<VarLenFeatureProto>
      PARSER = new com.google.protobuf.AbstractParser<VarLenFeatureProto>() {
    @Override
    public VarLenFeatureProto parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new VarLenFeatureProto(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<VarLenFeatureProto> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<VarLenFeatureProto> getParserForType() {
    return PARSER;
  }

  @Override
  public VarLenFeatureProto getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


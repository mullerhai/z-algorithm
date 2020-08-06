// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/step_stats.proto

package org.tensorflow.framework;

/**
 * <pre>
 * An allocation/de-allocation operation performed by the allocator.
 * </pre>
 *
 * Protobuf type {@code tensorflow.AllocationRecord}
 */
public  final class AllocationRecord extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:tensorflow.AllocationRecord)
    AllocationRecordOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AllocationRecord.newBuilder() to construct.
  private AllocationRecord(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AllocationRecord() {
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private AllocationRecord(
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

            allocMicros_ = input.readInt64();
            break;
          }
          case 16: {

            allocBytes_ = input.readInt64();
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
    return StepStatsProtos.internal_static_tensorflow_AllocationRecord_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return StepStatsProtos.internal_static_tensorflow_AllocationRecord_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            AllocationRecord.class, Builder.class);
  }

  public static final int ALLOC_MICROS_FIELD_NUMBER = 1;
  private long allocMicros_;
  /**
   * <pre>
   * The timestamp of the operation.
   * </pre>
   *
   * <code>int64 alloc_micros = 1;</code>
   */
  public long getAllocMicros() {
    return allocMicros_;
  }

  public static final int ALLOC_BYTES_FIELD_NUMBER = 2;
  private long allocBytes_;
  /**
   * <pre>
   * Number of bytes allocated, or de-allocated if negative.
   * </pre>
   *
   * <code>int64 alloc_bytes = 2;</code>
   */
  public long getAllocBytes() {
    return allocBytes_;
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
    if (allocMicros_ != 0L) {
      output.writeInt64(1, allocMicros_);
    }
    if (allocBytes_ != 0L) {
      output.writeInt64(2, allocBytes_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (allocMicros_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, allocMicros_);
    }
    if (allocBytes_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, allocBytes_);
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
    if (!(obj instanceof AllocationRecord)) {
      return super.equals(obj);
    }
    AllocationRecord other = (AllocationRecord) obj;

    if (getAllocMicros()
        != other.getAllocMicros()) return false;
    if (getAllocBytes()
        != other.getAllocBytes()) return false;
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
    hash = (37 * hash) + ALLOC_MICROS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getAllocMicros());
    hash = (37 * hash) + ALLOC_BYTES_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getAllocBytes());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static AllocationRecord parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AllocationRecord parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AllocationRecord parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AllocationRecord parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AllocationRecord parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static AllocationRecord parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static AllocationRecord parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static AllocationRecord parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static AllocationRecord parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static AllocationRecord parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static AllocationRecord parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static AllocationRecord parseFrom(
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
  public static Builder newBuilder(AllocationRecord prototype) {
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
   * <pre>
   * An allocation/de-allocation operation performed by the allocator.
   * </pre>
   *
   * Protobuf type {@code tensorflow.AllocationRecord}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:tensorflow.AllocationRecord)
      AllocationRecordOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return StepStatsProtos.internal_static_tensorflow_AllocationRecord_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return StepStatsProtos.internal_static_tensorflow_AllocationRecord_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              AllocationRecord.class, Builder.class);
    }

    // Construct using org.tensorflow.framework.AllocationRecord.newBuilder()
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
      allocMicros_ = 0L;

      allocBytes_ = 0L;

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return StepStatsProtos.internal_static_tensorflow_AllocationRecord_descriptor;
    }

    @Override
    public AllocationRecord getDefaultInstanceForType() {
      return AllocationRecord.getDefaultInstance();
    }

    @Override
    public AllocationRecord build() {
      AllocationRecord result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public AllocationRecord buildPartial() {
      AllocationRecord result = new AllocationRecord(this);
      result.allocMicros_ = allocMicros_;
      result.allocBytes_ = allocBytes_;
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
      if (other instanceof AllocationRecord) {
        return mergeFrom((AllocationRecord)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(AllocationRecord other) {
      if (other == AllocationRecord.getDefaultInstance()) return this;
      if (other.getAllocMicros() != 0L) {
        setAllocMicros(other.getAllocMicros());
      }
      if (other.getAllocBytes() != 0L) {
        setAllocBytes(other.getAllocBytes());
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
      AllocationRecord parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (AllocationRecord) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long allocMicros_ ;
    /**
     * <pre>
     * The timestamp of the operation.
     * </pre>
     *
     * <code>int64 alloc_micros = 1;</code>
     */
    public long getAllocMicros() {
      return allocMicros_;
    }
    /**
     * <pre>
     * The timestamp of the operation.
     * </pre>
     *
     * <code>int64 alloc_micros = 1;</code>
     */
    public Builder setAllocMicros(long value) {

      allocMicros_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The timestamp of the operation.
     * </pre>
     *
     * <code>int64 alloc_micros = 1;</code>
     */
    public Builder clearAllocMicros() {

      allocMicros_ = 0L;
      onChanged();
      return this;
    }

    private long allocBytes_ ;
    /**
     * <pre>
     * Number of bytes allocated, or de-allocated if negative.
     * </pre>
     *
     * <code>int64 alloc_bytes = 2;</code>
     */
    public long getAllocBytes() {
      return allocBytes_;
    }
    /**
     * <pre>
     * Number of bytes allocated, or de-allocated if negative.
     * </pre>
     *
     * <code>int64 alloc_bytes = 2;</code>
     */
    public Builder setAllocBytes(long value) {

      allocBytes_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Number of bytes allocated, or de-allocated if negative.
     * </pre>
     *
     * <code>int64 alloc_bytes = 2;</code>
     */
    public Builder clearAllocBytes() {

      allocBytes_ = 0L;
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


    // @@protoc_insertion_point(builder_scope:tensorflow.AllocationRecord)
  }

  // @@protoc_insertion_point(class_scope:tensorflow.AllocationRecord)
  private static final AllocationRecord DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new AllocationRecord();
  }

  public static AllocationRecord getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AllocationRecord>
      PARSER = new com.google.protobuf.AbstractParser<AllocationRecord>() {
    @Override
    public AllocationRecord parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new AllocationRecord(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<AllocationRecord> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<AllocationRecord> getParserForType() {
    return PARSER;
  }

  @Override
  public AllocationRecord getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


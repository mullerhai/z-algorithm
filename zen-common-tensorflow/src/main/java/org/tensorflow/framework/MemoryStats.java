// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/step_stats.proto

package org.tensorflow.framework;

import org.tensorflow.util.LongArrayList;

/**
 * <pre>
 * For memory tracking.
 * </pre>
 *
 * Protobuf type {@code tensorflow.MemoryStats}
 */
public  final class MemoryStats extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:tensorflow.MemoryStats)
        MemoryStatsOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MemoryStats.newBuilder() to construct.
  private MemoryStats(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MemoryStats() {
    persistentTensorAllocIds_ = LongArrayList.emptyList();
    devicePersistentTensorAllocIds_ = LongArrayList.emptyList();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private MemoryStats(
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

            tempMemorySize_ = input.readInt64();
            break;
          }
          case 16: {

            deviceTempMemorySize_ = input.readInt64();
            break;
          }
          case 24: {

            persistentMemorySize_ = input.readInt64();
            break;
          }
          case 32: {

            devicePersistentMemorySize_ = input.readInt64();
            break;
          }
          case 40: {
            if (!((mutable_bitField0_ & 0x00000004) != 0)) {
              persistentTensorAllocIds_ = LongArrayList.emptyList();
              mutable_bitField0_ |= 0x00000004;
            }
            persistentTensorAllocIds_.addLong(input.readInt64());
            break;
          }
          case 42: {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            if (!((mutable_bitField0_ & 0x00000004) != 0) && input.getBytesUntilLimit() > 0) {
              persistentTensorAllocIds_ = LongArrayList.emptyList();
              mutable_bitField0_ |= 0x00000004;
            }
            while (input.getBytesUntilLimit() > 0) {
              persistentTensorAllocIds_.addLong(input.readInt64());
            }
            input.popLimit(limit);
            break;
          }
          case 48: {
            if (!((mutable_bitField0_ & 0x00000020) != 0)) {
              devicePersistentTensorAllocIds_ = LongArrayList.emptyList();
              mutable_bitField0_ |= 0x00000020;
            }
            devicePersistentTensorAllocIds_.addLong(input.readInt64());
            break;
          }
          case 50: {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            if (!((mutable_bitField0_ & 0x00000020) != 0) && input.getBytesUntilLimit() > 0) {
              devicePersistentTensorAllocIds_ = LongArrayList.emptyList();
              mutable_bitField0_ |= 0x00000020;
            }
            while (input.getBytesUntilLimit() > 0) {
              devicePersistentTensorAllocIds_.addLong(input.readInt64());
            }
            input.popLimit(limit);
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
      if (((mutable_bitField0_ & 0x00000004) != 0)) {
        persistentTensorAllocIds_.makeImmutable(); // C
      }
      if (((mutable_bitField0_ & 0x00000020) != 0)) {
        devicePersistentTensorAllocIds_.makeImmutable(); // C
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return StepStatsProtos.internal_static_tensorflow_MemoryStats_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return StepStatsProtos.internal_static_tensorflow_MemoryStats_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            MemoryStats.class, Builder.class);
  }

  private int bitField0_;
  public static final int TEMP_MEMORY_SIZE_FIELD_NUMBER = 1;
  private long tempMemorySize_;
  /**
   * <code>int64 temp_memory_size = 1;</code>
   */
  public long getTempMemorySize() {
    return tempMemorySize_;
  }

  public static final int PERSISTENT_MEMORY_SIZE_FIELD_NUMBER = 3;
  private long persistentMemorySize_;
  /**
   * <code>int64 persistent_memory_size = 3;</code>
   */
  public long getPersistentMemorySize() {
    return persistentMemorySize_;
  }

  public static final int PERSISTENT_TENSOR_ALLOC_IDS_FIELD_NUMBER = 5;
  private LongArrayList persistentTensorAllocIds_;
  /**
   * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
   */
  public java.util.List<Long>
      getPersistentTensorAllocIdsList() {
    return persistentTensorAllocIds_;
  }
  /**
   * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
   */
  public int getPersistentTensorAllocIdsCount() {
    return persistentTensorAllocIds_.size();
  }
  /**
   * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
   */
  public long getPersistentTensorAllocIds(int index) {
    return persistentTensorAllocIds_.getLong(index);
  }
  private int persistentTensorAllocIdsMemoizedSerializedSize = -1;

  public static final int DEVICE_TEMP_MEMORY_SIZE_FIELD_NUMBER = 2;
  private long deviceTempMemorySize_;
  /**
   * <code>int64 device_temp_memory_size = 2 [deprecated = true];</code>
   */
  @Deprecated public long getDeviceTempMemorySize() {
    return deviceTempMemorySize_;
  }

  public static final int DEVICE_PERSISTENT_MEMORY_SIZE_FIELD_NUMBER = 4;
  private long devicePersistentMemorySize_;
  /**
   * <code>int64 device_persistent_memory_size = 4 [deprecated = true];</code>
   */
  @Deprecated public long getDevicePersistentMemorySize() {
    return devicePersistentMemorySize_;
  }

  public static final int DEVICE_PERSISTENT_TENSOR_ALLOC_IDS_FIELD_NUMBER = 6;
  private com.google.protobuf.Internal.LongList devicePersistentTensorAllocIds_;
  /**
   * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
   */
  @Deprecated public java.util.List<Long>
      getDevicePersistentTensorAllocIdsList() {
    return devicePersistentTensorAllocIds_;
  }
  /**
   * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
   */
  @Deprecated public int getDevicePersistentTensorAllocIdsCount() {
    return devicePersistentTensorAllocIds_.size();
  }
  /**
   * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
   */
  @Deprecated public long getDevicePersistentTensorAllocIds(int index) {
    return devicePersistentTensorAllocIds_.getLong(index);
  }
  private int devicePersistentTensorAllocIdsMemoizedSerializedSize = -1;

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
    getSerializedSize();
    if (tempMemorySize_ != 0L) {
      output.writeInt64(1, tempMemorySize_);
    }
    if (deviceTempMemorySize_ != 0L) {
      output.writeInt64(2, deviceTempMemorySize_);
    }
    if (persistentMemorySize_ != 0L) {
      output.writeInt64(3, persistentMemorySize_);
    }
    if (devicePersistentMemorySize_ != 0L) {
      output.writeInt64(4, devicePersistentMemorySize_);
    }
    if (getPersistentTensorAllocIdsList().size() > 0) {
      output.writeUInt32NoTag(42);
      output.writeUInt32NoTag(persistentTensorAllocIdsMemoizedSerializedSize);
    }
    for (int i = 0; i < persistentTensorAllocIds_.size(); i++) {
      output.writeInt64NoTag(persistentTensorAllocIds_.getLong(i));
    }
    if (getDevicePersistentTensorAllocIdsList().size() > 0) {
      output.writeUInt32NoTag(50);
      output.writeUInt32NoTag(devicePersistentTensorAllocIdsMemoizedSerializedSize);
    }
    for (int i = 0; i < devicePersistentTensorAllocIds_.size(); i++) {
      output.writeInt64NoTag(devicePersistentTensorAllocIds_.getLong(i));
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (tempMemorySize_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, tempMemorySize_);
    }
    if (deviceTempMemorySize_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, deviceTempMemorySize_);
    }
    if (persistentMemorySize_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, persistentMemorySize_);
    }
    if (devicePersistentMemorySize_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(4, devicePersistentMemorySize_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < persistentTensorAllocIds_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeInt64SizeNoTag(persistentTensorAllocIds_.getLong(i));
      }
      size += dataSize;
      if (!getPersistentTensorAllocIdsList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(dataSize);
      }
      persistentTensorAllocIdsMemoizedSerializedSize = dataSize;
    }
    {
      int dataSize = 0;
      for (int i = 0; i < devicePersistentTensorAllocIds_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeInt64SizeNoTag(devicePersistentTensorAllocIds_.getLong(i));
      }
      size += dataSize;
      if (!getDevicePersistentTensorAllocIdsList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(dataSize);
      }
      devicePersistentTensorAllocIdsMemoizedSerializedSize = dataSize;
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
    if (!(obj instanceof MemoryStats)) {
      return super.equals(obj);
    }
    MemoryStats other = (MemoryStats) obj;

    if (getTempMemorySize()
        != other.getTempMemorySize()) return false;
    if (getPersistentMemorySize()
        != other.getPersistentMemorySize()) return false;
    if (!getPersistentTensorAllocIdsList()
        .equals(other.getPersistentTensorAllocIdsList())) return false;
    if (getDeviceTempMemorySize()
        != other.getDeviceTempMemorySize()) return false;
    if (getDevicePersistentMemorySize()
        != other.getDevicePersistentMemorySize()) return false;
    if (!getDevicePersistentTensorAllocIdsList()
        .equals(other.getDevicePersistentTensorAllocIdsList())) return false;
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
    hash = (37 * hash) + TEMP_MEMORY_SIZE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTempMemorySize());
    hash = (37 * hash) + PERSISTENT_MEMORY_SIZE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getPersistentMemorySize());
    if (getPersistentTensorAllocIdsCount() > 0) {
      hash = (37 * hash) + PERSISTENT_TENSOR_ALLOC_IDS_FIELD_NUMBER;
      hash = (53 * hash) + getPersistentTensorAllocIdsList().hashCode();
    }
    hash = (37 * hash) + DEVICE_TEMP_MEMORY_SIZE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getDeviceTempMemorySize());
    hash = (37 * hash) + DEVICE_PERSISTENT_MEMORY_SIZE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getDevicePersistentMemorySize());
    if (getDevicePersistentTensorAllocIdsCount() > 0) {
      hash = (37 * hash) + DEVICE_PERSISTENT_TENSOR_ALLOC_IDS_FIELD_NUMBER;
      hash = (53 * hash) + getDevicePersistentTensorAllocIdsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static MemoryStats parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static MemoryStats parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static MemoryStats parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static MemoryStats parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static MemoryStats parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static MemoryStats parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static MemoryStats parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static MemoryStats parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static MemoryStats parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static MemoryStats parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static MemoryStats parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static MemoryStats parseFrom(
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
  public static Builder newBuilder(MemoryStats prototype) {
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
   * For memory tracking.
   * </pre>
   *
   * Protobuf type {@code tensorflow.MemoryStats}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:tensorflow.MemoryStats)
      MemoryStatsOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return StepStatsProtos.internal_static_tensorflow_MemoryStats_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return StepStatsProtos.internal_static_tensorflow_MemoryStats_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              MemoryStats.class, Builder.class);
    }

    // Construct using org.tensorflow.framework.MemoryStats.newBuilder()
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
      tempMemorySize_ = 0L;

      persistentMemorySize_ = 0L;

      persistentTensorAllocIds_ = LongArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000004);
      deviceTempMemorySize_ = 0L;

      devicePersistentMemorySize_ = 0L;

      devicePersistentTensorAllocIds_ = LongArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000020);
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return StepStatsProtos.internal_static_tensorflow_MemoryStats_descriptor;
    }

    @Override
    public MemoryStats getDefaultInstanceForType() {
      return MemoryStats.getDefaultInstance();
    }

    @Override
    public MemoryStats build() {
      MemoryStats result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public MemoryStats buildPartial() {
      MemoryStats result = new MemoryStats(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.tempMemorySize_ = tempMemorySize_;
      result.persistentMemorySize_ = persistentMemorySize_;
      if (((bitField0_ & 0x00000004) != 0)) {
        persistentTensorAllocIds_.makeImmutable();
        bitField0_ = (bitField0_ & ~0x00000004);
      }
      result.persistentTensorAllocIds_ = persistentTensorAllocIds_;
      result.deviceTempMemorySize_ = deviceTempMemorySize_;
      result.devicePersistentMemorySize_ = devicePersistentMemorySize_;
      if (((bitField0_ & 0x00000020) != 0)) {
        devicePersistentTensorAllocIds_.makeImmutable();
        bitField0_ = (bitField0_ & ~0x00000020);
      }
      result.devicePersistentTensorAllocIds_ = devicePersistentTensorAllocIds_;
      result.bitField0_ = to_bitField0_;
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
      if (other instanceof MemoryStats) {
        return mergeFrom((MemoryStats)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(MemoryStats other) {
      if (other == MemoryStats.getDefaultInstance()) return this;
      if (other.getTempMemorySize() != 0L) {
        setTempMemorySize(other.getTempMemorySize());
      }
      if (other.getPersistentMemorySize() != 0L) {
        setPersistentMemorySize(other.getPersistentMemorySize());
      }
      if (!other.persistentTensorAllocIds_.isEmpty()) {
        if (persistentTensorAllocIds_.isEmpty()) {
          persistentTensorAllocIds_ = other.persistentTensorAllocIds_;
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          ensurePersistentTensorAllocIdsIsMutable();
          persistentTensorAllocIds_.addAll(other.persistentTensorAllocIds_);
        }
        onChanged();
      }
      if (other.getDeviceTempMemorySize() != 0L) {
        setDeviceTempMemorySize(other.getDeviceTempMemorySize());
      }
      if (other.getDevicePersistentMemorySize() != 0L) {
        setDevicePersistentMemorySize(other.getDevicePersistentMemorySize());
      }
      if (!other.devicePersistentTensorAllocIds_.isEmpty()) {
        if (devicePersistentTensorAllocIds_.isEmpty()) {
          devicePersistentTensorAllocIds_ = other.devicePersistentTensorAllocIds_;
          bitField0_ = (bitField0_ & ~0x00000020);
        } else {
          ensureDevicePersistentTensorAllocIdsIsMutable();
          devicePersistentTensorAllocIds_.addAll(other.devicePersistentTensorAllocIds_);
        }
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
      MemoryStats parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (MemoryStats) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private long tempMemorySize_ ;
    /**
     * <code>int64 temp_memory_size = 1;</code>
     */
    public long getTempMemorySize() {
      return tempMemorySize_;
    }
    /**
     * <code>int64 temp_memory_size = 1;</code>
     */
    public Builder setTempMemorySize(long value) {

      tempMemorySize_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 temp_memory_size = 1;</code>
     */
    public Builder clearTempMemorySize() {

      tempMemorySize_ = 0L;
      onChanged();
      return this;
    }

    private long persistentMemorySize_ ;
    /**
     * <code>int64 persistent_memory_size = 3;</code>
     */
    public long getPersistentMemorySize() {
      return persistentMemorySize_;
    }
    /**
     * <code>int64 persistent_memory_size = 3;</code>
     */
    public Builder setPersistentMemorySize(long value) {

      persistentMemorySize_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 persistent_memory_size = 3;</code>
     */
    public Builder clearPersistentMemorySize() {

      persistentMemorySize_ = 0L;
      onChanged();
      return this;
    }

    private LongArrayList persistentTensorAllocIds_ = LongArrayList.emptyList();
    private void ensurePersistentTensorAllocIdsIsMutable() {
      if (!((bitField0_ & 0x00000004) != 0)) {
        persistentTensorAllocIds_ = persistentTensorAllocIds_.mutableCopyWithCapacity(persistentTensorAllocIds_.size());
        bitField0_ |= 0x00000004;
       }
    }
    /**
     * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
     */
    public java.util.List<Long>
        getPersistentTensorAllocIdsList() {
      return ((bitField0_ & 0x00000004) != 0) ?
               java.util.Collections.unmodifiableList(persistentTensorAllocIds_) : persistentTensorAllocIds_;
    }
    /**
     * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
     */
    public int getPersistentTensorAllocIdsCount() {
      return persistentTensorAllocIds_.size();
    }
    /**
     * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
     */
    public long getPersistentTensorAllocIds(int index) {
      return persistentTensorAllocIds_.getLong(index);
    }
    /**
     * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
     */
    public Builder setPersistentTensorAllocIds(
        int index, long value) {
      ensurePersistentTensorAllocIdsIsMutable();
      persistentTensorAllocIds_.setLong(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
     */
    public Builder addPersistentTensorAllocIds(long value) {
      ensurePersistentTensorAllocIdsIsMutable();
      persistentTensorAllocIds_.addLong(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
     */
    public Builder addAllPersistentTensorAllocIds(
        Iterable<? extends Long> values) {
      ensurePersistentTensorAllocIdsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, persistentTensorAllocIds_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 persistent_tensor_alloc_ids = 5;</code>
     */
    public Builder clearPersistentTensorAllocIds() {
      persistentTensorAllocIds_ = LongArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
    }

    private long deviceTempMemorySize_ ;
    /**
     * <code>int64 device_temp_memory_size = 2 [deprecated = true];</code>
     */
    @Deprecated public long getDeviceTempMemorySize() {
      return deviceTempMemorySize_;
    }
    /**
     * <code>int64 device_temp_memory_size = 2 [deprecated = true];</code>
     */
    @Deprecated public Builder setDeviceTempMemorySize(long value) {

      deviceTempMemorySize_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 device_temp_memory_size = 2 [deprecated = true];</code>
     */
    @Deprecated public Builder clearDeviceTempMemorySize() {

      deviceTempMemorySize_ = 0L;
      onChanged();
      return this;
    }

    private long devicePersistentMemorySize_ ;
    /**
     * <code>int64 device_persistent_memory_size = 4 [deprecated = true];</code>
     */
    @Deprecated public long getDevicePersistentMemorySize() {
      return devicePersistentMemorySize_;
    }
    /**
     * <code>int64 device_persistent_memory_size = 4 [deprecated = true];</code>
     */
    @Deprecated public Builder setDevicePersistentMemorySize(long value) {

      devicePersistentMemorySize_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 device_persistent_memory_size = 4 [deprecated = true];</code>
     */
    @Deprecated public Builder clearDevicePersistentMemorySize() {

      devicePersistentMemorySize_ = 0L;
      onChanged();
      return this;
    }

    private com.google.protobuf.Internal.LongList devicePersistentTensorAllocIds_ = LongArrayList.emptyList();
    private void ensureDevicePersistentTensorAllocIdsIsMutable() {
      if (!((bitField0_ & 0x00000020) != 0)) {
        devicePersistentTensorAllocIds_ = devicePersistentTensorAllocIds_.mutableCopyWithCapacity(devicePersistentTensorAllocIds_.size());
        bitField0_ |= 0x00000020;
       }
    }
    /**
     * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
     */
    @Deprecated public java.util.List<Long>
        getDevicePersistentTensorAllocIdsList() {
      return ((bitField0_ & 0x00000020) != 0) ?
               java.util.Collections.unmodifiableList(devicePersistentTensorAllocIds_) : devicePersistentTensorAllocIds_;
    }
    /**
     * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
     */
    @Deprecated public int getDevicePersistentTensorAllocIdsCount() {
      return devicePersistentTensorAllocIds_.size();
    }
    /**
     * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
     */
    @Deprecated public long getDevicePersistentTensorAllocIds(int index) {
      return devicePersistentTensorAllocIds_.getLong(index);
    }
    /**
     * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
     */
    @Deprecated public Builder setDevicePersistentTensorAllocIds(
        int index, long value) {
      ensureDevicePersistentTensorAllocIdsIsMutable();
      devicePersistentTensorAllocIds_.setLong(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
     */
    @Deprecated public Builder addDevicePersistentTensorAllocIds(long value) {
      ensureDevicePersistentTensorAllocIdsIsMutable();
      devicePersistentTensorAllocIds_.addLong(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
     */
    @Deprecated public Builder addAllDevicePersistentTensorAllocIds(
        Iterable<? extends Long> values) {
      ensureDevicePersistentTensorAllocIdsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, devicePersistentTensorAllocIds_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 device_persistent_tensor_alloc_ids = 6 [deprecated = true];</code>
     */
    @Deprecated public Builder clearDevicePersistentTensorAllocIds() {
      devicePersistentTensorAllocIds_ = LongArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000020);
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


    // @@protoc_insertion_point(builder_scope:tensorflow.MemoryStats)
  }

  // @@protoc_insertion_point(class_scope:tensorflow.MemoryStats)
  private static final MemoryStats DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new MemoryStats();
  }

  public static MemoryStats getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MemoryStats>
      PARSER = new com.google.protobuf.AbstractParser<MemoryStats>() {
    @Override
    public MemoryStats parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new MemoryStats(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<MemoryStats> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<MemoryStats> getParserForType() {
    return PARSER;
  }

  @Override
  public MemoryStats getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

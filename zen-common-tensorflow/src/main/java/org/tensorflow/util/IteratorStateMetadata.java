// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/iterator.proto

package org.tensorflow.util;

/**
 * <pre>
 * Protocol buffer representing the metadata for an iterator's state stored
 * as a Variant tensor.
 * </pre>
 *
 * Protobuf type {@code tensorflow.IteratorStateMetadata}
 */
public  final class IteratorStateMetadata extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:tensorflow.IteratorStateMetadata)
        IteratorStateMetadataOrBuilder {
private static final long serialVersionUID = 0L;
  // Use IteratorStateMetadata.newBuilder() to construct.
  private IteratorStateMetadata(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private IteratorStateMetadata() {
    version_ = "";
    keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private IteratorStateMetadata(
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
          case 10: {
            String s = input.readStringRequireUtf8();

            version_ = s;
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();
            if (!((mutable_bitField0_ & 0x00000002) != 0)) {
              keys_ = new com.google.protobuf.LazyStringArrayList();
              mutable_bitField0_ |= 0x00000002;
            }
            keys_.add(s);
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
      if (((mutable_bitField0_ & 0x00000002) != 0)) {
        keys_ = keys_.getUnmodifiableView();
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return IteratorProtos.internal_static_tensorflow_IteratorStateMetadata_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return IteratorProtos.internal_static_tensorflow_IteratorStateMetadata_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            IteratorStateMetadata.class, Builder.class);
  }

  private int bitField0_;
  public static final int VERSION_FIELD_NUMBER = 1;
  private volatile Object version_;
  /**
   * <pre>
   * A user-specified version string.
   * </pre>
   *
   * <code>string version = 1;</code>
   */
  public String getVersion() {
    Object ref = version_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      version_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * A user-specified version string.
   * </pre>
   *
   * <code>string version = 1;</code>
   */
  public com.google.protobuf.ByteString
      getVersionBytes() {
    Object ref = version_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      version_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int KEYS_FIELD_NUMBER = 2;
  private com.google.protobuf.LazyStringList keys_;
  /**
   * <pre>
   * Keys for tensors in the VariantTensorDataProto.
   * </pre>
   *
   * <code>repeated string keys = 2;</code>
   */
  public com.google.protobuf.ProtocolStringList
      getKeysList() {
    return keys_;
  }
  /**
   * <pre>
   * Keys for tensors in the VariantTensorDataProto.
   * </pre>
   *
   * <code>repeated string keys = 2;</code>
   */
  public int getKeysCount() {
    return keys_.size();
  }
  /**
   * <pre>
   * Keys for tensors in the VariantTensorDataProto.
   * </pre>
   *
   * <code>repeated string keys = 2;</code>
   */
  public String getKeys(int index) {
    return keys_.get(index);
  }
  /**
   * <pre>
   * Keys for tensors in the VariantTensorDataProto.
   * </pre>
   *
   * <code>repeated string keys = 2;</code>
   */
  public com.google.protobuf.ByteString
      getKeysBytes(int index) {
    return keys_.getByteString(index);
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
    if (!getVersionBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, version_);
    }
    for (int i = 0; i < keys_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, keys_.getRaw(i));
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getVersionBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, version_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < keys_.size(); i++) {
        dataSize += computeStringSizeNoTag(keys_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getKeysList().size();
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
    if (!(obj instanceof IteratorStateMetadata)) {
      return super.equals(obj);
    }
    IteratorStateMetadata other = (IteratorStateMetadata) obj;

    if (!getVersion()
        .equals(other.getVersion())) return false;
    if (!getKeysList()
        .equals(other.getKeysList())) return false;
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
    hash = (37 * hash) + VERSION_FIELD_NUMBER;
    hash = (53 * hash) + getVersion().hashCode();
    if (getKeysCount() > 0) {
      hash = (37 * hash) + KEYS_FIELD_NUMBER;
      hash = (53 * hash) + getKeysList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static IteratorStateMetadata parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static IteratorStateMetadata parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static IteratorStateMetadata parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static IteratorStateMetadata parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static IteratorStateMetadata parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static IteratorStateMetadata parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static IteratorStateMetadata parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static IteratorStateMetadata parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static IteratorStateMetadata parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static IteratorStateMetadata parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static IteratorStateMetadata parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static IteratorStateMetadata parseFrom(
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
  public static Builder newBuilder(IteratorStateMetadata prototype) {
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
   * Protocol buffer representing the metadata for an iterator's state stored
   * as a Variant tensor.
   * </pre>
   *
   * Protobuf type {@code tensorflow.IteratorStateMetadata}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:tensorflow.IteratorStateMetadata)
      IteratorStateMetadataOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return IteratorProtos.internal_static_tensorflow_IteratorStateMetadata_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return IteratorProtos.internal_static_tensorflow_IteratorStateMetadata_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              IteratorStateMetadata.class, Builder.class);
    }

    // Construct using org.tensorflow.util.IteratorStateMetadata.newBuilder()
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
      version_ = "";

      keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return IteratorProtos.internal_static_tensorflow_IteratorStateMetadata_descriptor;
    }

    @Override
    public IteratorStateMetadata getDefaultInstanceForType() {
      return IteratorStateMetadata.getDefaultInstance();
    }

    @Override
    public IteratorStateMetadata build() {
      IteratorStateMetadata result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public IteratorStateMetadata buildPartial() {
      IteratorStateMetadata result = new IteratorStateMetadata(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.version_ = version_;
      if (((bitField0_ & 0x00000002) != 0)) {
        keys_ = keys_.getUnmodifiableView();
        bitField0_ = (bitField0_ & ~0x00000002);
      }
      result.keys_ = keys_;
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
      if (other instanceof IteratorStateMetadata) {
        return mergeFrom((IteratorStateMetadata)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(IteratorStateMetadata other) {
      if (other == IteratorStateMetadata.getDefaultInstance()) return this;
      if (!other.getVersion().isEmpty()) {
        version_ = other.version_;
        onChanged();
      }
      if (!other.keys_.isEmpty()) {
        if (keys_.isEmpty()) {
          keys_ = other.keys_;
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          ensureKeysIsMutable();
          keys_.addAll(other.keys_);
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
      IteratorStateMetadata parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (IteratorStateMetadata) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private Object version_ = "";
    /**
     * <pre>
     * A user-specified version string.
     * </pre>
     *
     * <code>string version = 1;</code>
     */
    public String getVersion() {
      Object ref = version_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        version_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * A user-specified version string.
     * </pre>
     *
     * <code>string version = 1;</code>
     */
    public com.google.protobuf.ByteString
        getVersionBytes() {
      Object ref = version_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        version_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * A user-specified version string.
     * </pre>
     *
     * <code>string version = 1;</code>
     */
    public Builder setVersion(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      version_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * A user-specified version string.
     * </pre>
     *
     * <code>string version = 1;</code>
     */
    public Builder clearVersion() {

      version_ = getDefaultInstance().getVersion();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * A user-specified version string.
     * </pre>
     *
     * <code>string version = 1;</code>
     */
    public Builder setVersionBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      version_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringList keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    private void ensureKeysIsMutable() {
      if (!((bitField0_ & 0x00000002) != 0)) {
        keys_ = new com.google.protobuf.LazyStringArrayList(keys_);
        bitField0_ |= 0x00000002;
       }
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getKeysList() {
      return keys_.getUnmodifiableView();
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public int getKeysCount() {
      return keys_.size();
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public String getKeys(int index) {
      return keys_.get(index);
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public com.google.protobuf.ByteString
        getKeysBytes(int index) {
      return keys_.getByteString(index);
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public Builder setKeys(
        int index, String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureKeysIsMutable();
      keys_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public Builder addKeys(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureKeysIsMutable();
      keys_.add(value);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public Builder addAllKeys(
        Iterable<String> values) {
      ensureKeysIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, keys_);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public Builder clearKeys() {
      keys_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Keys for tensors in the VariantTensorDataProto.
     * </pre>
     *
     * <code>repeated string keys = 2;</code>
     */
    public Builder addKeysBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ensureKeysIsMutable();
      keys_.add(value);
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


    // @@protoc_insertion_point(builder_scope:tensorflow.IteratorStateMetadata)
  }

  // @@protoc_insertion_point(class_scope:tensorflow.IteratorStateMetadata)
  private static final IteratorStateMetadata DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new IteratorStateMetadata();
  }

  public static IteratorStateMetadata getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<IteratorStateMetadata>
      PARSER = new com.google.protobuf.AbstractParser<IteratorStateMetadata>() {
    @Override
    public IteratorStateMetadata parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new IteratorStateMetadata(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<IteratorStateMetadata> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<IteratorStateMetadata> getParserForType() {
    return PARSER;
  }

  @Override
  public IteratorStateMetadata getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


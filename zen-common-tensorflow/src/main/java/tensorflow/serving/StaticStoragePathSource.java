// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow_serving/sources/storage_path/static_storage_path_source.proto

package tensorflow.serving;

public final class StaticStoragePathSource {
  private StaticStoragePathSource() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface StaticStoragePathSourceConfigOrBuilder extends
      // @@protoc_insertion_point(interface_extends:tensorflow.serving.StaticStoragePathSourceConfig)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * The single servable name, version number and path to supply statically.
     * </pre>
     *
     * <code>string servable_name = 1;</code>
     */
    String getServableName();
    /**
     * <pre>
     * The single servable name, version number and path to supply statically.
     * </pre>
     *
     * <code>string servable_name = 1;</code>
     */
    com.google.protobuf.ByteString
        getServableNameBytes();

    /**
     * <code>int64 version_num = 2;</code>
     */
    long getVersionNum();

    /**
     * <code>string version_path = 3;</code>
     */
    String getVersionPath();
    /**
     * <code>string version_path = 3;</code>
     */
    com.google.protobuf.ByteString
        getVersionPathBytes();
  }
  /**
   * <pre>
   * Config proto for StaticStoragePathSource.
   * </pre>
   *
   * Protobuf type {@code tensorflow.serving.StaticStoragePathSourceConfig}
   */
  public  static final class StaticStoragePathSourceConfig extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:tensorflow.serving.StaticStoragePathSourceConfig)
      StaticStoragePathSourceConfigOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use StaticStoragePathSourceConfig.newBuilder() to construct.
    private StaticStoragePathSourceConfig(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private StaticStoragePathSourceConfig() {
      servableName_ = "";
      versionPath_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private StaticStoragePathSourceConfig(
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

              servableName_ = s;
              break;
            }
            case 16: {

              versionNum_ = input.readInt64();
              break;
            }
            case 26: {
              String s = input.readStringRequireUtf8();

              versionPath_ = s;
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
      return StaticStoragePathSource.internal_static_tensorflow_serving_StaticStoragePathSourceConfig_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return StaticStoragePathSource.internal_static_tensorflow_serving_StaticStoragePathSourceConfig_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              StaticStoragePathSourceConfig.class, Builder.class);
    }

    public static final int SERVABLE_NAME_FIELD_NUMBER = 1;
    private volatile Object servableName_;
    /**
     * <pre>
     * The single servable name, version number and path to supply statically.
     * </pre>
     *
     * <code>string servable_name = 1;</code>
     */
    public String getServableName() {
      Object ref = servableName_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        servableName_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * The single servable name, version number and path to supply statically.
     * </pre>
     *
     * <code>string servable_name = 1;</code>
     */
    public com.google.protobuf.ByteString
        getServableNameBytes() {
      Object ref = servableName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        servableName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int VERSION_NUM_FIELD_NUMBER = 2;
    private long versionNum_;
    /**
     * <code>int64 version_num = 2;</code>
     */
    public long getVersionNum() {
      return versionNum_;
    }

    public static final int VERSION_PATH_FIELD_NUMBER = 3;
    private volatile Object versionPath_;
    /**
     * <code>string version_path = 3;</code>
     */
    public String getVersionPath() {
      Object ref = versionPath_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        versionPath_ = s;
        return s;
      }
    }
    /**
     * <code>string version_path = 3;</code>
     */
    public com.google.protobuf.ByteString
        getVersionPathBytes() {
      Object ref = versionPath_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        versionPath_ = b;
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
      if (!getServableNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, servableName_);
      }
      if (versionNum_ != 0L) {
        output.writeInt64(2, versionNum_);
      }
      if (!getVersionPathBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, versionPath_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getServableNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, servableName_);
      }
      if (versionNum_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, versionNum_);
      }
      if (!getVersionPathBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, versionPath_);
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
      if (!(obj instanceof StaticStoragePathSourceConfig)) {
        return super.equals(obj);
      }
      StaticStoragePathSourceConfig other = (StaticStoragePathSourceConfig) obj;

      if (!getServableName()
          .equals(other.getServableName())) return false;
      if (getVersionNum()
          != other.getVersionNum()) return false;
      if (!getVersionPath()
          .equals(other.getVersionPath())) return false;
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
      hash = (37 * hash) + SERVABLE_NAME_FIELD_NUMBER;
      hash = (53 * hash) + getServableName().hashCode();
      hash = (37 * hash) + VERSION_NUM_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getVersionNum());
      hash = (37 * hash) + VERSION_PATH_FIELD_NUMBER;
      hash = (53 * hash) + getVersionPath().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static StaticStoragePathSourceConfig parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static StaticStoragePathSourceConfig parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static StaticStoragePathSourceConfig parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static StaticStoragePathSourceConfig parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static StaticStoragePathSourceConfig parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static StaticStoragePathSourceConfig parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static StaticStoragePathSourceConfig parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static StaticStoragePathSourceConfig parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static StaticStoragePathSourceConfig parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static StaticStoragePathSourceConfig parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static StaticStoragePathSourceConfig parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static StaticStoragePathSourceConfig parseFrom(
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
    public static Builder newBuilder(StaticStoragePathSourceConfig prototype) {
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
     * Config proto for StaticStoragePathSource.
     * </pre>
     *
     * Protobuf type {@code tensorflow.serving.StaticStoragePathSourceConfig}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:tensorflow.serving.StaticStoragePathSourceConfig)
        StaticStoragePathSourceConfigOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return StaticStoragePathSource.internal_static_tensorflow_serving_StaticStoragePathSourceConfig_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return StaticStoragePathSource.internal_static_tensorflow_serving_StaticStoragePathSourceConfig_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                StaticStoragePathSourceConfig.class, Builder.class);
      }

      // Construct using tensorflow.serving.StaticStoragePathSource.StaticStoragePathSourceConfig.newBuilder()
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
        servableName_ = "";

        versionNum_ = 0L;

        versionPath_ = "";

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return StaticStoragePathSource.internal_static_tensorflow_serving_StaticStoragePathSourceConfig_descriptor;
      }

      @Override
      public StaticStoragePathSourceConfig getDefaultInstanceForType() {
        return StaticStoragePathSourceConfig.getDefaultInstance();
      }

      @Override
      public StaticStoragePathSourceConfig build() {
        StaticStoragePathSourceConfig result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public StaticStoragePathSourceConfig buildPartial() {
        StaticStoragePathSourceConfig result = new StaticStoragePathSourceConfig(this);
        result.servableName_ = servableName_;
        result.versionNum_ = versionNum_;
        result.versionPath_ = versionPath_;
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
        if (other instanceof StaticStoragePathSourceConfig) {
          return mergeFrom((StaticStoragePathSourceConfig)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(StaticStoragePathSourceConfig other) {
        if (other == StaticStoragePathSourceConfig.getDefaultInstance()) return this;
        if (!other.getServableName().isEmpty()) {
          servableName_ = other.servableName_;
          onChanged();
        }
        if (other.getVersionNum() != 0L) {
          setVersionNum(other.getVersionNum());
        }
        if (!other.getVersionPath().isEmpty()) {
          versionPath_ = other.versionPath_;
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
        StaticStoragePathSourceConfig parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (StaticStoragePathSourceConfig) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object servableName_ = "";
      /**
       * <pre>
       * The single servable name, version number and path to supply statically.
       * </pre>
       *
       * <code>string servable_name = 1;</code>
       */
      public String getServableName() {
        Object ref = servableName_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          servableName_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       * The single servable name, version number and path to supply statically.
       * </pre>
       *
       * <code>string servable_name = 1;</code>
       */
      public com.google.protobuf.ByteString
          getServableNameBytes() {
        Object ref = servableName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          servableName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * The single servable name, version number and path to supply statically.
       * </pre>
       *
       * <code>string servable_name = 1;</code>
       */
      public Builder setServableName(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        servableName_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * The single servable name, version number and path to supply statically.
       * </pre>
       *
       * <code>string servable_name = 1;</code>
       */
      public Builder clearServableName() {

        servableName_ = getDefaultInstance().getServableName();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * The single servable name, version number and path to supply statically.
       * </pre>
       *
       * <code>string servable_name = 1;</code>
       */
      public Builder setServableNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        servableName_ = value;
        onChanged();
        return this;
      }

      private long versionNum_ ;
      /**
       * <code>int64 version_num = 2;</code>
       */
      public long getVersionNum() {
        return versionNum_;
      }
      /**
       * <code>int64 version_num = 2;</code>
       */
      public Builder setVersionNum(long value) {

        versionNum_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 version_num = 2;</code>
       */
      public Builder clearVersionNum() {

        versionNum_ = 0L;
        onChanged();
        return this;
      }

      private Object versionPath_ = "";
      /**
       * <code>string version_path = 3;</code>
       */
      public String getVersionPath() {
        Object ref = versionPath_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          versionPath_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string version_path = 3;</code>
       */
      public com.google.protobuf.ByteString
          getVersionPathBytes() {
        Object ref = versionPath_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          versionPath_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string version_path = 3;</code>
       */
      public Builder setVersionPath(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        versionPath_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string version_path = 3;</code>
       */
      public Builder clearVersionPath() {

        versionPath_ = getDefaultInstance().getVersionPath();
        onChanged();
        return this;
      }
      /**
       * <code>string version_path = 3;</code>
       */
      public Builder setVersionPathBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        versionPath_ = value;
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


      // @@protoc_insertion_point(builder_scope:tensorflow.serving.StaticStoragePathSourceConfig)
    }

    // @@protoc_insertion_point(class_scope:tensorflow.serving.StaticStoragePathSourceConfig)
    private static final StaticStoragePathSourceConfig DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new StaticStoragePathSourceConfig();
    }

    public static StaticStoragePathSourceConfig getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<StaticStoragePathSourceConfig>
        PARSER = new com.google.protobuf.AbstractParser<StaticStoragePathSourceConfig>() {
      @Override
      public StaticStoragePathSourceConfig parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new StaticStoragePathSourceConfig(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<StaticStoragePathSourceConfig> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<StaticStoragePathSourceConfig> getParserForType() {
      return PARSER;
    }

    @Override
    public StaticStoragePathSourceConfig getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_serving_StaticStoragePathSourceConfig_descriptor;
  private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_serving_StaticStoragePathSourceConfig_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\nHtensorflow_serving/sources/storage_pat" +
      "h/static_storage_path_source.proto\022\022tens" +
      "orflow.serving\"a\n\035StaticStoragePathSourc" +
      "eConfig\022\025\n\rservable_name\030\001 \001(\t\022\023\n\013versio" +
      "n_num\030\002 \001(\003\022\024\n\014version_path\030\003 \001(\tb\006proto" +
      "3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_tensorflow_serving_StaticStoragePathSourceConfig_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_tensorflow_serving_StaticStoragePathSourceConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_serving_StaticStoragePathSourceConfig_descriptor,
        new String[] { "ServableName", "VersionNum", "VersionPath", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

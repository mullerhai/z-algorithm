// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/resource_handle.proto

package org.tensorflow.framework;

public final class ResourceHandle {
  private ResourceHandle() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_ResourceHandleProto_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_ResourceHandleProto_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n/tensorflow/core/framework/resource_han" +
      "dle.proto\022\ntensorflow\"r\n\023ResourceHandleP" +
      "roto\022\016\n\006device\030\001 \001(\t\022\021\n\tcontainer\030\002 \001(\t\022" +
      "\014\n\004name\030\003 \001(\t\022\021\n\thash_code\030\004 \001(\004\022\027\n\017mayb" +
      "e_type_name\030\005 \001(\tBn\n\030org.tensorflow.fram" +
      "eworkB\016ResourceHandleP\001Z=github.com/tens" +
      "orflow/tensorflow/tensorflow/go/core/fra" +
      "mework\370\001\001b\006proto3"
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
    internal_static_tensorflow_ResourceHandleProto_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_tensorflow_ResourceHandleProto_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_ResourceHandleProto_descriptor,
        new String[] { "Device", "Container", "Name", "HashCode", "MaybeTypeName", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

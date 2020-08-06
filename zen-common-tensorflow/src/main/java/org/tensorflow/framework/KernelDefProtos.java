// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/kernel_def.proto

package org.tensorflow.framework;

public final class KernelDefProtos {
  private KernelDefProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_KernelDef_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_KernelDef_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_KernelDef_AttrConstraint_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_KernelDef_AttrConstraint_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n*tensorflow/core/framework/kernel_def.p" +
      "roto\022\ntensorflow\032*tensorflow/core/framew" +
      "ork/attr_value.proto\"\335\001\n\tKernelDef\022\n\n\002op" +
      "\030\001 \001(\t\022\023\n\013device_type\030\002 \001(\t\0228\n\nconstrain" +
      "t\030\003 \003(\0132$.tensorflow.KernelDef.AttrConst" +
      "raint\022\027\n\017host_memory_arg\030\004 \003(\t\022\r\n\005label\030" +
      "\005 \001(\t\032M\n\016AttrConstraint\022\014\n\004name\030\001 \001(\t\022-\n" +
      "\016allowed_values\030\002 \001(\0132\025.tensorflow.AttrV" +
      "alueB0\n\030org.tensorflow.frameworkB\017Kernel" +
      "DefProtosP\001\370\001\001b\006proto3"
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
          AttrValueProtos.getDescriptor(),
        }, assigner);
    internal_static_tensorflow_KernelDef_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_tensorflow_KernelDef_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_KernelDef_descriptor,
        new String[] { "Op", "DeviceType", "Constraint", "HostMemoryArg", "Label", });
    internal_static_tensorflow_KernelDef_AttrConstraint_descriptor =
      internal_static_tensorflow_KernelDef_descriptor.getNestedTypes().get(0);
    internal_static_tensorflow_KernelDef_AttrConstraint_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_KernelDef_AttrConstraint_descriptor,
        new String[] { "Name", "AllowedValues", });
    AttrValueProtos.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}

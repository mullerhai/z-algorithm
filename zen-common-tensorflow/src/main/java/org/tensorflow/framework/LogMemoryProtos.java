// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/log_memory.proto

package org.tensorflow.framework;

public final class LogMemoryProtos {
  private LogMemoryProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_MemoryLogStep_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_MemoryLogStep_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_MemoryLogTensorAllocation_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_MemoryLogTensorAllocation_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_MemoryLogTensorDeallocation_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_MemoryLogTensorDeallocation_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_MemoryLogTensorOutput_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_MemoryLogTensorOutput_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_MemoryLogRawAllocation_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_MemoryLogRawAllocation_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tensorflow_MemoryLogRawDeallocation_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tensorflow_MemoryLogRawDeallocation_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n*tensorflow/core/framework/log_memory.p" +
      "roto\022\ntensorflow\0322tensorflow/core/framew" +
      "ork/tensor_description.proto\"0\n\rMemoryLo" +
      "gStep\022\017\n\007step_id\030\001 \001(\003\022\016\n\006handle\030\002 \001(\t\"p" +
      "\n\031MemoryLogTensorAllocation\022\017\n\007step_id\030\001" +
      " \001(\003\022\023\n\013kernel_name\030\002 \001(\t\022-\n\006tensor\030\003 \001(" +
      "\0132\035.tensorflow.TensorDescription\"L\n\033Memo" +
      "ryLogTensorDeallocation\022\025\n\rallocation_id" +
      "\030\001 \001(\003\022\026\n\016allocator_name\030\002 \001(\t\"{\n\025Memory" +
      "LogTensorOutput\022\017\n\007step_id\030\001 \001(\003\022\023\n\013kern" +
      "el_name\030\002 \001(\t\022\r\n\005index\030\003 \001(\005\022-\n\006tensor\030\004" +
      " \001(\0132\035.tensorflow.TensorDescription\"\213\001\n\026" +
      "MemoryLogRawAllocation\022\017\n\007step_id\030\001 \001(\003\022" +
      "\021\n\toperation\030\002 \001(\t\022\021\n\tnum_bytes\030\003 \001(\003\022\013\n" +
      "\003ptr\030\004 \001(\004\022\025\n\rallocation_id\030\005 \001(\003\022\026\n\016all" +
      "ocator_name\030\006 \001(\t\"\177\n\030MemoryLogRawDealloc" +
      "ation\022\017\n\007step_id\030\001 \001(\003\022\021\n\toperation\030\002 \001(" +
      "\t\022\025\n\rallocation_id\030\003 \001(\003\022\026\n\016allocator_na" +
      "me\030\004 \001(\t\022\020\n\010deferred\030\005 \001(\010B0\n\030org.tensor" +
      "flow.frameworkB\017LogMemoryProtosP\001\370\001\001b\006pr" +
      "oto3"
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
          TensorDescriptionProtos.getDescriptor(),
        }, assigner);
    internal_static_tensorflow_MemoryLogStep_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_tensorflow_MemoryLogStep_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_MemoryLogStep_descriptor,
        new String[] { "StepId", "Handle", });
    internal_static_tensorflow_MemoryLogTensorAllocation_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_tensorflow_MemoryLogTensorAllocation_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_MemoryLogTensorAllocation_descriptor,
        new String[] { "StepId", "KernelName", "Tensor", });
    internal_static_tensorflow_MemoryLogTensorDeallocation_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_tensorflow_MemoryLogTensorDeallocation_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_MemoryLogTensorDeallocation_descriptor,
        new String[] { "AllocationId", "AllocatorName", });
    internal_static_tensorflow_MemoryLogTensorOutput_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_tensorflow_MemoryLogTensorOutput_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_MemoryLogTensorOutput_descriptor,
        new String[] { "StepId", "KernelName", "Index", "Tensor", });
    internal_static_tensorflow_MemoryLogRawAllocation_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_tensorflow_MemoryLogRawAllocation_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_MemoryLogRawAllocation_descriptor,
        new String[] { "StepId", "Operation", "NumBytes", "Ptr", "AllocationId", "AllocatorName", });
    internal_static_tensorflow_MemoryLogRawDeallocation_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_tensorflow_MemoryLogRawDeallocation_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tensorflow_MemoryLogRawDeallocation_descriptor,
        new String[] { "StepId", "Operation", "AllocationId", "AllocatorName", "Deferred", });
    TensorDescriptionProtos.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}

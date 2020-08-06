// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: apis/modelmgr/error_codes.proto

package tensorflow.serving;

public final class ErrorCodesProtos {
  private ErrorCodesProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * <pre>
   * The canonical error codes for TensorFlow APIs.
   * Warnings:
   * -   Do not change any numeric assignments.
   * -   Changes to this list should only be made if there is a compelling
   *     need that can't be satisfied in another way.  Such changes
   *     must be approved by at least two OWNERS.
   * Sometimes multiple error codes may apply.  Services should return
   * the most specific error code that applies.  For example, prefer
   * OUT_OF_RANGE over FAILED_PRECONDITION if both codes apply.
   * Similarly prefer NOT_FOUND or ALREADY_EXISTS over FAILED_PRECONDITION.
   * </pre>
   *
   * Protobuf enum {@code angel.serving.Code}
   */
  public enum Code
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     * Not an error; returned on success
     * </pre>
     *
     * <code>OK = 0;</code>
     */
    OK(0),
    /**
     * <pre>
     * The operation was cancelled (typically by the caller).
     * </pre>
     *
     * <code>CANCELLED = 1;</code>
     */
    CANCELLED(1),
    /**
     * <pre>
     * Unknown error.  An example of where this error may be returned is
     * if a Status value received from another address space belongs to
     * an error-space that is not known in this address space.  Also
     * errors raised by APIs that do not return enough error information
     * may be converted to this error.
     * </pre>
     *
     * <code>UNKNOWN = 2;</code>
     */
    UNKNOWN(2),
    /**
     * <pre>
     * Client specified an invalid argument.  Note that this differs
     * from FAILED_PRECONDITION.  INVALID_ARGUMENT indicates arguments
     * that are problematic regardless of the state of the system
     * (e.g., a malformed file name).
     * </pre>
     *
     * <code>INVALID_ARGUMENT = 3;</code>
     */
    INVALID_ARGUMENT(3),
    /**
     * <pre>
     * Deadline expired before operation could complete.  For operations
     * that change the state of the system, this error may be returned
     * even if the operation has completed successfully.  For example, a
     * successful response from a server could have been delayed long
     * enough for the deadline to expire.
     * </pre>
     *
     * <code>DEADLINE_EXCEEDED = 4;</code>
     */
    DEADLINE_EXCEEDED(4),
    /**
     * <pre>
     * Some requested entity (e.g., file or directory) was not found.
     * For privacy reasons, this code *may* be returned when the client
     * does not have the access right to the entity.
     * </pre>
     *
     * <code>NOT_FOUND = 5;</code>
     */
    NOT_FOUND(5),
    /**
     * <pre>
     * Some entity that we attempted to create (e.g., file or directory)
     * already exists.
     * </pre>
     *
     * <code>ALREADY_EXISTS = 6;</code>
     */
    ALREADY_EXISTS(6),
    /**
     * <pre>
     * The caller does not have permission to execute the specified
     * operation.  PERMISSION_DENIED must not be used for rejections
     * caused by exhausting some resource (use RESOURCE_EXHAUSTED
     * instead for those errors).  PERMISSION_DENIED must not be
     * used if the caller can not be identified (use UNAUTHENTICATED
     * instead for those errors).
     * </pre>
     *
     * <code>PERMISSION_DENIED = 7;</code>
     */
    PERMISSION_DENIED(7),
    /**
     * <pre>
     * The request does not have valid authentication credentials for the
     * operation.
     * </pre>
     *
     * <code>UNAUTHENTICATED = 16;</code>
     */
    UNAUTHENTICATED(16),
    /**
     * <pre>
     * Some resource has been exhausted, perhaps a per-user quota, or
     * perhaps the entire file system is out of space.
     * </pre>
     *
     * <code>RESOURCE_EXHAUSTED = 8;</code>
     */
    RESOURCE_EXHAUSTED(8),
    /**
     * <pre>
     * Operation was rejected because the system is not in a state
     * required for the operation's execution.  For example, directory
     * to be deleted may be non-empty, an rmdir operation is applied to
     * a non-directory, etc.
     * A litmus test that may help a service implementor in deciding
     * between FAILED_PRECONDITION, ABORTED, and UNAVAILABLE:
     *  (a) Use UNAVAILABLE if the client can retry just the failing call.
     *  (b) Use ABORTED if the client should retry at a higher-level
     *      (e.g., restarting a read-modify-write sequence).
     *  (c) Use FAILED_PRECONDITION if the client should not retry until
     *      the system state has been explicitly fixed.  E.g., if an "rmdir"
     *      fails because the directory is non-empty, FAILED_PRECONDITION
     *      should be returned since the client should not retry unless
     *      they have first fixed up the directory by deleting files from it.
     *  (d) Use FAILED_PRECONDITION if the client performs conditional
     *      REST Get/Update/Delete on a resource and the resource on the
     *      server does not match the condition. E.g., conflicting
     *      read-modify-write on the same resource.
     * </pre>
     *
     * <code>FAILED_PRECONDITION = 9;</code>
     */
    FAILED_PRECONDITION(9),
    /**
     * <pre>
     * The operation was aborted, typically due to a concurrency issue
     * like sequencer check failures, transaction aborts, etc.
     * See litmus test above for deciding between FAILED_PRECONDITION,
     * ABORTED, and UNAVAILABLE.
     * </pre>
     *
     * <code>ABORTED = 10;</code>
     */
    ABORTED(10),
    /**
     * <pre>
     * Operation tried to iterate past the valid input range.  E.g., seeking or
     * reading past end of file.
     * Unlike INVALID_ARGUMENT, this error indicates a problem that may
     * be fixed if the system state changes. For example, a 32-bit file
     * system will generate INVALID_ARGUMENT if asked to read at an
     * offset that is not in the range [0,2^32-1], but it will generate
     * OUT_OF_RANGE if asked to read from an offset past the current
     * file size.
     * There is a fair bit of overlap between FAILED_PRECONDITION and
     * OUT_OF_RANGE.  We recommend using OUT_OF_RANGE (the more specific
     * error) when it applies so that callers who are iterating through
     * a space can easily look for an OUT_OF_RANGE error to detect when
     * they are done.
     * </pre>
     *
     * <code>OUT_OF_RANGE = 11;</code>
     */
    OUT_OF_RANGE(11),
    /**
     * <pre>
     * Operation is not implemented or not supported/enabled in this service.
     * </pre>
     *
     * <code>UNIMPLEMENTED = 12;</code>
     */
    UNIMPLEMENTED(12),
    /**
     * <pre>
     * Internal errors.  Means some invariant expected by the underlying
     * system has been broken.  If you see one of these errors,
     * something is very broken.
     * </pre>
     *
     * <code>INTERNAL = 13;</code>
     */
    INTERNAL(13),
    /**
     * <pre>
     * The service is currently unavailable.  This is a most likely a
     * transient condition and may be corrected by retrying with
     * a backoff.
     * See litmus test above for deciding between FAILED_PRECONDITION,
     * ABORTED, and UNAVAILABLE.
     * </pre>
     *
     * <code>UNAVAILABLE = 14;</code>
     */
    UNAVAILABLE(14),
    /**
     * <pre>
     * Unrecoverable data loss or corruption.
     * </pre>
     *
     * <code>DATA_LOSS = 15;</code>
     */
    DATA_LOSS(15),
    /**
     * <pre>
     * An extra enum entry to prevent people from writing code that
     * fails to compile when a new code is added.
     * Nobody should ever reference this enumeration entry. In particular,
     * if you write C++ code that switches on this enumeration, add a default:
     * case instead of a case that mentions this enumeration entry.
     * Nobody should rely on the value (currently 20) listed here.  It
     * may change in the future.
     * </pre>
     *
     * <code>DO_NOT_USE_RESERVED_FOR_FUTURE_EXPANSION_USE_DEFAULT_IN_SWITCH_INSTEAD_ = 20;</code>
     */
    DO_NOT_USE_RESERVED_FOR_FUTURE_EXPANSION_USE_DEFAULT_IN_SWITCH_INSTEAD_(20),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     * Not an error; returned on success
     * </pre>
     *
     * <code>OK = 0;</code>
     */
    public static final int OK_VALUE = 0;
    /**
     * <pre>
     * The operation was cancelled (typically by the caller).
     * </pre>
     *
     * <code>CANCELLED = 1;</code>
     */
    public static final int CANCELLED_VALUE = 1;
    /**
     * <pre>
     * Unknown error.  An example of where this error may be returned is
     * if a Status value received from another address space belongs to
     * an error-space that is not known in this address space.  Also
     * errors raised by APIs that do not return enough error information
     * may be converted to this error.
     * </pre>
     *
     * <code>UNKNOWN = 2;</code>
     */
    public static final int UNKNOWN_VALUE = 2;
    /**
     * <pre>
     * Client specified an invalid argument.  Note that this differs
     * from FAILED_PRECONDITION.  INVALID_ARGUMENT indicates arguments
     * that are problematic regardless of the state of the system
     * (e.g., a malformed file name).
     * </pre>
     *
     * <code>INVALID_ARGUMENT = 3;</code>
     */
    public static final int INVALID_ARGUMENT_VALUE = 3;
    /**
     * <pre>
     * Deadline expired before operation could complete.  For operations
     * that change the state of the system, this error may be returned
     * even if the operation has completed successfully.  For example, a
     * successful response from a server could have been delayed long
     * enough for the deadline to expire.
     * </pre>
     *
     * <code>DEADLINE_EXCEEDED = 4;</code>
     */
    public static final int DEADLINE_EXCEEDED_VALUE = 4;
    /**
     * <pre>
     * Some requested entity (e.g., file or directory) was not found.
     * For privacy reasons, this code *may* be returned when the client
     * does not have the access right to the entity.
     * </pre>
     *
     * <code>NOT_FOUND = 5;</code>
     */
    public static final int NOT_FOUND_VALUE = 5;
    /**
     * <pre>
     * Some entity that we attempted to create (e.g., file or directory)
     * already exists.
     * </pre>
     *
     * <code>ALREADY_EXISTS = 6;</code>
     */
    public static final int ALREADY_EXISTS_VALUE = 6;
    /**
     * <pre>
     * The caller does not have permission to execute the specified
     * operation.  PERMISSION_DENIED must not be used for rejections
     * caused by exhausting some resource (use RESOURCE_EXHAUSTED
     * instead for those errors).  PERMISSION_DENIED must not be
     * used if the caller can not be identified (use UNAUTHENTICATED
     * instead for those errors).
     * </pre>
     *
     * <code>PERMISSION_DENIED = 7;</code>
     */
    public static final int PERMISSION_DENIED_VALUE = 7;
    /**
     * <pre>
     * The request does not have valid authentication credentials for the
     * operation.
     * </pre>
     *
     * <code>UNAUTHENTICATED = 16;</code>
     */
    public static final int UNAUTHENTICATED_VALUE = 16;
    /**
     * <pre>
     * Some resource has been exhausted, perhaps a per-user quota, or
     * perhaps the entire file system is out of space.
     * </pre>
     *
     * <code>RESOURCE_EXHAUSTED = 8;</code>
     */
    public static final int RESOURCE_EXHAUSTED_VALUE = 8;
    /**
     * <pre>
     * Operation was rejected because the system is not in a state
     * required for the operation's execution.  For example, directory
     * to be deleted may be non-empty, an rmdir operation is applied to
     * a non-directory, etc.
     * A litmus test that may help a service implementor in deciding
     * between FAILED_PRECONDITION, ABORTED, and UNAVAILABLE:
     *  (a) Use UNAVAILABLE if the client can retry just the failing call.
     *  (b) Use ABORTED if the client should retry at a higher-level
     *      (e.g., restarting a read-modify-write sequence).
     *  (c) Use FAILED_PRECONDITION if the client should not retry until
     *      the system state has been explicitly fixed.  E.g., if an "rmdir"
     *      fails because the directory is non-empty, FAILED_PRECONDITION
     *      should be returned since the client should not retry unless
     *      they have first fixed up the directory by deleting files from it.
     *  (d) Use FAILED_PRECONDITION if the client performs conditional
     *      REST Get/Update/Delete on a resource and the resource on the
     *      server does not match the condition. E.g., conflicting
     *      read-modify-write on the same resource.
     * </pre>
     *
     * <code>FAILED_PRECONDITION = 9;</code>
     */
    public static final int FAILED_PRECONDITION_VALUE = 9;
    /**
     * <pre>
     * The operation was aborted, typically due to a concurrency issue
     * like sequencer check failures, transaction aborts, etc.
     * See litmus test above for deciding between FAILED_PRECONDITION,
     * ABORTED, and UNAVAILABLE.
     * </pre>
     *
     * <code>ABORTED = 10;</code>
     */
    public static final int ABORTED_VALUE = 10;
    /**
     * <pre>
     * Operation tried to iterate past the valid input range.  E.g., seeking or
     * reading past end of file.
     * Unlike INVALID_ARGUMENT, this error indicates a problem that may
     * be fixed if the system state changes. For example, a 32-bit file
     * system will generate INVALID_ARGUMENT if asked to read at an
     * offset that is not in the range [0,2^32-1], but it will generate
     * OUT_OF_RANGE if asked to read from an offset past the current
     * file size.
     * There is a fair bit of overlap between FAILED_PRECONDITION and
     * OUT_OF_RANGE.  We recommend using OUT_OF_RANGE (the more specific
     * error) when it applies so that callers who are iterating through
     * a space can easily look for an OUT_OF_RANGE error to detect when
     * they are done.
     * </pre>
     *
     * <code>OUT_OF_RANGE = 11;</code>
     */
    public static final int OUT_OF_RANGE_VALUE = 11;
    /**
     * <pre>
     * Operation is not implemented or not supported/enabled in this service.
     * </pre>
     *
     * <code>UNIMPLEMENTED = 12;</code>
     */
    public static final int UNIMPLEMENTED_VALUE = 12;
    /**
     * <pre>
     * Internal errors.  Means some invariant expected by the underlying
     * system has been broken.  If you see one of these errors,
     * something is very broken.
     * </pre>
     *
     * <code>INTERNAL = 13;</code>
     */
    public static final int INTERNAL_VALUE = 13;
    /**
     * <pre>
     * The service is currently unavailable.  This is a most likely a
     * transient condition and may be corrected by retrying with
     * a backoff.
     * See litmus test above for deciding between FAILED_PRECONDITION,
     * ABORTED, and UNAVAILABLE.
     * </pre>
     *
     * <code>UNAVAILABLE = 14;</code>
     */
    public static final int UNAVAILABLE_VALUE = 14;
    /**
     * <pre>
     * Unrecoverable data loss or corruption.
     * </pre>
     *
     * <code>DATA_LOSS = 15;</code>
     */
    public static final int DATA_LOSS_VALUE = 15;
    /**
     * <pre>
     * An extra enum entry to prevent people from writing code that
     * fails to compile when a new code is added.
     * Nobody should ever reference this enumeration entry. In particular,
     * if you write C++ code that switches on this enumeration, add a default:
     * case instead of a case that mentions this enumeration entry.
     * Nobody should rely on the value (currently 20) listed here.  It
     * may change in the future.
     * </pre>
     *
     * <code>DO_NOT_USE_RESERVED_FOR_FUTURE_EXPANSION_USE_DEFAULT_IN_SWITCH_INSTEAD_ = 20;</code>
     */
    public static final int DO_NOT_USE_RESERVED_FOR_FUTURE_EXPANSION_USE_DEFAULT_IN_SWITCH_INSTEAD__VALUE = 20;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @Deprecated
    public static Code valueOf(int value) {
      return forNumber(value);
    }

    public static Code forNumber(int value) {
      switch (value) {
        case 0: return OK;
        case 1: return CANCELLED;
        case 2: return UNKNOWN;
        case 3: return INVALID_ARGUMENT;
        case 4: return DEADLINE_EXCEEDED;
        case 5: return NOT_FOUND;
        case 6: return ALREADY_EXISTS;
        case 7: return PERMISSION_DENIED;
        case 16: return UNAUTHENTICATED;
        case 8: return RESOURCE_EXHAUSTED;
        case 9: return FAILED_PRECONDITION;
        case 10: return ABORTED;
        case 11: return OUT_OF_RANGE;
        case 12: return UNIMPLEMENTED;
        case 13: return INTERNAL;
        case 14: return UNAVAILABLE;
        case 15: return DATA_LOSS;
        case 20: return DO_NOT_USE_RESERVED_FOR_FUTURE_EXPANSION_USE_DEFAULT_IN_SWITCH_INSTEAD_;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Code>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        Code> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Code>() {
            public Code findValueByNumber(int number) {
              return Code.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return ErrorCodesProtos.getDescriptor().getEnumTypes().get(0);
    }

    private static final Code[] VALUES = values();

    public static Code valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private Code(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:angel.serving.Code)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\037apis/modelmgr/error_codes.proto\022\rangel" +
      ".serving*\204\003\n\004Code\022\006\n\002OK\020\000\022\r\n\tCANCELLED\020\001" +
      "\022\013\n\007UNKNOWN\020\002\022\024\n\020INVALID_ARGUMENT\020\003\022\025\n\021D" +
      "EADLINE_EXCEEDED\020\004\022\r\n\tNOT_FOUND\020\005\022\022\n\016ALR" +
      "EADY_EXISTS\020\006\022\025\n\021PERMISSION_DENIED\020\007\022\023\n\017" +
      "UNAUTHENTICATED\020\020\022\026\n\022RESOURCE_EXHAUSTED\020" +
      "\010\022\027\n\023FAILED_PRECONDITION\020\t\022\013\n\007ABORTED\020\n\022" +
      "\020\n\014OUT_OF_RANGE\020\013\022\021\n\rUNIMPLEMENTED\020\014\022\014\n\010" +
      "INTERNAL\020\r\022\017\n\013UNAVAILABLE\020\016\022\r\n\tDATA_LOSS" +
      "\020\017\022K\nGDO_NOT_USE_RESERVED_FOR_FUTURE_EXP" +
      "ANSION_USE_DEFAULT_IN_SWITCH_INSTEAD_\020\024B" +
      "=\n\'com.tencent.angel.serving.apis.modelm" +
      "grB\020ErrorCodesProtosP\000b\006proto3"
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
  }

  // @@protoc_insertion_point(outer_class_scope)
}

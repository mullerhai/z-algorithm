// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/device_attributes.proto

package org.tensorflow.framework;

public interface InterconnectLinkOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tensorflow.InterconnectLink)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 device_id = 1;</code>
   */
  int getDeviceId();

  /**
   * <code>string type = 2;</code>
   */
  String getType();
  /**
   * <code>string type = 2;</code>
   */
  com.google.protobuf.ByteString
      getTypeBytes();

  /**
   * <code>int32 strength = 3;</code>
   */
  int getStrength();
}

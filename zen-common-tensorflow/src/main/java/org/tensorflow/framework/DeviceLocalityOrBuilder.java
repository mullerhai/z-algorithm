// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/framework/device_attributes.proto

package org.tensorflow.framework;

public interface DeviceLocalityOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tensorflow.DeviceLocality)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Optional bus locality of device.  Default value of 0 means
   * no specific locality.  Specific localities are indexed from 1.
   * </pre>
   *
   * <code>int32 bus_id = 1;</code>
   */
  int getBusId();

  /**
   * <pre>
   * Optional NUMA locality of device.
   * </pre>
   *
   * <code>int32 numa_node = 2;</code>
   */
  int getNumaNode();

  /**
   * <pre>
   * Optional local interconnect links to other devices.
   * </pre>
   *
   * <code>.tensorflow.LocalLinks links = 3;</code>
   */
  boolean hasLinks();
  /**
   * <pre>
   * Optional local interconnect links to other devices.
   * </pre>
   *
   * <code>.tensorflow.LocalLinks links = 3;</code>
   */
  LocalLinks getLinks();
  /**
   * <pre>
   * Optional local interconnect links to other devices.
   * </pre>
   *
   * <code>.tensorflow.LocalLinks links = 3;</code>
   */
  LocalLinksOrBuilder getLinksOrBuilder();
}

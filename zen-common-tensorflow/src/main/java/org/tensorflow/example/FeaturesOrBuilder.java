// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tensorflow/core/example/feature.proto

package org.tensorflow.example;

public interface FeaturesOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tensorflow.Features)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Map from feature name to feature.
   * </pre>
   *
   * <code>map&lt;string, .tensorflow.Feature&gt; feature = 1;</code>
   */
  int getFeatureCount();
  /**
   * <pre>
   * Map from feature name to feature.
   * </pre>
   *
   * <code>map&lt;string, .tensorflow.Feature&gt; feature = 1;</code>
   */
  boolean containsFeature(
          String key);
  /**
   * Use {@link #getFeatureMap()} instead.
   */
  @Deprecated
  java.util.Map<String, Feature>
  getFeature();
  /**
   * <pre>
   * Map from feature name to feature.
   * </pre>
   *
   * <code>map&lt;string, .tensorflow.Feature&gt; feature = 1;</code>
   */
  java.util.Map<String, Feature>
  getFeatureMap();
  /**
   * <pre>
   * Map from feature name to feature.
   * </pre>
   *
   * <code>map&lt;string, .tensorflow.Feature&gt; feature = 1;</code>
   */

  Feature getFeatureOrDefault(
          String key,
          Feature defaultValue);
  /**
   * <pre>
   * Map from feature name to feature.
   * </pre>
   *
   * <code>map&lt;string, .tensorflow.Feature&gt; feature = 1;</code>
   */

  Feature getFeatureOrThrow(
          String key);
}
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: world.proto

package com.tt.message.proto;

public interface ChangePositionOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.protobuf.ChangePosition)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 id = 1;</code>
   * @return The id.
   */
  long getId();

  /**
   * <pre>
   * 位置
   * </pre>
   *
   * <code>.google.protobuf.VectorProto position = 2;</code>
   * @return Whether the position field is set.
   */
  boolean hasPosition();
  /**
   * <pre>
   * 位置
   * </pre>
   *
   * <code>.google.protobuf.VectorProto position = 2;</code>
   * @return The position.
   */
  com.tt.message.proto.VectorProto getPosition();
  /**
   * <pre>
   * 位置
   * </pre>
   *
   * <code>.google.protobuf.VectorProto position = 2;</code>
   */
  com.tt.message.proto.VectorProtoOrBuilder getPositionOrBuilder();

  /**
   * <pre>
   * 朝向
   * </pre>
   *
   * <code>float toward = 3;</code>
   * @return The toward.
   */
  float getToward();
}

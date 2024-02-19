// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: world.proto

package com.tt.message.proto;

public interface GameObjInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.protobuf.GameObjInfo)
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

  /**
   * <pre>
   * 类型
   * </pre>
   *
   * <code>.google.protobuf.GameObjType type = 4;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <pre>
   * 类型
   * </pre>
   *
   * <code>.google.protobuf.GameObjType type = 4;</code>
   * @return The type.
   */
  com.tt.message.proto.GameObjType getType();

  /**
   * <pre>
   * 扩展信息
   * </pre>
   *
   * <code>.google.protobuf.Any typeExtendInfo = 5;</code>
   * @return Whether the typeExtendInfo field is set.
   */
  boolean hasTypeExtendInfo();
  /**
   * <pre>
   * 扩展信息
   * </pre>
   *
   * <code>.google.protobuf.Any typeExtendInfo = 5;</code>
   * @return The typeExtendInfo.
   */
  com.google.protobuf.Any getTypeExtendInfo();
  /**
   * <pre>
   * 扩展信息
   * </pre>
   *
   * <code>.google.protobuf.Any typeExtendInfo = 5;</code>
   */
  com.google.protobuf.AnyOrBuilder getTypeExtendInfoOrBuilder();

  /**
   * <pre>
   * 是否移动到新灯塔，客户端可能收到两个同样对象，以这个字段为false的为准
   * </pre>
   *
   * <code>bool moveToOtherTower = 6;</code>
   * @return The moveToOtherTower.
   */
  boolean getMoveToOtherTower();
}
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: BaseMessage.proto

package com.tt.message.proto;

public interface StringArrayOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.protobuf.StringArray)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated string value = 1;</code>
   * @return A list containing the value.
   */
  java.util.List<java.lang.String>
      getValueList();
  /**
   * <code>repeated string value = 1;</code>
   * @return The count of value.
   */
  int getValueCount();
  /**
   * <code>repeated string value = 1;</code>
   * @param index The index of the element to return.
   * @return The value at the given index.
   */
  java.lang.String getValue(int index);
  /**
   * <code>repeated string value = 1;</code>
   * @param index The index of the value to return.
   * @return The bytes of the value at the given index.
   */
  com.google.protobuf.ByteString
      getValueBytes(int index);
}
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: world.proto

package com.tt.message.proto;

/**
 * <pre>
 * 修改位置
 * </pre>
 *
 * Protobuf type {@code google.protobuf.ChangePosition}
 */
public final class ChangePosition extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.protobuf.ChangePosition)
    ChangePositionOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ChangePosition.newBuilder() to construct.
  private ChangePosition(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ChangePosition() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ChangePosition();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ChangePosition(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            id_ = input.readInt64();
            break;
          }
          case 18: {
            com.tt.message.proto.VectorProto.Builder subBuilder = null;
            if (position_ != null) {
              subBuilder = position_.toBuilder();
            }
            position_ = input.readMessage(com.tt.message.proto.VectorProto.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(position_);
              position_ = subBuilder.buildPartial();
            }

            break;
          }
          case 29: {

            toward_ = input.readFloat();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.tt.message.proto.WorldMessageProto.internal_static_google_protobuf_ChangePosition_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.tt.message.proto.WorldMessageProto.internal_static_google_protobuf_ChangePosition_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.tt.message.proto.ChangePosition.class, com.tt.message.proto.ChangePosition.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private long id_;
  /**
   * <code>int64 id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public long getId() {
    return id_;
  }

  public static final int POSITION_FIELD_NUMBER = 2;
  private com.tt.message.proto.VectorProto position_;
  /**
   * <pre>
   * 位置
   * </pre>
   *
   * <code>.google.protobuf.VectorProto position = 2;</code>
   * @return Whether the position field is set.
   */
  @java.lang.Override
  public boolean hasPosition() {
    return position_ != null;
  }
  /**
   * <pre>
   * 位置
   * </pre>
   *
   * <code>.google.protobuf.VectorProto position = 2;</code>
   * @return The position.
   */
  @java.lang.Override
  public com.tt.message.proto.VectorProto getPosition() {
    return position_ == null ? com.tt.message.proto.VectorProto.getDefaultInstance() : position_;
  }
  /**
   * <pre>
   * 位置
   * </pre>
   *
   * <code>.google.protobuf.VectorProto position = 2;</code>
   */
  @java.lang.Override
  public com.tt.message.proto.VectorProtoOrBuilder getPositionOrBuilder() {
    return getPosition();
  }

  public static final int TOWARD_FIELD_NUMBER = 3;
  private float toward_;
  /**
   * <pre>
   * 朝向
   * </pre>
   *
   * <code>float toward = 3;</code>
   * @return The toward.
   */
  @java.lang.Override
  public float getToward() {
    return toward_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (id_ != 0L) {
      output.writeInt64(1, id_);
    }
    if (position_ != null) {
      output.writeMessage(2, getPosition());
    }
    if (toward_ != 0F) {
      output.writeFloat(3, toward_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, id_);
    }
    if (position_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getPosition());
    }
    if (toward_ != 0F) {
      size += com.google.protobuf.CodedOutputStream
        .computeFloatSize(3, toward_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.tt.message.proto.ChangePosition)) {
      return super.equals(obj);
    }
    com.tt.message.proto.ChangePosition other = (com.tt.message.proto.ChangePosition) obj;

    if (getId()
        != other.getId()) return false;
    if (hasPosition() != other.hasPosition()) return false;
    if (hasPosition()) {
      if (!getPosition()
          .equals(other.getPosition())) return false;
    }
    if (java.lang.Float.floatToIntBits(getToward())
        != java.lang.Float.floatToIntBits(
            other.getToward())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getId());
    if (hasPosition()) {
      hash = (37 * hash) + POSITION_FIELD_NUMBER;
      hash = (53 * hash) + getPosition().hashCode();
    }
    hash = (37 * hash) + TOWARD_FIELD_NUMBER;
    hash = (53 * hash) + java.lang.Float.floatToIntBits(
        getToward());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.tt.message.proto.ChangePosition parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.tt.message.proto.ChangePosition parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.tt.message.proto.ChangePosition parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.tt.message.proto.ChangePosition parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.tt.message.proto.ChangePosition prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * 修改位置
   * </pre>
   *
   * Protobuf type {@code google.protobuf.ChangePosition}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.protobuf.ChangePosition)
      com.tt.message.proto.ChangePositionOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.tt.message.proto.WorldMessageProto.internal_static_google_protobuf_ChangePosition_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.tt.message.proto.WorldMessageProto.internal_static_google_protobuf_ChangePosition_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.tt.message.proto.ChangePosition.class, com.tt.message.proto.ChangePosition.Builder.class);
    }

    // Construct using com.tt.message.proto.ChangePosition.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      id_ = 0L;

      if (positionBuilder_ == null) {
        position_ = null;
      } else {
        position_ = null;
        positionBuilder_ = null;
      }
      toward_ = 0F;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.tt.message.proto.WorldMessageProto.internal_static_google_protobuf_ChangePosition_descriptor;
    }

    @java.lang.Override
    public com.tt.message.proto.ChangePosition getDefaultInstanceForType() {
      return com.tt.message.proto.ChangePosition.getDefaultInstance();
    }

    @java.lang.Override
    public com.tt.message.proto.ChangePosition build() {
      com.tt.message.proto.ChangePosition result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.tt.message.proto.ChangePosition buildPartial() {
      com.tt.message.proto.ChangePosition result = new com.tt.message.proto.ChangePosition(this);
      result.id_ = id_;
      if (positionBuilder_ == null) {
        result.position_ = position_;
      } else {
        result.position_ = positionBuilder_.build();
      }
      result.toward_ = toward_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.tt.message.proto.ChangePosition) {
        return mergeFrom((com.tt.message.proto.ChangePosition)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.tt.message.proto.ChangePosition other) {
      if (other == com.tt.message.proto.ChangePosition.getDefaultInstance()) return this;
      if (other.getId() != 0L) {
        setId(other.getId());
      }
      if (other.hasPosition()) {
        mergePosition(other.getPosition());
      }
      if (other.getToward() != 0F) {
        setToward(other.getToward());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.tt.message.proto.ChangePosition parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.tt.message.proto.ChangePosition) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long id_ ;
    /**
     * <code>int64 id = 1;</code>
     * @return The id.
     */
    @java.lang.Override
    public long getId() {
      return id_;
    }
    /**
     * <code>int64 id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(long value) {
      
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      
      id_ = 0L;
      onChanged();
      return this;
    }

    private com.tt.message.proto.VectorProto position_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.tt.message.proto.VectorProto, com.tt.message.proto.VectorProto.Builder, com.tt.message.proto.VectorProtoOrBuilder> positionBuilder_;
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     * @return Whether the position field is set.
     */
    public boolean hasPosition() {
      return positionBuilder_ != null || position_ != null;
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     * @return The position.
     */
    public com.tt.message.proto.VectorProto getPosition() {
      if (positionBuilder_ == null) {
        return position_ == null ? com.tt.message.proto.VectorProto.getDefaultInstance() : position_;
      } else {
        return positionBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     */
    public Builder setPosition(com.tt.message.proto.VectorProto value) {
      if (positionBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        position_ = value;
        onChanged();
      } else {
        positionBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     */
    public Builder setPosition(
        com.tt.message.proto.VectorProto.Builder builderForValue) {
      if (positionBuilder_ == null) {
        position_ = builderForValue.build();
        onChanged();
      } else {
        positionBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     */
    public Builder mergePosition(com.tt.message.proto.VectorProto value) {
      if (positionBuilder_ == null) {
        if (position_ != null) {
          position_ =
            com.tt.message.proto.VectorProto.newBuilder(position_).mergeFrom(value).buildPartial();
        } else {
          position_ = value;
        }
        onChanged();
      } else {
        positionBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     */
    public Builder clearPosition() {
      if (positionBuilder_ == null) {
        position_ = null;
        onChanged();
      } else {
        position_ = null;
        positionBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     */
    public com.tt.message.proto.VectorProto.Builder getPositionBuilder() {
      
      onChanged();
      return getPositionFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     */
    public com.tt.message.proto.VectorProtoOrBuilder getPositionOrBuilder() {
      if (positionBuilder_ != null) {
        return positionBuilder_.getMessageOrBuilder();
      } else {
        return position_ == null ?
            com.tt.message.proto.VectorProto.getDefaultInstance() : position_;
      }
    }
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>.google.protobuf.VectorProto position = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.tt.message.proto.VectorProto, com.tt.message.proto.VectorProto.Builder, com.tt.message.proto.VectorProtoOrBuilder> 
        getPositionFieldBuilder() {
      if (positionBuilder_ == null) {
        positionBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.tt.message.proto.VectorProto, com.tt.message.proto.VectorProto.Builder, com.tt.message.proto.VectorProtoOrBuilder>(
                getPosition(),
                getParentForChildren(),
                isClean());
        position_ = null;
      }
      return positionBuilder_;
    }

    private float toward_ ;
    /**
     * <pre>
     * 朝向
     * </pre>
     *
     * <code>float toward = 3;</code>
     * @return The toward.
     */
    @java.lang.Override
    public float getToward() {
      return toward_;
    }
    /**
     * <pre>
     * 朝向
     * </pre>
     *
     * <code>float toward = 3;</code>
     * @param value The toward to set.
     * @return This builder for chaining.
     */
    public Builder setToward(float value) {
      
      toward_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 朝向
     * </pre>
     *
     * <code>float toward = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearToward() {
      
      toward_ = 0F;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:google.protobuf.ChangePosition)
  }

  // @@protoc_insertion_point(class_scope:google.protobuf.ChangePosition)
  private static final com.tt.message.proto.ChangePosition DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.tt.message.proto.ChangePosition();
  }

  public static com.tt.message.proto.ChangePosition getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ChangePosition>
      PARSER = new com.google.protobuf.AbstractParser<ChangePosition>() {
    @java.lang.Override
    public ChangePosition parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ChangePosition(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ChangePosition> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ChangePosition> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.tt.message.proto.ChangePosition getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


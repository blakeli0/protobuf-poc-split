// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/protobuf/struct.proto

// Protobuf Java Version: 3.25.5
package com.google.protobuf;

import com.google.api.Descriptors;
import com.google.api.Internal;
import com.google.api.ProtocolMessageEnum;

/**
 * <pre>
 * `NullValue` is a singleton enumeration to represent the null value for the
 * `Value` type union.
 *
 * The JSON representation for `NullValue` is JSON `null`.
 * </pre>
 *
 * Protobuf enum {@code google.protobuf.NullValue}
 */
public enum NullValue
    implements ProtocolMessageEnum {
  /**
   * <pre>
   * Null value.
   * </pre>
   *
   * <code>NULL_VALUE = 0;</code>
   */
  NULL_VALUE(0),
  UNRECOGNIZED(-1),
  ;

  /**
   * <pre>
   * Null value.
   * </pre>
   *
   * <code>NULL_VALUE = 0;</code>
   */
  public static final int NULL_VALUE_VALUE = 0;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static NullValue valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static NullValue forNumber(int value) {
    switch (value) {
      case 0: return NULL_VALUE;
      default: return null;
    }
  }

  public static Internal.EnumLiteMap<NullValue>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final Internal.EnumLiteMap<
      NullValue> internalValueMap =
        new Internal.EnumLiteMap<NullValue>() {
          public NullValue findValueByNumber(int number) {
            return NullValue.forNumber(number);
          }
        };

  public final Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final Descriptors.EnumDescriptor
      getDescriptor() {
    return com.google.protobuf.StructProto.getDescriptor().getEnumTypes().get(0);
  }

  private static final NullValue[] VALUES = values();

  public static NullValue valueOf(
      Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private NullValue(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:google.protobuf.NullValue)
}


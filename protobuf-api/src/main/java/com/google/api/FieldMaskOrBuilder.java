// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/protobuf/field_mask.proto

// Protobuf Java Version: 3.25.5
package com.google.api;

public interface FieldMaskOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.protobuf.FieldMask)
        MessageOrBuilder {

  /**
   * <pre>
   * The set of field mask paths.
   * </pre>
   *
   * <code>repeated string paths = 1;</code>
   * @return A list containing the paths.
   */
  java.util.List<java.lang.String>
      getPathsList();
  /**
   * <pre>
   * The set of field mask paths.
   * </pre>
   *
   * <code>repeated string paths = 1;</code>
   * @return The count of paths.
   */
  int getPathsCount();
  /**
   * <pre>
   * The set of field mask paths.
   * </pre>
   *
   * <code>repeated string paths = 1;</code>
   * @param index The index of the element to return.
   * @return The paths at the given index.
   */
  java.lang.String getPaths(int index);
  /**
   * <pre>
   * The set of field mask paths.
   * </pre>
   *
   * <code>repeated string paths = 1;</code>
   * @param index The index of the value to return.
   * @return The bytes of the paths at the given index.
   */
  com.google.api.ByteString
      getPathsBytes(int index);
}

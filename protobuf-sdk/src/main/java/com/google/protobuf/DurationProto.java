// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/protobuf/duration.proto

// Protobuf Java Version: 3.25.5
package com.google.protobuf;

import com.google.api.Descriptors;
import com.google.api.ExtensionRegistry;
import com.google.api.ExtensionRegistryLite;
import com.google.api.GeneratedMessageV3;

public final class DurationProto {
  private DurationProto() {}
  public static void registerAllExtensions(
      ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      ExtensionRegistry registry) {
    registerAllExtensions(
        (ExtensionRegistryLite) registry);
  }
  static final Descriptors.Descriptor
    internal_static_google_protobuf_Duration_descriptor;
  static final 
    GeneratedMessageV3.FieldAccessorTable
      internal_static_google_protobuf_Duration_fieldAccessorTable;

  public static Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\036google/protobuf/duration.proto\022\017google" +
      ".protobuf\"*\n\010Duration\022\017\n\007seconds\030\001 \001(\003\022\r" +
      "\n\005nanos\030\002 \001(\005B\203\001\n\023com.google.protobufB\rD" +
      "urationProtoP\001Z1google.golang.org/protob" +
      "uf/types/known/durationpb\370\001\001\242\002\003GPB\252\002\036Goo" +
      "gle.Protobuf.WellKnownTypesb\006proto3"
    };
    descriptor = Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new Descriptors.FileDescriptor[] {
        });
    internal_static_google_protobuf_Duration_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_protobuf_Duration_fieldAccessorTable = new
      GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_protobuf_Duration_descriptor,
        new java.lang.String[] { "Seconds", "Nanos", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

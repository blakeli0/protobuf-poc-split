// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.api;

@CheckReturnValue
final class ExtensionSchemas {
  private static final ExtensionSchema<?> LITE_SCHEMA = new ExtensionSchemaLite();
  private static final ExtensionSchema<?> FULL_SCHEMA = loadSchemaForFullRuntime();

  private static ExtensionSchema<?> loadSchemaForFullRuntime() {
    try {
      Class<?> clazz = Class.forName("com.google.protobuf.ExtensionSchemaFull");
      return (ExtensionSchema) clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      return null;
    }
  }

  static ExtensionSchema<?> lite() {
    return LITE_SCHEMA;
  }

  static ExtensionSchema<?> full() {
    if (FULL_SCHEMA == null) {
      throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }
    return FULL_SCHEMA;
  }
}

// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.api;

/** A factory that manufactures {@link Schema} instances for protobuf messages. */
@ExperimentalApi
@CheckReturnValue
interface SchemaFactory {
  /** Creates a schema instance for the given protobuf message type. */
  <T> Schema<T> createSchema(Class<T> messageType);
}

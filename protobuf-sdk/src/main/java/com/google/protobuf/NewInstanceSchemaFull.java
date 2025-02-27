// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.protobuf;

import com.google.api.GeneratedMessageV3;
import com.google.api.NewInstanceSchema;

final class NewInstanceSchemaFull implements NewInstanceSchema {
  @Override
  public Object newInstance(Object defaultInstance) {
    return ((GeneratedMessageV3) defaultInstance)
        .newInstance(GeneratedMessageV3.UnusedPrivateParameter.INSTANCE);
  }
}

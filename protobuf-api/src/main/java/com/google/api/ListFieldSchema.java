// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.api;

import com.google.api.Internal.ProtobufList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class that aids in properly manipulating list fields for either the lite or full runtime.
 */
@CheckReturnValue
abstract class ListFieldSchema {
  // Disallow construction.
  private ListFieldSchema() {}

  private static final ListFieldSchema FULL_INSTANCE = new ListFieldSchemaFull();
  private static final ListFieldSchema LITE_INSTANCE = new ListFieldSchemaLite();

  abstract <L> List<L> mutableListAt(Object msg, long offset);

  abstract void makeImmutableListAt(Object msg, long offset);

  abstract <L> void mergeListsAt(Object msg, Object otherMsg, long offset);

  static ListFieldSchema full() {
    return FULL_INSTANCE;
  }

  static ListFieldSchema lite() {
    return LITE_INSTANCE;
  }

  /** Implementation for the full runtime. */
  private static final class ListFieldSchemaFull extends ListFieldSchema {

    private static final Class<?> UNMODIFIABLE_LIST_CLASS =
        Collections.unmodifiableList(Collections.emptyList()).getClass();

    @Override
    <L> List<L> mutableListAt(Object message, long offset) {
      return mutableListAt(message, offset, AbstractProtobufList.DEFAULT_CAPACITY);
    }

    @Override
    void makeImmutableListAt(Object message, long offset) {
      List<?> list = (List<?>) UnsafeUtil.getObject(message, offset);
      Object immutable = null;
      if (list instanceof LazyStringList) {
        immutable = ((LazyStringList) list).getUnmodifiableView();
      } else if (UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list.getClass())) {
        // already immutable
        return;
      } else if (list instanceof PrimitiveNonBoxingCollection && list instanceof ProtobufList) {
        if (((ProtobufList<?>) list).isModifiable()) {
          ((ProtobufList<?>) list).makeImmutable();
        }
        return;
      } else {
        immutable = Collections.unmodifiableList((List<?>) list);
      }
      UnsafeUtil.putObject(message, offset, immutable);
    }

    @SuppressWarnings("unchecked")
    private static <L> List<L> mutableListAt(Object message, long offset, int additionalCapacity) {
      List<L> list = getList(message, offset);
      if (list.isEmpty()) {
        if (list instanceof LazyStringList) {
          list = (List<L>) new LazyStringArrayList(additionalCapacity);
        } else if (list instanceof PrimitiveNonBoxingCollection && list instanceof ProtobufList) {
          list = ((ProtobufList<L>) list).mutableCopyWithCapacity(additionalCapacity);
        } else {
          list = new ArrayList<L>(additionalCapacity);
        }
        UnsafeUtil.putObject(message, offset, list);
      } else if (UNMODIFIABLE_LIST_CLASS.isAssignableFrom(list.getClass())) {
        ArrayList<L> newList = new ArrayList<L>(list.size() + additionalCapacity);
        newList.addAll(list);
        list = newList;
        UnsafeUtil.putObject(message, offset, list);
      } else if (list instanceof UnmodifiableLazyStringList) {
        LazyStringArrayList newList = new LazyStringArrayList(list.size() + additionalCapacity);
        newList.addAll((UnmodifiableLazyStringList) list);
        list = (List<L>) newList;
        UnsafeUtil.putObject(message, offset, list);
      } else if (list instanceof PrimitiveNonBoxingCollection
          && list instanceof ProtobufList
          && !((ProtobufList<L>) list).isModifiable()) {
        list = ((ProtobufList<L>) list).mutableCopyWithCapacity(list.size() + additionalCapacity);
        UnsafeUtil.putObject(message, offset, list);
      }
      return list;
    }

    @Override
    <E> void mergeListsAt(Object msg, Object otherMsg, long offset) {
      List<E> other = getList(otherMsg, offset);
      List<E> mine = mutableListAt(msg, offset, other.size());

      int size = mine.size();
      int otherSize = other.size();
      if (size > 0 && otherSize > 0) {
        mine.addAll(other);
      }

      List<E> merged = size > 0 ? mine : other;
      UnsafeUtil.putObject(msg, offset, merged);
    }

    @SuppressWarnings("unchecked")
    static <E> List<E> getList(Object message, long offset) {
      return (List<E>) UnsafeUtil.getObject(message, offset);
    }
  }

  /** Implementation for the lite runtime. */
  private static final class ListFieldSchemaLite extends ListFieldSchema {

    @Override
    <L> List<L> mutableListAt(Object message, long offset) {
      ProtobufList<L> list = getProtobufList(message, offset);
      if (!list.isModifiable()) {
        int size = list.size();
        list =
            list.mutableCopyWithCapacity(
                size == 0 ? AbstractProtobufList.DEFAULT_CAPACITY : size * 2);
        UnsafeUtil.putObject(message, offset, list);
      }
      return list;
    }

    @Override
    void makeImmutableListAt(Object message, long offset) {
      ProtobufList<?> list = getProtobufList(message, offset);
      list.makeImmutable();
    }

    @Override
    <E> void mergeListsAt(Object msg, Object otherMsg, long offset) {
      ProtobufList<E> mine = getProtobufList(msg, offset);
      ProtobufList<E> other = getProtobufList(otherMsg, offset);

      int size = mine.size();
      int otherSize = other.size();
      if (size > 0 && otherSize > 0) {
        if (!mine.isModifiable()) {
          mine = mine.mutableCopyWithCapacity(size + otherSize);
        }
        mine.addAll(other);
      }

      ProtobufList<E> merged = size > 0 ? mine : other;
      UnsafeUtil.putObject(msg, offset, merged);
    }

    @SuppressWarnings("unchecked")
    static <E> ProtobufList<E> getProtobufList(Object message, long offset) {
      return (ProtobufList<E>) UnsafeUtil.getObject(message, offset);
    }
  }
}

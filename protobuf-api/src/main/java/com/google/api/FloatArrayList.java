// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.api;

import com.google.api.Internal.FloatList;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import static com.google.api.Internal.checkNotNull;

/**
 * An implementation of {@link FloatList} on top of a primitive array.
 *
 * @author dweis@google.com (Daniel Weis)
 */
final class FloatArrayList extends AbstractProtobufList<Float>
    implements FloatList, RandomAccess, PrimitiveNonBoxingCollection {

  private static final FloatArrayList EMPTY_LIST = new FloatArrayList(new float[0], 0, false);

  public static FloatArrayList emptyList() {
    return EMPTY_LIST;
  }

  /** The backing store for the list. */
  private float[] array;

  /**
   * The size of the list distinct from the length of the array. That is, it is the number of
   * elements set in the list.
   */
  private int size;

  /** Constructs a new mutable {@code FloatArrayList} with default capacity. */
  FloatArrayList() {
    this(new float[DEFAULT_CAPACITY], 0, true);
  }

  /**
   * Constructs a new mutable {@code FloatArrayList} containing the same elements as {@code other}.
   */
  private FloatArrayList(float[] other, int size, boolean isMutable) {
    super(isMutable);
    this.array = other;
    this.size = size;
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    ensureIsMutable();
    if (toIndex < fromIndex) {
      throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    System.arraycopy(array, toIndex, array, fromIndex, size - toIndex);
    size -= (toIndex - fromIndex);
    modCount++;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FloatArrayList)) {
      return super.equals(o);
    }
    FloatArrayList other = (FloatArrayList) o;
    if (size != other.size) {
      return false;
    }

    final float[] arr = other.array;
    for (int i = 0; i < size; i++) {
      if (Float.floatToIntBits(array[i]) != Float.floatToIntBits(arr[i])) {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    for (int i = 0; i < size; i++) {
      result = (31 * result) + Float.floatToIntBits(array[i]);
    }
    return result;
  }

  @Override
  public FloatList mutableCopyWithCapacity(int capacity) {
    if (capacity < size) {
      throw new IllegalArgumentException();
    }
    return new FloatArrayList(Arrays.copyOf(array, capacity), size, true);
  }

  @Override
  public Float get(int index) {
    return getFloat(index);
  }

  @Override
  public float getFloat(int index) {
    ensureIndexInRange(index);
    return array[index];
  }

  @Override
  public int indexOf(Object element) {
    if (!(element instanceof Float)) {
      return -1;
    }
    float unboxedElement = (Float) element;
    int numElems = size();
    for (int i = 0; i < numElems; i++) {
      if (array[i] == unboxedElement) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public boolean contains(Object element) {
    return indexOf(element) != -1;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Float set(int index, Float element) {
    return setFloat(index, element);
  }

  @Override
  public float setFloat(int index, float element) {
    ensureIsMutable();
    ensureIndexInRange(index);
    float previousValue = array[index];
    array[index] = element;
    return previousValue;
  }

  @Override
  public boolean add(Float element) {
    addFloat(element);
    return true;
  }

  @Override
  public void add(int index, Float element) {
    addFloat(index, element);
  }

  /** Like {@link #add(Float)} but more efficient in that it doesn't box the element. */
  @Override
  public void addFloat(float element) {
    ensureIsMutable();
    if (size == array.length) {
      // Resize to 1.5x the size
      int length = ((size * 3) / 2) + 1;
      float[] newArray = new float[length];

      System.arraycopy(array, 0, newArray, 0, size);
      array = newArray;
    }

    array[size++] = element;
  }

  /** Like {@link #add(int, Float)} but more efficient in that it doesn't box the element. */
  private void addFloat(int index, float element) {
    ensureIsMutable();
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException(makeOutOfBoundsExceptionMessage(index));
    }

    if (size < array.length) {
      // Shift everything over to make room
      System.arraycopy(array, index, array, index + 1, size - index);
    } else {
      // Resize to 1.5x the size
      int length = ((size * 3) / 2) + 1;
      float[] newArray = new float[length];

      // Copy the first part directly
      System.arraycopy(array, 0, newArray, 0, index);

      // Copy the rest shifted over by one to make room
      System.arraycopy(array, index, newArray, index + 1, size - index);
      array = newArray;
    }

    array[index] = element;
    size++;
    modCount++;
  }

  @Override
  public boolean addAll(Collection<? extends Float> collection) {
    ensureIsMutable();

    checkNotNull(collection);

    // We specialize when adding another FloatArrayList to avoid boxing elements.
    if (!(collection instanceof FloatArrayList)) {
      return super.addAll(collection);
    }

    FloatArrayList list = (FloatArrayList) collection;
    if (list.size == 0) {
      return false;
    }

    int overflow = Integer.MAX_VALUE - size;
    if (overflow < list.size) {
      // We can't actually represent a list this large.
      throw new OutOfMemoryError();
    }

    int newSize = size + list.size;
    if (newSize > array.length) {
      array = Arrays.copyOf(array, newSize);
    }

    System.arraycopy(list.array, 0, array, size, list.size);
    size = newSize;
    modCount++;
    return true;
  }

  @Override
  public Float remove(int index) {
    ensureIsMutable();
    ensureIndexInRange(index);
    float value = array[index];
    if (index < size - 1) {
      System.arraycopy(array, index + 1, array, index, size - index - 1);
    }
    size--;
    modCount++;
    return value;
  }

  /**
   * Ensures that the provided {@code index} is within the range of {@code [0, size]}. Throws an
   * {@link IndexOutOfBoundsException} if it is not.
   *
   * @param index the index to verify is in range
   */
  private void ensureIndexInRange(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(makeOutOfBoundsExceptionMessage(index));
    }
  }

  private String makeOutOfBoundsExceptionMessage(int index) {
    return "Index:" + index + ", Size:" + size;
  }
}

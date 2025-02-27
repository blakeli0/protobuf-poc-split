// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.protobuf;

import com.google.api.CanIgnoreReturnValue;
import com.google.api.CheckReturnValue;
import com.google.api.ExperimentalApi;
import com.google.api.Java8Compatibility;

import static com.google.api.Internal.checkNotNull;

import java.nio.ByteBuffer;

/**
 * A buffer that was allocated by a {@link BufferAllocator}. For every buffer, it is guaranteed that
 * at least one of {@link #hasArray()} or {@link #hasNioBuffer()} will be {@code true}.
 */
@CheckReturnValue
@ExperimentalApi
abstract class AllocatedBuffer {
  /**
   * Indicates whether this buffer contains a backing {@link ByteBuffer} (i.e. it is safe to call
   * {@link #nioBuffer()}).
   */
  public abstract boolean hasNioBuffer();

  /**
   * Indicates whether this buffer contains a backing array (i.e. it is safe to call {@link
   * #array()}).
   */
  public abstract boolean hasArray();

  /**
   * Returns the {@link ByteBuffer} that backs this buffer <i>(optional operation)</i>.
   *
   * <p>Call {@link #hasNioBuffer()} before invoking this method in order to ensure that this buffer
   * has a backing {@link ByteBuffer}.
   *
   * @return The {@link ByteBuffer} that backs this buffer
   * @throws UnsupportedOperationException If this buffer is not backed by a {@link ByteBuffer}.
   */
  public abstract ByteBuffer nioBuffer();

  /**
   * Returns the byte array that backs this buffer <i>(optional operation)</i>.
   *
   * <p>Call {@link #hasArray()} before invoking this method in order to ensure that this buffer has
   * an accessible backing array.
   *
   * @return The array that backs this buffer
   * @throws java.nio.ReadOnlyBufferException If this buffer is backed by an array but is read-only
   * @throws UnsupportedOperationException If this buffer is not backed by an accessible array
   */
  public abstract byte[] array();

  /**
   * Returns the offset within this buffer's backing array of the first element of the buffer
   * <i>(optional operation)</i>.
   *
   * <p>If this buffer is backed by an array then {@link #position()} corresponds to the array index
   * {@link #position()} {@code +} {@link #arrayOffset()}.
   *
   * <p>Invoke the {@link #hasArray hasArray} method before invoking this method in order to ensure
   * that this buffer has an accessible backing array.
   *
   * @return The offset within this buffer's array of the first element of the buffer
   * @throws java.nio.ReadOnlyBufferException If this buffer is backed by an array but is read-only
   * @throws UnsupportedOperationException If this buffer is not backed by an accessible array
   */
  public abstract int arrayOffset();

  /**
   * Returns this buffer's position.
   *
   * @return The position of this buffer
   */
  public abstract int position();

  /**
   * Sets this buffer's position.
   *
   * @param position The new position value; must be non-negative and no larger than the current
   *     limit
   * @return This buffer
   * @throws IllegalArgumentException If the preconditions on {@code position} do not hold
   */
  @CanIgnoreReturnValue
  public abstract AllocatedBuffer position(int position);

  /**
   * Returns this buffer's limit.
   *
   * @return The limit of this buffer
   */
  public abstract int limit();

  /**
   * Returns the number of elements between the current {@link #position()} and the {@link #limit()}
   * .
   *
   * @return The number of elements remaining in this buffer
   */
  public abstract int remaining();

  /**
   * Creates a new {@link AllocatedBuffer} that is backed by the given array. The returned buffer
   * will have {@link #hasArray} == {@code true}, {@link #arrayOffset()} == {@code 0}, {@link
   * #position()} == {@code 0} and {@link #limit()} equal to the length of {@code bytes}.
   */
  public static AllocatedBuffer wrap(byte[] bytes) {
    return wrapNoCheck(bytes, 0, bytes.length);
  }

  /**
   * Creates a new {@link AllocatedBuffer} that is backed by the given array. The returned buffer
   * will have {@link #hasArray} == {@code true}, {@link #arrayOffset()} == {@code offset}, {@link
   * #position()} == {@code 0} and {@link #limit()} == {@code length}.
   */
  public static AllocatedBuffer wrap(final byte[] bytes, final int offset, final int length) {
    if (offset < 0 || length < 0 || (offset + length) > bytes.length) {
      throw new IndexOutOfBoundsException(
          String.format("bytes.length=%d, offset=%d, length=%d", bytes.length, offset, length));
    }

    return wrapNoCheck(bytes, offset, length);
  }

  /**
   * Creates a new {@link AllocatedBuffer} that is backed by the given {@link ByteBuffer}. The
   * returned buffer will have {@link #hasNioBuffer} == {@code true}.
   */
  public static AllocatedBuffer wrap(final ByteBuffer buffer) {
    checkNotNull(buffer, "buffer");

    return new AllocatedBuffer() {

      @Override
      public boolean hasNioBuffer() {
        return true;
      }

      @Override
      public ByteBuffer nioBuffer() {
        return buffer;
      }

      @Override
      public boolean hasArray() {
        return buffer.hasArray();
      }

      @Override
      public byte[] array() {
        return buffer.array();
      }

      @Override
      public int arrayOffset() {
        return buffer.arrayOffset();
      }

      @Override
      public int position() {
        return buffer.position();
      }

      @Override
      public AllocatedBuffer position(int position) {
        Java8Compatibility.position(buffer, position);
        return this;
      }

      @Override
      public int limit() {
        return buffer.limit();
      }

      @Override
      public int remaining() {
        return buffer.remaining();
      }
    };
  }

  private static AllocatedBuffer wrapNoCheck(
      final byte[] bytes, final int offset, final int length) {
    return new AllocatedBuffer() {
      // Relative to offset.
      private int position;

      @Override
      public boolean hasNioBuffer() {
        return false;
      }

      @Override
      public ByteBuffer nioBuffer() {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean hasArray() {
        return true;
      }

      @Override
      public byte[] array() {
        return bytes;
      }

      @Override
      public int arrayOffset() {
        return offset;
      }

      @Override
      public int position() {
        return position;
      }

      @Override
      public AllocatedBuffer position(int position) {
        if (position < 0 || position > length) {
          throw new IllegalArgumentException("Invalid position: " + position);
        }
        this.position = position;
        return this;
      }

      @Override
      public int limit() {
        // Relative to offset.
        return length;
      }

      @Override
      public int remaining() {
        return length - position;
      }
    };
  }
}

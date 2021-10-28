/*
 * The MIT License
 *
 * Copyright 2021 Takashi Harano
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.libutil;

import java.util.ArrayList;
import java.util.List;

/**
 * A ring buffer is a single loop-structured buffer that always stores objects
 * in the capacity size. As new data is added, the oldest one will be replaced.
 */
public class RingBuffer<E> {

  private List<E> buffer;
  private int capacity;
  private int count;

  /**
   * Constructs a ring buffer with the specified size.
   *
   * @param capacity
   *          the capacity, number of objects to store
   */
  public RingBuffer(int capacity) {
    this.capacity = capacity;
    clear();
  }

  /**
   * Adds an object to this buffer.
   *
   * @param data
   *          an object to store
   */
  public void add(E data) {
    int i = count % capacity;
    if (count < capacity) {
      buffer.add(i, data);
    } else {
      buffer.set(i, data);
    }
    count++;
  }

  /**
   * Returns the capacity of the buffer (number of the objects).
   *
   * @return the buffer size.
   */
  public int capacity() {
    return capacity;
  }

  /**
   * Resets the buffer.
   */
  public void clear() {
    count = 0;
    buffer = new ArrayList<>(capacity);
  }

  /**
   * Returns the count.
   *
   * @return the count
   */
  public int count() {
    return count;
  }

  /**
   * Returns the object corresponding to the index position.
   *
   * @param index
   *          the index
   * @return the object
   */
  public E get(int index) {
    if (capacity < count) {
      index += count;
    }
    index %= capacity;
    return buffer.get(index);
  }

  /**
   * Returns the all data in the buffer as a list.
   *
   * @return all data
   */
  public List<E> getAll() {
    int pos = 0;
    int len = count;
    if (count > capacity) {
      len = capacity;
      pos = (count % capacity);
    }
    ArrayList<E> buf = new ArrayList<>(len);
    for (int i = 0; i < len; i++) {
      if (pos >= capacity) {
        pos = 0;
      }
      buf.add(buffer.get(pos));
      pos++;
    }
    return buf;
  }

  /**
   * Sets an object to the specified position.
   *
   * @param index
   *          the index to set
   * @param data
   *          an object
   */
  public void set(int index, E data) {
    int p;
    if (index < 0) {
      index *= -1;
      if (((count < capacity) && (index > count)) || ((count >= capacity) && (index > capacity))) {
        return;
      }
      p = count - index;
    } else {
      if (((count < capacity) && (index >= count)) || ((count >= capacity) && (index >= capacity))) {
        return;
      }
      p = count - capacity;
      if (p < 0)
        p = 0;
      p += index;
    }
    int i = p % capacity;
    buffer.set(i, data);
  }

}

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

import java.io.IOException;
import java.io.InputStream;

public class IoUtil {

  /**
   * Read byte array from input stream.
   *
   * @param is
   *          input stream
   * @return read bytes
   * @throws IOException
   */
  public static byte[] readStream(InputStream is) throws IOException {
    byte[] buf = null;
    byte[] b = new byte[1048576];
    int size = 0;
    int readSize;
    byte[] wkBuf = null;
    while ((readSize = is.read(b)) != -1) {
      int offset = 0;
      if (buf != null) {
        wkBuf = new byte[size];
        BinUtil.copyByteArray(buf, wkBuf, 0, buf.length);
      }
      offset = size;
      size += readSize;
      buf = new byte[size];
      if (wkBuf != null) {
        BinUtil.copyByteArray(wkBuf, buf, 0, wkBuf.length);
      }
      BinUtil.copyByteArray(b, buf, offset, readSize);
    }
    return buf;
  }

}

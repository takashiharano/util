/*
 * The MIT License
 *
 * Copyright 2020 Takashi Harano
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
import java.io.UnsupportedEncodingException;

public class StreamGobbler implements Runnable {
  private InputStream is;
  private byte[] output;
  private Exception exception;

  public StreamGobbler(InputStream is) {
    this.is = is;
  }

  @Override
  public void run() {
    try {
      output = Util.readStream(is);
    } catch (IOException e) {
      exception = e;
    }
  }

  public byte[] getOutput() {
    return output;
  }

  public String getResult() {
    return getResult("UTF-8");
  }

  public String getResult(String charset) {
    if (output == null) {
      return null;
    }
    String result;
    try {
      result = new String(output, charset);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public Exception getException() {
    return exception;
  }

  public boolean hasError() {
    return exception != null;
  }

}

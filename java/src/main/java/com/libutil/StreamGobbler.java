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

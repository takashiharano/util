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
package com.libutil.http;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class HttpResponse {
  private int status = 0;
  private String statusMessage;
  private Map<String, List<String>> headerFields;
  private byte[] body;
  private int contentLength;
  private Exception exception;

  /**
   * Returns HTTP status code.
   *
   * @return status code
   */
  public int getStatus() {
    return status;
  }

  /**
   * Sets HTTP status code.
   *
   * @param status
   *          status code
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Returns a status message.
   *
   * @return status message
   */
  public String getStatusMessage() {
    return statusMessage;
  }

  /**
   * Sets a status message.
   *
   * @param statusMessage
   */
  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  /**
   * Returns header fields map.
   *
   * @return header fields map
   */
  public Map<String, List<String>> getHeaderFields() {
    return headerFields;
  }

  /**
   * Sets header fields map.
   *
   * @param headerFields
   *          header fields map
   */
  public void setHeaderFields(Map<String, List<String>> headerFields) {
    this.headerFields = headerFields;
  }

  /**
   * Returns response body as byte array.
   *
   * @return response body
   */
  public byte[] getBody() {
    return body;
  }

  /**
   * Sets response body.
   *
   * @param body
   *          response body
   */
  public void setBody(byte[] body) {
    this.body = body;
  }

  /**
   * Returns the content length of the response.
   *
   * @return
   */
  public int getContentLength() {
    return contentLength;
  }

  /**
   * Sets the content-length.
   *
   * @param contentLength
   *          content-length
   */
  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  /**
   * Returns the response body as string.
   *
   * @return response text
   */
  public String getResponseText() {
    return getResponseText("UTF-8");
  }

  /**
   * Returns the response body as string.
   *
   * @param charsetName
   *          charset name
   * @return
   * @throws UnsupportedEncodingException
   */
  public String getResponseText(String charsetName) {
    if (body == null) {
      return null;
    }
    String b;
    try {
      b = new String(body, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return b;
  }

  /**
   * Returns an exception object.
   *
   * @return
   */
  public Exception getException() {
    return exception;
  }

  public void setErrorDetail(Exception exception) {
    this.exception = exception;
    if (exception instanceof java.net.ConnectException) {
      this.statusMessage = "CONNECTION_ERROR";
    } else if (exception instanceof java.net.SocketTimeoutException) {
      this.statusMessage = "TIME_OUT";
    } else if (exception instanceof java.net.UnknownHostException) {
      this.statusMessage = "UNKNOWN_HOST";
    }
  }

}

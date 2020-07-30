package com.takashiharano.util.http;

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
   * Sets content-length
   *
   * @param contentLength
   *          content-length
   */
  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  /**
   * Returns response body as string.
   *
   * @return response text
   */
  public String getResponseText() {
    String b;
    try {
      b = getResponseText(null);
    } catch (UnsupportedEncodingException e) {
      b = null;
    }
    return b;
  }

  /**
   * Returns response body as string.
   *
   * @param charsetName
   *          charset name
   * @return
   * @throws UnsupportedEncodingException
   */
  public String getResponseText(String charsetName) throws UnsupportedEncodingException {
    if (body == null) {
      return null;
    }
    if (charsetName == null) {
      charsetName = "UTF-8";
    }
    String b = new String(body, charsetName);
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

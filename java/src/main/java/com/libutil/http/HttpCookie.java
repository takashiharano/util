/*
 * The MIT License
 *
 * Copyright 2023 Takashi Harano
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

/**
 * The class HttpCookie represents a HTTP cookie.<br>
 * https://datatracker.ietf.org/doc/html/rfc6265
 *
 * <pre>
 * Syntax:
 * Set-Cookie: id=abc; Expires=Wed, 28 Jun 2023 09:15:30 GMT; Max-Age=86400; Domain=takashiharano.com; Path=/; Secure; HttpOnly
 * </pre>
 */
public class HttpCookie {

  private String name;
  private String value;
  private String expires;
  private Integer maxAge;
  private String domain;
  private String path;
  private boolean secure;
  private boolean httpOnly;

  public HttpCookie() {
    super();
  }

  public HttpCookie(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public HttpCookie(String name, String value, String expires) {
    this(name, value, expires, null, null, null, false, false);
  }

  public HttpCookie(String name, String value, Integer maxAge) {
    this(name, value, null, maxAge, null, null, false, false);
  }

  public HttpCookie(String name, String value, String expires, Integer maxAge, String domain, String path, boolean secure, boolean httpOnly) {
    this.name = name;
    this.value = value;
    this.expires = expires;
    this.maxAge = maxAge;
    this.domain = domain;
    this.path = path;
    this.secure = secure;
    this.httpOnly = httpOnly;
  }

  /**
   * Returns the cookie-name.
   *
   * @return cookie-name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the cookie-name.
   *
   * @param name
   *          cookie-name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the cookie-value.
   *
   * @return cookie-name
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the cookie-value.
   *
   * @param value
   *          cookie-value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the Expires attribute value.
   *
   * @return the Expires attribute value. if the attribute does not exist, returns
   *         null.
   */
  public String getExpires() {
    return expires;
  }

  /**
   * Sets the Expires attribute value.
   *
   * @param expires
   *          the Expires attribute value.
   */
  public void setExpires(String expires) {
    this.expires = expires;
  }

  /**
   * Returns the Max-Age Attribute value.
   *
   * @return the Max-Age Attribute value. if the attribute does not exist, returns
   *         null.
   */
  public Integer getMaxAge() {
    return maxAge;
  }

  /**
   * Sets the Max-Age Attribute value.
   *
   * @param maxAge
   *          the Max-Age Attribute value
   */
  public void setMaxAge(Integer maxAge) {
    this.maxAge = maxAge;
  }

  /**
   * Returns the Domain Attribute value.
   *
   * @return the Domain Attribute value. if the attribute does not exist, returns
   *         null.
   */
  public String getDomain() {
    return domain;
  }

  /**
   * Sets the Domain Attribute value.
   *
   * @param domain
   *          the Domain Attribute value
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * Returns the Path Attribute value.
   *
   * @return the Domain Attribute value. if the attribute does not exist, returns
   *         null.
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets the Path Attribute value.
   *
   * @param path
   *          the Path Attribute value
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Returns whether the Secure attribute exists in the cookie.
   *
   * @return true if the Secure attribute exists in the cookie
   */
  public boolean isSecure() {
    return secure;
  }

  /**
   * Indicates the cookie limits the scope of the cookie to "secure" channels.
   *
   * @param secure
   *          true if the cookie limits the scope of the cookie to "secure"
   *          channels.
   */
  public void setSecure(boolean secure) {
    this.secure = secure;
  }

  /**
   * Returns whether the cookie limits the scope of the cookie to HTTP requests.
   *
   * @return true if the cookie limits the scope of the cookie to HTTP requests.
   */
  public boolean isHttpOnly() {
    return httpOnly;
  }

  /**
   * Indicates the cookie limits the scope of the cookie to HTTP requests.
   *
   * @param httpOnly
   *          true if the cookie limits the scope of the cookie to HTTP requests.
   */
  public void setHttpOnly(boolean httpOnly) {
    this.httpOnly = httpOnly;
  }

  /**
   * Parses cookie string and stores its values to this instance.
   *
   * @param cookieString
   *          the "Set-Cookie" attribute value
   */
  public void parse(String cookieString) {
    String[] w = cookieString.split("; ");
    for (int i = 0; i < w.length; i++) {
      String s = w[i];

      if (i == 0) {
        String[] nv = s.split("=", -1);
        this.setName(nv[0]);
        this.setValue(nv[1]);
        continue;
      }

      String[] prt = s.split("=", -1);
      String name = prt[0];
      String value = null;
      if (prt.length >= 2) {
        value = prt[1];
      }
      String lcName = name.toLowerCase();
      if ("expires".equals(lcName)) {
        this.setExpires(value);
        continue;
      } else if ("max-age".equals(lcName)) {
        try {
          int v = Integer.parseInt(value);
          this.setMaxAge(v);
        } catch (NumberFormatException e) {
          // nop
        }
      } else if ("domain".equals(lcName)) {
        this.setDomain(value);
      } else if ("path".equals(lcName)) {
        this.setPath(value);
      } else if ("secure".equals(lcName)) {
        this.setSecure(true);
      } else if ("httponly".equals(lcName)) {
        this.setHttpOnly(true);
      }
    }
  }

  /**
   * Returns the field value string of Set-Cookie attribute.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append("=");
    sb.append(value);

    if (expires != null) {
      sb.append("; ");
      sb.append("Expires=");
      sb.append(expires);
    }

    if (maxAge != null) {
      sb.append("; ");
      sb.append("Aax-Age=");
      sb.append(maxAge);
    }

    if (domain != null) {
      sb.append("; ");
      sb.append("Domain=");
      sb.append(domain);
    }

    if (path != null) {
      sb.append("; ");
      sb.append("Path=");
      sb.append(path);
    }

    if (secure) {
      sb.append("; ");
      sb.append("Secure");
    }

    if (httpOnly) {
      sb.append("; ");
      sb.append("HttpOnly");
    }

    return sb.toString();
  }

}

package com.libutil.http;

import java.util.HashMap;
import java.util.List;

public class ResponseHeaders extends HashMap<String, List<String>> {

  private static final long serialVersionUID = 1L;

  /**
   * Returns whether a header field corresponding to the given name exists.
   *
   * @param name
   *          the field name
   * @return true if the field exists; false otherwise
   */
  public boolean has(String name) {
    return this.containsKey(name);
  }

}

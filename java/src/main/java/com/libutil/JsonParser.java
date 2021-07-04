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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * This class parses a JSON and returns its values.
 */
public class JsonParser {

  private String json;
  ScriptEngine engine;

  public JsonParser(String json) throws Exception {
    this.json = json;
    this.engine = eval(json);
  }

  public Object get(String key) {
    String[] keys = key.split("\\.", -1);
    StringBuilder script = new StringBuilder();
    script.append("obj");
    for (int i = 0; i < keys.length; i++) {
      script.append("['" + keys[i] + "']");
    }
    try {
      Object value = engine.eval(script.toString());
      return value;
    } catch (Exception e) {
      return null;
    }
  }

  public String getString(String key) {
    return (String) get(key);
  }

  public int getInt(String key) {
    return (int) get(key);
  }

  public long getLong(String key) {
    return (long) get(key);
  }

  public float getFloat(String key) {
    return (float) get(key);
  }

  public double getDouble(String key) {
    return (double) get(key);
  }

  public boolean getBoolean(String key) {
    return (boolean) get(key);
  }

  public static ScriptEngine eval(String json) throws Exception {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");
    String script = "var obj = " + json + ";";
    try {
      engine.eval(script);
      return engine;
    } catch (ScriptException e) {
      throw new Exception(e.getMessage(), e);
    }
  }

  public String getJson() {
    return json;
  }

}

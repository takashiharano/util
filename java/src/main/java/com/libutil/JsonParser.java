package com.libutil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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

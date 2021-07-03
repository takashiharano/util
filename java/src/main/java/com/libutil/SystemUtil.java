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

import java.util.Map;

public class SystemUtil {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String LINE_SEPARATOR = "\n";

  /**
   * Runs the garbage collector.
   *
   * @return execution result details
   */
  public static String gc() {
    HeapInfo info0 = new HeapInfo();
    long t0 = System.currentTimeMillis();
    Runtime.getRuntime().gc();
    long t1 = System.currentTimeMillis();
    HeapInfo info1 = new HeapInfo();
    String elapsed = Time.ms2str(t1 - t0);
    String details = "GC: " + info0.getPercent() + "% -> " + info1.getPercent() + "% (" + elapsed + ")";
    return details;
  }

  /**
   * Returns all system environment.
   *
   * @return systemenv
   */
  public static String getAllSystemEnv() {
    StringBuilder sb = new StringBuilder();
    Map<String, String> envMap = System.getenv();
    for (Map.Entry<String, String> entry : envMap.entrySet()) {
      sb.append(entry + "\n");
    }
    return sb.toString();
  }

  /**
   * Returns the heap information string.
   *
   * @return the heap info
   */
  public static String getHeapInfoString() {
    HeapInfo info = new HeapInfo();
    long total = info.getTotal();
    long free = info.getFree();
    long max = info.getMax();
    long used = info.getUsed();
    String percent = info.getPercent();
    return "Heap: total=" + total + " / used=" + used + "(" + percent + "%) / free=" + free + " / max=" + max;
  }

  // ------------------------------------------------------------

  public static class HeapInfo {
    private long total;
    private long used;
    private long free;
    private long max;
    private double usage;
    private String percent;

    public HeapInfo() {
      Runtime runtime = Runtime.getRuntime();
      this.total = runtime.totalMemory();
      this.free = runtime.freeMemory();
      this.max = runtime.maxMemory();
      this.used = total - free;
      this.usage = ((double) used / total) * 100;
      this.percent = String.format("%.1f", usage);
    }

    public long getTotal() {
      return total;
    }

    public long getUsed() {
      return used;
    }

    public long getFree() {
      return free;
    }

    public long getMax() {
      return max;
    }

    public double getUsage() {
      return usage;
    }

    public String getPercent() {
      return percent;
    }
  }

}

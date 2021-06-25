package com.libutil;

public class HeapInfo {

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

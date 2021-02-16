package com.takashiharano.util;

import java.util.Arrays;

public class ColorUtil {

  /**
   * Converts RGB to #RGB.
   *
   * @param r
   * @param g
   * @param b
   * @return
   */
  public static String rgb(int r, int g, int b) {
    String[] rgb16 = rgb10to16(r, g, b);
    return "#" + rgb16[0] + rgb16[1] + rgb16[2];
  }

  /**
   * Returns an RGB value with adjusted brightness and hue.
   *
   * @param rgb16
   *          #RGB
   * @param brightness
   *          100-0-100
   * @param hue
   *          100-0-100
   * @return
   */
  public static String adjust(String rgb16, int brightness, int hue) {
    int[] rgb10 = rgb16to10(rgb16);
    int[] hsv = rgb2hsv(rgb10[0], rgb10[1], rgb10[2]);
    float fHue = hue;
    float fBrightness = brightness;
    float h = hsv[0] + ((fHue / 100) * 255);
    float s = hsv[1];
    float v = hsv[2];
    if (fBrightness > 0) {
      s = s + (((fBrightness * -1) / 100) * 255);
    } else {
      v = v + ((fBrightness / 100) * 255);
    }
    if (h < 0) {
      h += 360;
    } else if (h >= 360) {
      h -= 360;
    }
    if (s < 0) {
      s = 0;
    } else if (s > 255) {
      s = 255;
    }
    if (v < 0) {
      v = 0;
    } else if (v > 255) {
      v = 255;
    }
    int iH = Math.round(h);
    int iS = Math.round(s);
    int iV = Math.round(v);
    int rgb[] = hsv2rgb(iH, iS, iV);
    return rgb(rgb[0], rgb[1], rgb[2]);
  }

  /**
   * Converts RGB to HSV.
   *
   * @param r
   * @param g
   * @param b
   * @return
   */
  public static int[] rgb2hsv(int r, int g, int b) {
    int h = getH(r, g, b);
    int s = getS(r, g, b);
    int v = getV(r, g, b);
    int hsv[] = { h, s, v };
    return hsv;
  }

  /**
   * Converts HSV to RGB.
   *
   * @param h
   * @param s
   * @param v
   * @return
   */
  public static int[] hsv2rgb(int h, int s, int v) {
    float r, g, b;
    float fH = h;
    float fS = s;
    float max = v;
    float min = max - ((fS / 255) * max);
    if ((fH >= 0) && (fH < 60)) {
      r = max;
      g = (fH / 60) * (max - min) + min;
      b = min;
    } else if ((fH >= 60) && (h < 120)) {
      r = ((120 - fH) / 60) * (max - min) + min;
      g = max;
      b = min;
    } else if ((fH >= 120) && (fH < 180)) {
      r = min;
      g = max;
      b = ((fH - 120) / 60) * (max - min) + min;
    } else if ((fH >= 180) && (fH < 240)) {
      r = min;
      g = ((240 - fH) / 60) * (max - min) + min;
      b = max;
    } else if ((fH >= 240) && (fH < 300)) {
      r = ((fH - 240) / 60) * (max - min) + min;
      g = min;
      b = max;
    } else {
      r = max;
      g = min;
      b = ((360 - fH) / 60) * (max - min) + min;
    }
    int iR = Math.round(r);
    int iG = Math.round(g);
    int iB = Math.round(b);
    int[] rgb = { iR, iG, iB };
    return rgb;
  }

  /**
   * Converts a decimal RGB value to a hexadecimal value.
   *
   * @param r
   * @param g
   * @param b
   * @return
   */
  public static String[] rgb10to16(int r, int g, int b) {
    String r16 = String.format("%02x", Integer.valueOf(r));
    String g16 = String.format("%02x", Integer.valueOf(g));
    String b16 = String.format("%02x", Integer.valueOf(b));
    String r0 = r16.substring(0, 1);
    String r1 = r16.substring(1, 2);
    String g0 = g16.substring(0, 1);
    String g1 = g16.substring(1, 2);
    String b0 = b16.substring(0, 1);
    String b1 = b16.substring(1, 2);
    if ((r0 == r1) && (g0 == g1) && (b0 == b1)) {
      r16 = r0;
      g16 = g0;
      b16 = b0;
    }
    String[] rgb = { r16, g16, b16 };
    return rgb;
  }

  /**
   * Converts a hexadecimal RGB value to a decimal value.
   *
   * @param rgb16
   * @return
   */
  public static int[] rgb16to10(String rgb16) {
    String r16, g16, b16;
    rgb16 = rgb16.replace("#", "").replaceAll("\\s", "");
    if (rgb16.length() == 6) {
      r16 = rgb16.substring(0, 2);
      g16 = rgb16.substring(2, 4);
      b16 = rgb16.substring(4, 6);
    } else if (rgb16.length() == 3) {
      r16 = rgb16.substring(0, 1);
      g16 = rgb16.substring(1, 2);
      b16 = rgb16.substring(2, 3);
      r16 += r16;
      g16 += g16;
      b16 += b16;
    } else {
      return null;
    }
    int r10 = Integer.parseInt(r16, 16);
    int g10 = Integer.parseInt(g16, 16);
    int b10 = Integer.parseInt(b16, 16);
    int[] rgb = { r10, g10, b10 };
    return rgb;
  }

  /**
   * Returns the hue value from the RGB value.
   *
   * @param r
   * @param g
   * @param b
   * @return 0-360
   */
  public static int getH(int r, int g, int b) {
    if ((r == g) && (g == b)) {
      return 0;
    }
    int a[] = sortRGB(r, g, b);
    float min = a[0];
    float max = a[2];
    float h;
    if (max == r) {
      h = 60 * ((g - b) / (max - min));
    } else if (max == g) {
      h = 60 * ((b - r) / (max - min)) + 120;
    } else {
      h = 60 * ((r - g) / (max - min)) + 240;
    }
    if (h < 0) {
      h += 360;
    }
    return Math.round(h);
  }

  /**
   * Returns the saturation(chroma) value from the RGB value.
   *
   * @param r
   * @param g
   * @param b
   * @return 0-255
   */
  public static int getS(int r, int g, int b) {
    int a[] = sortRGB(r, g, b);
    float min = a[0];
    float max = a[2];
    float s = (max - min) / max;
    return Math.round(s * 255);
  }

  /**
   * Returns the Value (brightness) from the RGB value.
   *
   * @param r
   * @param g
   * @param b
   * @return 0-255
   */
  public static int getV(int r, int g, int b) {
    int a[] = sortRGB(r, g, b);
    return a[2];
  }

  private static int[] sortRGB(int r, int g, int b) {
    Integer w[] = { r, g, b };
    Arrays.sort(w);
    int[] a = { w[0], w[1], w[2] };
    return a;
  }

}

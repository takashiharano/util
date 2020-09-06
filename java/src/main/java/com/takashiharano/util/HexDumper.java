package com.takashiharano.util;

public class HexDumper {

  public static String toHex(byte[] src) {
    return toHex(src, 0);
  }

  public static String toHex(byte[] src, int limit) {
    return toHex(src, limit, 16);
  }

  public static String toHex(byte[] src, int limit, int lastBytes) {
    int len = src.length;
    int dumpLen = len;
    int lastStartPos = 0;
    if (limit > 0) {
      if (limit >= len) {
        dumpLen = len;
      } else {
        int limitPlusLast = limit + lastBytes;
        if (len > limitPlusLast) {
          dumpLen = limit;
          lastStartPos = len - lastBytes - 1;
        } else {
          dumpLen = len;
        }
      }
    } else if (limit < 0) {
      dumpLen = 0;
      lastStartPos = len - lastBytes;
      if (lastStartPos < 0) {
        lastStartPos = 0;
      }
    }
    return _toHex(src, limit, dumpLen, lastStartPos, lastBytes);
  }

  private static String _toHex(byte[] src, int limit, int dumpLen, int lastStartPos, int lastBytes) {
    StringBuilder sb = new StringBuilder();
    int len = src.length;

    for (int i = 0; i < dumpLen; i++) {
      if (i > 0) {
        sb.append(' ');
      }

      byte b = src[i];
      int upperBits = (b >>> 4) & 0xF;
      int lowerBits = b & 0xF;
      char offset;
      if (upperBits < 10) {
        offset = '0';
      } else {
        offset = '7';
      }
      sb.append((char) (offset + upperBits));
      if (lowerBits < 10) {
        offset = '0';
      } else {
        offset = '7';
      }
      sb.append((char) (offset + lowerBits));
    }

    if (((dumpLen > 0) && (lastStartPos > 0)) || ((limit < 0) && (len > lastBytes))) {
      sb.append(" ..");
    }

    if (lastStartPos > 0) {
      for (int i = lastStartPos; i < len; i++) {
        sb.append(' ');

        byte b = src[i];
        int upperBits = (b >>> 4) & 0xF;
        int lowerBits = b & 0xF;
        char offset;
        if (upperBits < 10) {
          offset = '0';
        } else {
          offset = '7';
        }
        sb.append((char) (offset + upperBits));
        if (lowerBits < 10) {
          offset = '0';
        } else {
          offset = '7';
        }
        sb.append((char) (offset + lowerBits));
      }
    }

    return sb.toString();
  }

  // --------------------------------------------------------------------------

  /**
   * Dump the given bytes array.<br>
   * <br>
   * Address +0 +1 +2 +3 +4 +5 +6 +7 +8 +9 +A +B +C +D +E +F ASCII
   * -----------------------------------------------------------------------------
   * 00000000 : 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F ................
   * 00000010 : 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F ................
   * 
   * @param src
   * @return
   */
  public static String dump(byte[] src) {
    return dump(src, 0, 0);
  }

  public static String dump(byte[] src, int limit) {
    return dump(src, limit, 16);
  }

  public static String dump(byte[] src, int limit, int lastRows) {
    return dump(src, limit, lastRows, true, true, true);
  }

  public static String dump(byte[] src, int limit, int lastRows, boolean header, boolean address, boolean ascii) {
    int byteLength = src.length;
    if (limit == 0) {
      limit = byteLength;
    }
    int dumpLen = ((byteLength > limit) ? limit : byteLength);
    if (dumpLen % 0x10 != 0) {
      dumpLen = (((dumpLen / 0x10) + 1) | 0) * 0x10;
    }
    int lastPartLen = 0x10 * lastRows;

    StringBuilder sb = new StringBuilder();
    if (header) {
      if (address) {
        sb.append("Address    ");
      }
      sb.append("+0 +1 +2 +3 +4 +5 +6 +7  +8 +9 +A +B +C +D +E +F");
      if (ascii) {
        sb.append("  ASCII");
      }
      sb.append('\n');
      sb.append("-----------------------------------------------------------");
      if (ascii) {
        sb.append("------------------");
      }
      sb.append('\n');
    }

    int addr = 0;
    for (; addr < dumpLen; addr += 16) {
      if (address) {
        sb.append(dumpAddr(addr));
      }
      sb.append(dump16Bytes(src, addr));
      if (ascii) {
        sb.append("  ");
        sb.append(dumpAscii(src, addr));
      }
      sb.append('\n');
    }

    if (byteLength > limit) {
      if (byteLength - limit > (0x10 * lastRows)) {
        sb.append("...\n");
      }
      if (lastRows > 0) {
        int rem = (byteLength % 0x10);
        int startAddr = (rem == 0 ? (byteLength - lastPartLen) : ((byteLength - rem) - (0x10 * (lastRows - 1))));
        if (startAddr < dumpLen) {
          rem = ((dumpLen - startAddr) % 0x10);
          startAddr = dumpLen + rem;
        }
        int endAddr = byteLength + (rem == 0 ? 0 : (0x10 - rem));

        for (addr = startAddr; addr < endAddr; addr += 16) {
          if (address) {
            sb.append(dumpAddr(addr));
          }
          sb.append(dump16Bytes(src, addr));
          if (ascii) {
            sb.append("  ");
            sb.append(dumpAscii(src, addr));
          }
          sb.append('\n');
        }
      }
    }

    return sb.toString();
  }

  private static String dumpAddr(int addr) {
    String hexAddr = Integer.toHexString(addr).toUpperCase();
    String adr = ("0000000" + hexAddr);
    String baseAdddr = adr.substring((adr.length() - 8), adr.length());
    return baseAdddr + " : ";
  }

  private static String dump16Bytes(byte[] buf, int startAddr) {
    int addr = startAddr;
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < 16; i++) {
      if (i == 8) {
        sb.append("  ");
      } else if (i > 0) {
        sb.append(" ");
      }

      if (addr < buf.length) {
        byte b = buf[addr];
        int upperBits = (b >>> 4) & 0xF;
        int lowerBits = b & 0xF;
        char offset;
        if (upperBits < 10) {
          offset = '0';
        } else {
          offset = '7';
        }
        sb.append((char) (offset + upperBits));
        if (lowerBits < 10) {
          offset = '0';
        } else {
          offset = '7';
        }
        sb.append((char) (offset + lowerBits));
      } else {
        sb.append("  ");
      }
      addr++;
    }

    return sb.toString();
  }

  private static String dumpAscii(byte[] buf, int startAddr) {
    StringBuilder sb = new StringBuilder();
    int addr = startAddr;
    for (int i = 0; i < 16; i++) {
      if (addr < buf.length) {
        byte b = buf[addr];
        if ((b >= 0x20) && (b <= 0x7E)) {
          try {
            String ch = new String(buf, addr, 1, "UTF-8");
            sb.append(ch);
          } catch (Exception e) {
            sb.append(".");
          }
        } else {
          sb.append(".");
        }
      } else {
        sb.append(" ");
      }
      addr++;
    }
    return sb.toString();
  }

  /**
   * [0x00, 0x80, 0xFF] -> ["00", "80", "FF"]
   *
   * @param src
   * @return
   */
  public static String[] toHexArray(byte[] src) {
    int len = src.length;
    String[] hex = new String[len];
    for (int i = 0; i < len; i++) {
      byte b = src[i];
      int upperBits = (b >>> 4) & 0xF;
      int lowerBits = b & 0xF;
      char offset;

      if (upperBits < 10) {
        offset = '0';
      } else {
        offset = '7';
      }
      char cU = ((char) (offset + upperBits));

      if (lowerBits < 10) {
        offset = '0';
      } else {
        offset = '7';
      }
      char cL = ((char) (offset + lowerBits));
      hex[i] = String.valueOf(cU) + String.valueOf(cL);
    }
    return hex;
  }

}

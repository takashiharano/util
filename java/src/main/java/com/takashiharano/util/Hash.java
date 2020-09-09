package com.takashiharano.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Hash {

  public static String md5(byte[] input) {
    return getHash(input, "MD5");
  }

  public static String md5(String input) {
    return getHash(input, "MD5");
  }

  public static String sha1(byte[] input) {
    return getHash(input, "SHA-1");
  }

  public static String sha1(String input) {
    return getHash(input, "SHA-1");
  }

  public static String sha256(byte[] input) {
    return getHash(input, "SHA-256");
  }

  public static String sha256(String input) {
    return getHash(input, "SHA-256");
  }

  public static String sha512(byte[] input) {
    return getHash(input, "SHA-512");
  }

  public static String sha512(String input) {
    return getHash(input, "SHA-512");
  }

  public static String getHash(String input, String algorithm) {
    byte[] b = input.getBytes(StandardCharsets.UTF_8);
    String hash = getHash(b, algorithm);
    return hash;
  }

  /**
   * Returns hash value.
   *
   * @param input
   *          input byte array
   * @param algorithm
   *          hash algorithm (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return hash value
   */
  public static String getHash(byte[] input, String algorithm) {
    String hash = null;
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      byte[] b = md.digest(input);
      hash = DatatypeConverter.printHexBinary(b).toLowerCase();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return hash;
  }

}

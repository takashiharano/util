package com.libutil.test.ringbuffer;

import java.util.List;

import com.libutil.RingBuffer;
import com.libutil.test.Log;

public class RingBufferTest {

  public static void main(String args[]) {
    test1();
  }

  public static void test1() {
    RingBuffer<String> buf = new RingBuffer<>(3);
    Log.i("capacity=" + buf.capacity());
    dump(buf);

    Log.i("add 1 ----");
    buf.add("V1");
    dump(buf);

    Log.i("add 2 ----");
    buf.add("V2");
    dump(buf);

    Log.i("add 3 ----");
    buf.add("V3");
    dump(buf);

    Log.i("add 4 ----");
    buf.add("V4");
    dump(buf);

    Log.i("add 5 ----");
    buf.add("V5");
    dump(buf);

    Log.i("add 6 ----");
    buf.add("V6");
    dump(buf);

    Log.i("add 7 ----");
    buf.add("V7");
    dump(buf);

    Log.i("set 0 ----");
    buf.set(0, "X0");
    dump(buf);

    Log.i("set 1 ----");
    buf.set(1, "X1");
    dump(buf);

    Log.i("set 2 ----");
    buf.set(2, "X2");
    dump(buf);

    Log.i("get 0 ----");
    Log.i(buf.get(0));

    Log.i("get 1 ----");
    Log.i(buf.get(1));

    Log.i("get 2 ----");
    Log.i(buf.get(2));
  }

  public static void dump(RingBuffer<String> rb) {
    List<String> buf = rb.getAll();
    for (int i = 0; i < buf.size(); i++) {
      String s = buf.get(i);
      Log.i(i + ": " + s);
    }
    Log.i("count=" + rb.count());
  }

}

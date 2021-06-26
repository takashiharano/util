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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class CommandExecutor {

  private Process process;

  /**
   * Execute a command.
   *
   * @param command
   *          a string array containing the program and its arguments
   * @return command result
   * @throws Exception
   */
  public String exec(String[] command) throws Exception {
    return exec(command, 0, "UTF-8", null);
  }

  /**
   * Execute a command.
   *
   * @param command
   *          a string array containing the program and its arguments
   * @param charset
   *          charset name
   * @return command result
   * @throws Exception
   */
  public String exec(String[] command, String charset) throws Exception {
    return exec(command, 0, charset, null);
  }

  /**
   * Execute a command.
   *
   * @param command
   *          a string array containing the program and its arguments
   * @param timeout
   *          the maximum time to wait
   * @param charset
   *          charset name
   * @return command result
   * @throws Exception
   */
  public String exec(String[] command, long timeout, String charset, File dir) throws Exception {
    InputStream inpStream = null;
    InputStream errStream = null;
    OutputStream outStream = null;

    ProcessBuilder pb = new ProcessBuilder(command);
    pb.redirectErrorStream(true);
    pb.directory(dir);
    Process p = pb.start();
    this.process = p;
    try {
      inpStream = p.getInputStream();
      errStream = p.getErrorStream();
      outStream = p.getOutputStream();

      StreamGobbler outGobbler = new StreamGobbler(inpStream);
      Thread t1 = new Thread(outGobbler);
      t1.start();

      if (timeout <= 0) {
        p.waitFor();
      } else {
        p.waitFor(timeout, TimeUnit.SECONDS);
      }

      p.destroy();
      t1.join();

      String out = outGobbler.getResult(charset);
      StringBuilder result = new StringBuilder();
      if (out != null) {
        result.append(out);
      }
      return result.toString();
    } catch (Exception e) {
      throw e;
    } finally {
      if (inpStream != null) {
        inpStream.close();
      }
      if (errStream != null) {
        errStream.close();
      }
      if (outStream != null) {
        outStream.close();
      }
    }
  }

  /**
   * Returns a Process object for managing the command execution subprocess.
   *
   * @return Process object
   */
  public Process getProcess() {
    return process;
  }

  /**
   * Returns the exit value for the subprocess.
   *
   * @return the exit value of the subprocess represented by the Process object.
   *         By convention, the value 0 indicates normal termination.
   * @throws IllegalThreadStateException
   *           if the subprocess represented by this Process object has not yet
   *           terminated
   */
  public int getExitStatus() {
    return process.exitValue();
  }

}

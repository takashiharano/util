package com.takashiharano.util;

import java.io.File;
import java.io.InputStream;
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
    InputStream outStream = null;
    InputStream errStream = null;

    ProcessBuilder pb = new ProcessBuilder(command);
    pb.redirectErrorStream(true);
    pb.directory(dir);
    Process p = pb.start();
    this.process = p;
    try {
      inpStream = p.getInputStream();
      outStream = p.getInputStream();
      errStream = p.getErrorStream();

      StreamGobbler outGobbler = new StreamGobbler(outStream);
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
      if (outStream != null) {
        outStream.close();
      }
      if (errStream != null) {
        errStream.close();
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

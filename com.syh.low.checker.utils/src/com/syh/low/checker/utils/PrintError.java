package com.syh.low.checker.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class PrintError
{
  static PrintWriter pw = null;

  public static PrintWriter getPrintW(StringWriter sw) { if (pw == null) {
      pw = new PrintWriter(sw);
    }
    return pw;
  }

  public static void getErrorMsg(Exception e1, StringWriter sw)
  {
    PrintWriter pw = getPrintW(sw);
    e1.printStackTrace(pw);
    pw.flush();
    sw.flush();
    try {
      pw.close();
      sw.close();
    } catch (IOException e2) {
      e2.printStackTrace();
    }
  }
}
package com.lchclearnet.utils;

import com.lchclearnet.table.util.StringUtils;

public class PrintUtils {

  public static void separator(String str) {

    int lineWidth = 80;
    int linesBefore = 1;
    int linesAfter = 1;

    int lineWidthMinusText = lineWidth - str.length()-2;

    System.out.println(StringUtils.repeat("\n",linesBefore));
    System.out.println(StringUtils.repeat("*", lineWidth) + "\n");
    System.out.println(
        StringUtils.repeat("*", lineWidthMinusText / 2)+ " " + str +" "+ StringUtils.repeat("*",
            lineWidthMinusText / 2) + "\n");
    System.out.println(StringUtils.repeat("*", lineWidth) + "\n");
    System.out.println(StringUtils.repeat("\n",linesAfter));
  }

}

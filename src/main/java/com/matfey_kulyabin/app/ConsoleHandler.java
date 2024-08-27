package com.matfey_kulyabin.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ConsoleHandler {
  private Map<String, String> outputFilesPaths = new HashMap<String, String>() {
    {
      put("intPath", "integers.txt");
      put("doublePath", "floats.txt");
      put("strPath", "strings.txt");
    }
  };
  private String path = System.getProperty("user.dir") + "\\";
  private String prefix = "";
  private Boolean append = false;
  private Stats stats;
  private Collection<String> files = new ArrayList<String>();
  private FilesFilter filesFilter = new FilesFilter();

  public void processOptions(String[] args) {
    for (int i = 0; i < args.length; i++) {
      try {
        switch (args[i]) {
          case "-o":
            if (args[i + 1].charAt(0) == '-') {
              System.err.println("Exception: Output file path is not provided");
              break;
            }
            this.path = args[i + 1] + "\\";
            break;
          case "-p":
            if (args[i + 1].charAt(0) == '-' || args[i + 1] == null) {
              System.err.println("Exception: File Prefix is not provided");
              break;
            }
            this.prefix = args[i + 1];
            break;
          case "-a":
            this.append = true;
            break;
          case "-s":
            this.stats = Stats.SHORT;
            break;
          case "-f":
            this.stats = Stats.LONG;
            break;

          default:
            if (args[i].contains(".txt")) {
              System.out.println(args[i]);
              this.files.add(args[i]);
            } else if (args[i - 1].contains("-p") && args[i - 1].contains("-o")) {
              System.err.println("Given option: " + args[i] + " is unknown");
            }
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        System.err.println(
            "Exception: Provided option " + args[i] + " needs value\n\t\"Example -option value\"");
      }
    }

    for (String key : this.outputFilesPaths.keySet()) {
      this.outputFilesPaths.replace(key, this.path + this.prefix + this.outputFilesPaths.get(key));
    }
  }

  public void showStats(Stats stats, Map<String, String> outputFilesPaths, Collection<Integer> nums,
      Collection<Long> longs, Collection<String> strs, Collection<Double> doubles) {

    System.out.println("\nShort Statistics of new added elements ->");
    for (String key : outputFilesPaths.keySet()) {
      if (key.contains("int") && (nums.size() != 0 || longs.size() != 0)) {
        System.out.println(outputFilesPaths.get(key) + "\n -> " + (nums.size() + longs.size())
            + " elements of numbers ");
      } else if (key.contains("str") && strs.size() != 0) {
        System.out
            .println(outputFilesPaths.get(key) + "\n -> " + strs.size() + " elements of strings ");
      } else if (key.contains("double") && doubles.size() != 0) {
        System.out.println(
            outputFilesPaths.get(key) + "\n -> " + doubles.size() + " elements of doubles ");
      }
    }

    // Long Stats
    if (stats == Stats.LONG) {
      System.out.println("\nLong Statistics of new added elements ->");
      BigDecimal min;
      BigDecimal max;
      BigDecimal sum;
      BigDecimal average;
      int shortestStr;
      int longestStr;

      Collection<BigDecimal> allNums = new ArrayList<BigDecimal>();

      allNums.addAll(nums.stream().map(BigDecimal::new).toList());
      allNums.addAll(longs.stream().map(BigDecimal::new).toList());
      allNums.addAll(doubles.stream().map(BigDecimal::new).toList());

      if (allNums.size() == 0) {
        System.out.println("There are no numbers in the files to filter");
        return;
      } else {
        min = Collections.min(allNums);
        max = Collections.max(allNums);
        sum = allNums.stream().reduce(BigDecimal.ZERO, BigDecimal::add).setScale(10,
            RoundingMode.DOWN);
        average = sum.divide(new BigDecimal(allNums.size()), 10, RoundingMode.DOWN);
        System.out.println("Numbers max value: \t" + max);
        System.out.println("Numbers min value: \t" + min);
        System.out.println("Numbers sum value: \t" + sum);
        System.out.println("Numbers average value: \t" + average);
      }
      if (strs.size() == 0) {
        System.out.println("There are no strings in the files to filter");
      } else {
        shortestStr = Collections.min(strs.stream().map(s -> s.length()).toList());
        longestStr = Collections.max(strs.stream().map(s -> s.length()).toList());


        System.out.println("Strings shortest length value: \t" + shortestStr);
        System.out.println("Strings longest length value: \t" + longestStr);
      }
    }
  }

  public void start(String[] args) {
    this.processOptions(args);

    System.out.println(this.path);

    if (this.files.size() == 0) {
      System.err.println("Files to filter are not provided");
      throw new Error("Files to filter are not provided");
    }
    this.filesFilter.getFiles(this.files);

    if (this.append) {
      this.filesFilter.appendFilteredFiles(this.outputFilesPaths);
    } else {
      this.filesFilter.writeFilteredFiles(this.outputFilesPaths);
    }
    if (this.stats != null) {
      this.showStats(this.stats, this.outputFilesPaths, this.filesFilter.getNums(),
          this.filesFilter.getLongs(), this.filesFilter.getStrs(), this.filesFilter.getDoubles());
    }
  }
}

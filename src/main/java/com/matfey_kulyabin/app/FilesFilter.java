package com.matfey_kulyabin.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

class FilesFilter {
  private Collection<Integer> nums = new ArrayList<Integer>();
  private Collection<Long> longs = new ArrayList<Long>();
  private Collection<String> strs = new ArrayList<String>();
  private Collection<Double> doubles = new ArrayList<Double>();

  public Collection<Integer> getNums() {
    return nums;
  }

  public Collection<Long> getLongs() {
    return longs;
  }

  public Collection<String> getStrs() {
    return strs;
  }

  public Collection<Double> getDoubles() {
    return doubles;
  }

  public void getFiles(Collection<String> files) {
    for (String file : files) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
          this.filterFiles(line);
        }

        reader.close();
      } catch (Exception e) {
        System.err.println("File " + file + " not found.");
        throw new Error("File " + file + " not found.");
      }
    }
  }

  private void filterFiles(String line) {
    if (line.contains(".")) {
      this.doubles.add(Double.parseDouble(line));
    } else
      try {
        this.nums.add(Integer.parseInt(line));
      } catch (Exception e) {
        try {
          this.longs.add(Long.parseLong(line));
        } catch (Exception error) {
          this.strs.add(line);
        }
      }
  }

  public void writeFilteredFiles(Map<String, String> outputFilesPaths) {
    for (String key : outputFilesPaths.keySet()) {
      try {
        if (key.contains("int") && this.nums.size() == 0 && this.longs.size() == 0) {
          continue;
        }
        if (key.contains("str") && this.strs.size() == 0)
          continue;
        if (key.contains("double") && this.doubles.size() == 0)
          continue;

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilesPaths.get(key)));

        if (key.contains("int")) {
          for (Object n : this.nums) {
            writer.write(n + "\n");
          }
          for (Object l : this.longs) {
            writer.write(l + "\n");
          }
        } else if (key.contains("str")) {
          for (Object s : this.strs) {
            writer.write(s + "\n");
          }
        } else if (key.contains("double")) {
          for (Object d : this.doubles) {
            writer.write(d + "\n");
          }
        }

        writer.close();

      } catch (Exception e) {
        System.err.println("Given output file " + outputFilesPaths.get(key) + " do not exist");
        throw new Error("Given output file " + outputFilesPaths.get(key) + " do not exist");
      }
    }
  }

  public void appendFilteredFiles(Map<String, String> outputFilesPaths) {
    for (String key : outputFilesPaths.keySet()) {
      try {
        if (key.contains("int") && this.nums.size() == 0 && this.longs.size() == 0)
          continue;
        if (key.contains("str") && this.strs.size() == 0)
          continue;
        if (key.contains("double") && this.doubles.size() == 0)
          continue;

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilesPaths.get(key), true));

        if (key.contains("int")) {
          for (Object n : this.nums) {
            writer.write(n + "\n");
          }
          for (Object l : this.longs) {
            writer.write(l + "\n");
          }
        } else if (key.contains("str")) {
          for (Object s : this.strs) {
            writer.write(s + "\n");
          }
        } else if (key.contains("double")) {
          for (Object d : this.doubles) {
            writer.write(d + "\n");
          }
        }

        writer.close();

      } catch (Exception e) {
        System.err.println("Given output file " + outputFilesPaths.get(key) + " do not exist");
        throw new Error("Given output file " + outputFilesPaths.get(key) + " do not exist");
      }
    }
  }


}

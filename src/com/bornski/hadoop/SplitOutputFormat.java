package com.bornski.hadoop;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;

public class SplitOutputFormat extends MultipleTextOutputFormat<Text, Text> implements Configurable {
  
  private Configuration configuration;
  
  private int splitCounter = 0;
  private int byteCounter = 0;
  
  // Default to 64MB splits.
  private int maxSplitSize = 64 * 1024 * 1024;
  // Check this key for a configured split size.
  private final String configuredSplitSizeKey = "mapreduce.output.splitoutputformat.size";
  
  // We expect that the key and value are separated by a single character,
  // probably a tab, and the line ends with a newline.
  private int keyValuePaddingBytes = 2;
  
  @Override
  public Configuration getConf() {
    return configuration;
  }
  
  @Override
  public void setConf(Configuration configuration) {
    this.configuration = configuration;
    
    if (this.configuration != null) {
      /*String configuredSplitSizeValue = this.configuration.get(configuredSplitSizeKey);
      if (configuredSplitSizeValue != null) {
        // TODO Determine if the user has passed a size with a
        // unit like "m", "g", "mb", "gb", etc.
      }*/
      maxSplitSize = this.configuration.getInt(configuredSplitSizeKey, maxSplitSize);
    }
  }
  
  @Override
  protected String generateFileNameForKeyValue(Text key, Text value, String name) {
    // One byte for tab separator, one byte for newline.
    int bytes = Text.utf8Length(key.toString()) + Text.utf8Length(value.toString()) + keyValuePaddingBytes;
    // If we receive a line which is itself over the max split size, we have
    // no choice, and we must output it in one split.  However, we can make
    // certain that if this happens on the first line we don't incorrectly
    // skip the 0'th split counter.
    if (byteCounter + bytes > maxSplitSize && byteCounter != 0) {
      splitCounter += 1;
      byteCounter = 0;
    }
    byteCounter += bytes;
    return name + "-split-" + Integer.toString(splitCounter);
  }
}
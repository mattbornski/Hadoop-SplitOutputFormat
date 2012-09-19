# Hadoop-SplitOutputFormat

Split TextOutputFormat files by capping the number of bytes each file may contain.

I have many situations where I'd like to control the size of the output from my Hadoop Map/Reduce jobs.  The SplitOutputFormat provided here achieves this.

## Building

```bash
ant
```

## Using

### With Hadoop Streaming, locally

```bash
wget http://github.com/mattbornski/Hadoop-SplitOutputFormat/raw/master/hadoop-splitoutputformat.jar
hadoop jar $HADOOP_HOME/libexec/contrib/streaming/hadoop-streaming-1.0.3.jar -libjars hadoop-splitoutputformat.jar -D mapreduce.output.splitoutputformat.size=5000000 -input file:///path/to/some/data -output /path/to/output/folder -outputformat com.bornski.hadoop.SplitOutputFormat -mapper /path/to/mapper.py -reducer /path/to/reducer.py
ls -l /path/to/output/folder
```

### With Hadoop Streaming on Amazon Elastic MapReduce

TODO generate example

## Testing

I have manually tested this against Hadoop 1.0.3.

TODO Test suite + TravisCI
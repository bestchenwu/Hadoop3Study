package hadoopStudy.unit9_MapReduce_features;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class SortPartitioner extends Partitioner<IntPair, NullWritable> {

    @Override
    public int getPartition(IntPair intPair, NullWritable nullWritable, int numPartitions) {
        return Math.abs(intPair.getFirst() * 127) % numPartitions;
    }
}

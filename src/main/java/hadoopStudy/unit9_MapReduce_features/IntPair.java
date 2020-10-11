package hadoopStudy.unit9_MapReduce_features;

import hadoopStudy.unit5_compress.TextPair;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class IntPair implements WritableComparable<IntPair> {

    private Integer first;
    private Integer second;

    public IntPair() {
        super();
    }

    public IntPair(int firstValue, int secondValue) {
        this();
        this.first = firstValue;
        this.second = secondValue;
    }

    @Override
    public int hashCode() {
        int hashCode
                = Objects.hash(first, second);
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntPair)) {
            return false;
        }
        IntPair other = (IntPair) obj;
        return this.first.equals(other.first) && this.second.equals(other.second);
    }

    @Override
    public int compareTo(IntPair other) {
        int cmp = first.compareTo(other.first);
        return cmp != 0 ? cmp : second.compareTo(other.second);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(first);
        out.writeInt(second);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        first = in.readInt();
        second = in.readInt();
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("%d\t%d",first,second);
    }
}

package hadoopStudy.unit9_MapReduce_features;


import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.util.Comparator;

public class SortGroupComparator extends WritableComparator {

    protected SortGroupComparator() {
        super(IntPair.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        IntPair pair1 = (IntPair)a;
        IntPair pair2 = (IntPair)b;
        return pair1.getFirst().compareTo(pair2.getFirst());
    }
}

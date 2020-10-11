package hadoopStudy.unit9_MapReduce_features;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 对第一个key按照升序排序，对第二个key降序排序
 *
 * @author chenwu on 2020.10.11
 */
public class SortKeyComparator extends WritableComparator {

    protected SortKeyComparator() {
        super(IntPair.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        IntPair pair1 = (IntPair)a;
        IntPair pair2 = (IntPair)b;
        int cmp = pair1.getFirst().compareTo(pair2.getFirst());
        if(cmp!=0){
            return cmp;
        }
        return Math.negateExact(pair1.getSecond().compareTo(pair2.getSecond()));
    }
}

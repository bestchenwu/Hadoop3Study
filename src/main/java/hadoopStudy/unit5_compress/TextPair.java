package hadoopStudy.unit5_compress;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 自定义一个{@link org.apache.hadoop.io.Writable}
 *
 * @author chenwu on 2020.9.20
 */
public class TextPair implements WritableComparable<TextPair> {

    private Text first = new Text();
    private Text second = new Text();

    public TextPair(){
        super();
    }

    public TextPair(Text first,Text second){
        this();
        this.first = first;
        this.second = second;
    }

    public Text getFirst() {
        return first;
    }

    public Text getSecond() {
        return second;
    }

    @Override
    public int compareTo(TextPair other) {
        if(other==null){
            return 1;
        }
        int firstCompareResult = first.compareTo(other.first);
        if(firstCompareResult!=0){
            return firstCompareResult;
        }
        return second.compareTo(other.second);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        first.write(out);
        second.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        first.readFields(in);
        second.readFields(in);
    }

    @Override
    public boolean equals(Object other) {
        if(! (other instanceof TextPair)){
            return false;
        }
        TextPair otherTextPair = (TextPair)other;
        return this.first.equals(otherTextPair.first) && this.second.equals(otherTextPair.second);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result+=first.hashCode()*31;
        result+=second.hashCode()*31;
        return result;
    }

    @Override
    public String toString() {
        return String.format("TextPair[first=%s,second=%s]",first.toString(),second.toString());
    }

    public static class Comparator extends WritableComparator{

        private static final Text.Comparator TEXT_COMPARATOR = new Text.Comparator();

        /**
         * 比较b1从s1位置开始的l1长度的字符数组和b2从s2位置开始的l2长度的字符数组
         * <br/>可以参考:https://blog.csdn.net/hfy779012303/article/details/88291420
         *
         * @param b1
         * @param s1
         * @param l1
         * @param b2
         * @param s2
         * @param l2
         * @return
         */
        @Override
        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            //因为Text调用write方法的时候先写入的是byte流的长度，再写入byte数组，所以这里调用readInt方法可以拿到长度
            //第一个WritableUtils.decodeVIntSize(b1[s1])返回的是字节长度的占位情况,
            //因为byte最大支持127，所以如果大于127，就出现占多位的情况
            //这里为1
            //第二个是取的第一个text序列化的长度
            //所以假定第一个TextPair是abc ba,第二个TextPair是abc ab
            //那么存储的内容为[3,97,98,99,2,98,97] 第二个为[3,97,98,99,2,97,98]
            //这个时候firstLen1返回的是4
            //所以compare(b1 s1 firstLen1 b2 s2 firstLen2)实际上返回的比较的是[3,97,98,99]与[3,97,98,99]
            int firstLen1 = WritableUtils.decodeVIntSize(b1[s1])+readInt(b1,s1);
            int firstLen2 = WritableUtils.decodeVIntSize(b2[s1])+readInt(b2,s2);
            int compare = TEXT_COMPARATOR.compare(b1, s1, firstLen1, b2, s2, firstLen2);
            if(compare!=0){
                return compare;
            }
            return TEXT_COMPARATOR.compare(b1,s1+firstLen1,l1-firstLen1,b2,s2+firstLen2,l2-firstLen2);
        }

        static {
            WritableComparator.define(TextPair.class,new Comparator());
        }
    }
}

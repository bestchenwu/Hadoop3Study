package HBaseStudy.unit2;

import org.apache.hadoop.hbase.util.Bytes;

public class KeyValue implements Comparable<KeyValue>{

    public static final int RAW_KEY_LEN_SIZE = 4;
    public static final int VAL_LEN_SIZE = 4;
    public static final int OP_SIZE = 1;
    public static final int SEQ_ID_SIZE = 8;

    private byte[] key;
    private byte[] value;
    private Op op;
    private long sequenceId;

    public KeyValue(byte[] key,byte[] value,Op op,long sequenceId){
        this.key = key;
        this.value = value;
        this.op = op;
        this.sequenceId = sequenceId;
    }

    public static KeyValue create(byte[] key, byte[] value, Op op, long sequenceId) {
        return new KeyValue(key, value, op, sequenceId);
    }

    public static KeyValue createPut(byte[] key, byte[] value, long sequenceId) {
        return KeyValue.create(key, value, Op.Put, sequenceId);
    }

    public static KeyValue createDelete(byte[] key, long sequenceId) {
        return KeyValue.create(key, CommonConstants.EMPTY_BYTES, Op.Delete, sequenceId);
    }

    @Override
    public int compareTo(KeyValue other) {
        if(other==null){
            throw new IllegalArgumentException("other can't be null");
        }
        int ret = Bytes.compareTo(key,other.key);
        if(ret!=0){
            return ret;
        }
        if(this.sequenceId!=other.sequenceId){
            return this.sequenceId>other.sequenceId?1:-1;
        }
        if(this.op!=other.op){
            return this.op.getCode()>other.op.getCode()?-1:1;
        }
        return 0;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public Op getOp() {
        return op;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    public long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(long sequenceId) {
        this.sequenceId = sequenceId;
    }

    /**
     * rowkey的length
     *
     * @return
     */
    public int getRawkeyLength(){
        return key.length+OP_SIZE+SEQ_ID_SIZE;
    }

    public int getSerializerLength(){
        return RAW_KEY_LEN_SIZE+VAL_LEN_SIZE+getRawkeyLength()+value.length;
    }

    public byte[] toBytes(){
        int rowkeyLen = getRawkeyLength();
        int pos = 0;
        byte[] bytes = new byte[getSerializerLength()];

        //encode row key length
        byte[] rowkeyLenBytes = Bytes.toBytes(rowkeyLen);
        System.arraycopy(rowkeyLenBytes,0,bytes,0,RAW_KEY_LEN_SIZE);
        pos+=RAW_KEY_LEN_SIZE;

        //encode value length
        byte[] valueLenBytes = Bytes.toBytes(value.length);
        System.arraycopy(valueLenBytes,0,bytes,pos,VAL_LEN_SIZE);
        pos+=VAL_LEN_SIZE;

        //encode key
        System.arraycopy(key,0,bytes,pos,key.length);
        pos+=key.length;

        //encode op
        byte[] opBytes = Bytes.toBytes(op.getCode());
        System.arraycopy(opBytes,0,bytes,pos,OP_SIZE);
        pos+=OP_SIZE;

        //encode seq_id
        byte[] seqIdBytes = Bytes.toBytes(getSequenceId());
        System.arraycopy(seqIdBytes,0,bytes,pos,SEQ_ID_SIZE);
        pos+=SEQ_ID_SIZE;

        //enocde value
        System.arraycopy(value,0,bytes,pos,value.length);
        pos+=value.length;
        return bytes;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!(obj instanceof KeyValue)){
            return false;
        }
        KeyValue other = (KeyValue) obj;
        return this.compareTo(other) == 0;
    }

    public static KeyValue parseFrom(byte[] bytes,int offset){
        if(bytes==null){
            throw new IllegalArgumentException("bytes can't be null");
        }
        if(offset+RAW_KEY_LEN_SIZE+VAL_LEN_SIZE>bytes.length){
            throw new IllegalStateException("offset is not invalid:"+offset+"<bytes.length="+bytes.length);
        }
        //skip rawkey len
        int pos = offset;
        int rawkeyLen = Bytes.toInt(bytes,pos,RAW_KEY_LEN_SIZE);

        pos+=RAW_KEY_LEN_SIZE;
        //skip value len
        int valueLen = Bytes.toInt(bytes,pos,VAL_LEN_SIZE);
        pos+=VAL_LEN_SIZE;

        //decode key
        int keyLen = rawkeyLen-OP_SIZE-SEQ_ID_SIZE;
        byte[] key = Bytes.copy(bytes,pos,keyLen);
        pos+=keyLen;

        //decode op
        byte opCode = bytes[pos];
        Op op = Op.parseFrom(opCode);
        pos+=OP_SIZE;

        //decode seq_id
        byte[] seqId_bytes = Bytes.copy(bytes,pos,SEQ_ID_SIZE);
        pos+=SEQ_ID_SIZE;
        long seqId = Bytes.toLong(seqId_bytes);

        //decode value
        byte[] values = Bytes.copy(bytes,pos,valueLen);
        return create(key,values,op,seqId);
    }

    public static KeyValue parseFrom(byte[] bytes){
        return parseFrom(bytes,0);
    }

    @Override
    public String toString() {
        String str = String.format("key=%s,value=%s,op=%s,seqId=%d",Bytes.toString(key),Bytes.toString(value),op,sequenceId);
        return str;
    }
}

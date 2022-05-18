package HBaseStudy.unit2;

public enum Op {

    Put((byte)0),
    Delete((byte)1);

    private int code;

     Op(byte code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static Op parseFrom(byte code){
         for(Op op:Op.values()){
             if(op.code == code){
                 return op;
             }
         }
         return null;
    }


    @Override
    public String toString() {
        return this.name();
    }
}

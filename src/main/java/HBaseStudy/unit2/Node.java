package HBaseStudy.unit2;

public class Node {

    //当前节点的值
    public long data;
    //分别定义上下左右四个方向的指针
    public Node left,right,up,down;

    public Node(long data){
        this.data = data;
    }
}

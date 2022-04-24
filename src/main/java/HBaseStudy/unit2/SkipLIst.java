package HBaseStudy.unit2;

/**
 *　参考https://www.cnblogs.com/bigyangsir/p/15629640.html
 *  https://blog.csdn.net/solo_jm/article/details/117370626
 *  快表练习
 *
 * @author chenwu on 2022.4.24
 **/
public class SkipLIst {

    private class Node{
        //当前节点的值
        public long data;
        //分别定义上下左右四个方向的指针
        public Node left,right,up,down;

        public Node(long data){
            this.data = data;
        }
    }

    //最上层链表的头尾节点
    private Node head,tail;
    //总层数
    private int level;
    //总节点个数
    private int size;

    public SkipLIst(){
        this.head = new Node(-1);
        this.tail = new Node(-1);
        head.right = tail;
        tail.left = head;
        level = 1;
        size = 0;
    }

    /**
     * 判断是否包含某个值
     *
     * @param score
     * @return 包含则返回true,否则返回false
     * @author chenwu on 2022.4.24
     */
    public boolean contain(long score){
        return false;
    }
}

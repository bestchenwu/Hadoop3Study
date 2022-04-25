package HBaseStudy.unit2;

/**
 *　参考https://www.cnblogs.com/bigyangsir/p/15629640.html<br/>
 *  https://blog.csdn.net/solo_jm/article/details/117370626<br/>
 *  快表练习
 *
 * @author chenwu on 2022.4.24
 **/
public class SkipList {

    //最上层链表的头尾节点
    private Node head,tail;
    //总层数
    private int level;
    //总节点个数
    private int size;

    public SkipList(){
        this.head = new Node(Long.MIN_VALUE);
        this.tail = new Node(Long.MAX_VALUE);
        head.right = tail;
        tail.left = head;
        level = 1;
        size = 0;
    }

    public Node getHead(){
        return head;
    }

    public Node getTail(){
        return tail;
    }

    /**
     * 判断是否包含某个值
     *
     * @param score
     * @return 包含则返回true,否则返回false
     * @author chenwu on 2022.4.24
     */
    public boolean contain(long score){
        Node tmp = head;
        while(tmp!=null){
            if(tmp.data == score){
                return true;
            }else if(tmp.right.data<=score){
                tmp = tmp.right;
            }else{
                tmp = tmp.down;
            }
        }
        return false;
    }

    public Node find(long score){
        Node tmp = head;
        while(tmp!=null){
            if(tmp.data == score){
                return tmp;
            }else if(tmp.right.data<=score){
                tmp = tmp.right;
            }else{
                tmp = tmp.down;
            }
        }
        return tmp;
    }

    /**
     * 获取比指定score小的前一个节点
     *
     * @param score
     * @return {@link Node}
     * @author chenwu on 2022.4.24
     */
    public Node findPreNode(long score){
       Node tmp = head;
       while(tmp!=null){
           if(tmp.right.data>score){
               if(tmp.down==null){
                   return tmp;
               }else{
                   tmp = tmp.down;
               }
           }else{
               tmp = tmp.right;
           }
       }
       return tmp;
    }

    private Node headToBottom(){
        Node tmp = head;
        while(tmp.down!=null){
            tmp = tmp.down;
        }
        return tmp;
    }

}

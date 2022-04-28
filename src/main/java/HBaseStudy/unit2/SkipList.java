package HBaseStudy.unit2;

import java.util.Random;

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

    private Random random = new Random(47);

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

    private void setHead(Node newHead){
        this.head = newHead;
    }

    public Node getTail(){
        return tail;
    }

    private void setTail(Node newTail){
        this.tail = newTail;
    }

    /**
     * 是否是链表头部节点
     *
     * @param node
     * @return boolean 是否是头部节点
     * @author chenwu on 2022.4.28
     */
    public boolean isHead(Node node){
        return node.data==Long.MIN_VALUE;
    }

    /**
     * 是否是尾部节点
     *
     * @param node
     * @return boolean 是否是尾部节点
     * @author chenwu on 2022.4.28
     */
    public boolean isTail(Node node){
        return node.data == Long.MAX_VALUE;
    }

    @Deprecated
    public void setSize(int size){
        this.size = size;
    }

    @Deprecated
    public void setLevel(int level){
        this.level = level;
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
        return null;
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

    public boolean isEmpty(){
        return size>0;
    }

    /**
     * 打印所有nodes
     *
     * @return {@link String}
     * @author chenwu on 2022.4.28
     */
    public String printAllNodes(){
        StringBuilder sb = new StringBuilder();
        Node tmp = headToBottom();
        if(isHead(tmp)){
            tmp = tmp.right;
        }
        while(true){
            sb.append(tmp.data);
            tmp = tmp.right;
            if(isTail(tmp)){
                break;
            }
            sb.append("->");
        }
        return sb.toString();
    }

    public boolean removeByScore(long score){
        Node node = find(score);
        if(node == null){
            return false;
        }
        boolean res = removeByNode(node);
        return res;
    }

    public boolean removeByNode(Node node){
        if(node == null){
            return false;
        }else{
            node.left.right = node.right;
            node.right.left = node.left;
            Node upNode = node.up;
            Node downNode = node.down;
            while(upNode!=null){
                upNode.left.right = upNode.right;
                upNode.right.left = upNode.left;
                upNode = upNode.up;
            }
            while(downNode!=null){
                downNode.left.right = downNode.right;
                downNode.right.left = downNode.left;
                downNode = downNode.down;
            }
            this.size-=1;
            return true;
        }
    }

    /**
     * 插入一个节点
     *
     * @param newScore
     * @return {@link Node} 把插入的节点返回
     * @author chenwu on 2022.4.28
     */
    public Node insertValue(long newScore){
        Node target = findPreNode(newScore);
        //先插入在最底层链表
        Node newNode = new Node(newScore);
        newNode.left = target;
        newNode.right = target.right;
        target.right.left = newNode;
        target.right = newNode;
        this.size+=1;
        //通过随机的方式获取该元素要占据的层数
        int maxLevel = level+1;
        //计算从底部开始往上需要占据的层次
        int targetLevel = random.nextInt(maxLevel);
        boolean buildNewLevel = false;
        if(targetLevel==0){
            buildNewLevel = true;
        }
        while(targetLevel>0){
            Node right = newNode.right;
            while(right!=null && right.up==null){
                right = right.right;
            }
            if(right == null){
                break;
            }
            right = right.up;
            Node newUpNode = new Node(newScore);
            newUpNode.right = right;
            newUpNode.left = right.left;
            right.left.right = newUpNode;
            right.left = newUpNode;
            newUpNode.down = newNode;
            newNode.up = newUpNode;
            targetLevel-=1;
            newNode = newUpNode;
        }
        if(buildNewLevel){
            Node head = new Node(Long.MIN_VALUE);
            Node tail = new Node(Long.MAX_VALUE);
            Node newTopNode = new Node(newScore);
            head.right = newTopNode;
            newTopNode.right = tail;
            newTopNode.left = head;
            tail.left = newTopNode;
            //建立新层与新节点的关系
            newTopNode.down = newNode;
            newNode.up = newTopNode;
            Node originHead = getHead();
            Node originTail = getTail();
            head.down = originHead;
            originHead.up = head;
            originTail.up = tail;
            tail.down = originTail;
            //赋予新的head tail
            setHead(head);
            setTail(tail);
            this.level+=1;
        }
        return newNode;
    }
}

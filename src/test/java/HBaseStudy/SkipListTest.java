package HBaseStudy;

import HBaseStudy.unit2.Node;
import HBaseStudy.unit2.SkipList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link SkipList}的单元测试
 *
 * @author chenwu on 2022.4.25
 */
public class SkipListTest {

    private SkipList skipList;

    @Before
    public void init(){
        skipList = new SkipList();
        Node head = skipList.getHead();
        Node tail = skipList.getTail();
        //在下方插入一个新链表
        Node newNode_head = new Node(Long.MIN_VALUE);
        Node newNode_tail = new Node(Long.MAX_VALUE);
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        newNode_head.right = node1;
        node1.left = newNode_head;
        node1.right = node2;
        node2.left = node1;
        node2.right = node3;
        node3.left = node2;
        node3.right = newNode_tail;
        newNode_tail.left = node3;
        head.down = newNode_head;
        newNode_head.up = head;
        //在上层建立一个中间节点
        Node halfFindNode = new Node(2);
        head.right = halfFindNode;
        halfFindNode.left = head;
        halfFindNode.right = tail;
        tail.left = halfFindNode;
        halfFindNode.down = node2;
        node2.up = halfFindNode;
        skipList.setSize(3);
        skipList.setLevel(2);
    }

    @Test
    public void testContain(){
        long score = 3;
        boolean contain = skipList.contain(score);
        Assert.assertTrue(contain);
    }

    @Test
    public void testFindPreNode(){
        long score = 3;
        Node preNode = skipList.findPreNode(score);
        Assert.assertEquals(2l,preNode.data);
    }
}

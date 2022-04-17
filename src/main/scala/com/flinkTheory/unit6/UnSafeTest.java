package com.flinkTheory.unit6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sun.misc.Unsafe;

import java.lang.Class;
import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

/**
 * Java的{@link sun.misc.Unsafe}学习<br/>
 * 更多参考:https://blog.csdn.net/zyzzxycj/article/details/89877863
 *
 * @author chenwu on 2022.4.17
 */
public class UnSafeTest {

    private Unsafe unsafe;

    @Before
    public void init() throws Exception {
        //通过反射获取到
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        unsafe = (Unsafe) f.get(null);
    }

    /**
     * 绕过构造函数直接创建对象
     *
     * @throws Exception
     */
    @Test
    public void testAllocateInstance() throws Exception {
        A a = new A();
        a.printA();
        A a1 = A.class.newInstance();
        a1.printA();
        A a2 = (A) unsafe.allocateInstance(A.class);
        //这里输出a = 0,绕过了构造函数
        a2.printA();
    }

    /**
     * 绕过限制符,直接修改成员变量值
     *
     * @throws Exception
     * @author chenwu on 2022.4.17
     */
    @Test
    public void testModifyField() throws Exception{
        Guard guard = new Guard();
        Assert.assertFalse(guard.isAllowAccessed());
        Field access_allowed = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
        unsafe.putInt(guard,unsafe.objectFieldOffset(access_allowed),42);
        Assert.assertTrue(guard.isAllowAccessed());
    }

    /**
     * 计算所有静态成员的最大位移
     *
     * @author chenwu on 2022.4.17
     */
    private long sizeof(Object o){
        Class aClass = o.getClass();
        HashSet<Field> fieldHashSet = new HashSet<>();
        while(!aClass.equals(Object.class)){
            Field[] declaredFields = aClass.getDeclaredFields();
            for(Field field :declaredFields){
                if((field.getModifiers() & Modifier.STATIC ) == 0){
                    fieldHashSet.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        long maxOffset = 0;
        for(Field field : fieldHashSet){
            long fieldOffset = unsafe.objectFieldOffset(field);
            if(fieldOffset>maxOffset){
                maxOffset = fieldOffset;
            }
        }
        maxOffset =  (maxOffset/8+1)*8;
        return maxOffset;
    }

    /**
     * 计算所有静态成员的最大位移
     *
     * @author chenwu on 2022.4.17
     */
    @Test
    public void testSizeof(){
        Guard guard = new Guard();
        long maxOffset = sizeof(guard);
        System.out.println("maxOffset = "+maxOffset);
    }

    /**
     * 测试静态成员的浅拷贝<br/>
     * 第一次运行会报Core dumps have been disabled. To enable core dumping, try "ulimit -c unlimited" before starting Java again,
     * 需要执行
     *
     * @author chenwu on 2022.4.17
     */
    @Test
    public void testShallawCopy(){
        Guard guard = new Guard();
        long size = sizeof(guard);
        long start = toAddress(guard);
        long newStart = unsafe.allocateMemory(size);
        unsafe.copyMemory(start,newStart,size);
        Object newObject = fromAddress(newStart);
        Guard newGuard = (Guard) newObject;
        System.out.println(newGuard);
    }

    private long toAddress(Object o){
        //可以获取数组第一个元素的偏移地址
        Object[] array = new Object[]{o};
        int baseOffset = unsafe.arrayBaseOffset(array.getClass());
        long aLong = unsafe.getLong(array, (long)baseOffset);
        return aLong;
    }

    private Object fromAddress(long address){
        Object[] array = new Object[]{null};
        int baseOffset = unsafe.arrayBaseOffset(array.getClass());
        unsafe.putLong(array,(long)baseOffset,address);
        return array[0];
    }




}

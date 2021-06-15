package redisStudy.appliance2;

/**
 * 塞入消息队列的对象
 *
 * @param <T>
 * @author chenwu on 2021.6.11
 */
public class TaskItem<T> {

    public String id;
    public T msg;

    @Override
    public String toString() {
        return "TaskItem{" +
                "id='" + id + '\'' +
                ", msg=" + msg +
                '}';
    }
}

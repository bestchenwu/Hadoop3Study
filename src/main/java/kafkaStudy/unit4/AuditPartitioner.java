package kafkaStudy.unit4;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 自定义key分组器
 *
 * @author chenwu on 2020.10.9
 */
public class AuditPartitioner implements Partitioner {

    private Random random;

    /**
     * 当key是null或者不包含audit字符串的时候，被随机分配到[0,count-1)<br/>
     * 否则分配到最后一个分区
     *
     * @param topic
     * @param key
     * @param keyBytes
     * @param value
     * @param valueBytes
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
        int partitionSize = partitionInfos.size();
        int lastPartitionCount = partitionSize - 1;
        String keyStr = key == null ? "" : key.toString();
        return (StringUtils.isBlank(keyStr) || !keyStr.contains("audit")) ? random.nextInt(lastPartitionCount) : lastPartitionCount;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
        random = new Random(47);
    }
}

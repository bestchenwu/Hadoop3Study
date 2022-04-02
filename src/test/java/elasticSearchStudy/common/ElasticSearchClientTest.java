package elasticSearchStudy.common;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试{@link ElasticSearchClient}
 *
 * @author chenwu on 2022.4.2
 */
public class ElasticSearchClientTest {

    private ElasticSearchClient elasticSearchClient;

    @Before
    public void init(){
        elasticSearchClient = ElasticSearchClient.getInstance("elasticSearch/elasticSearchCluster.properties");
    }

    @Test
    public void testBulkUpdate() throws IOException{
        Map<String,Object> map = new HashMap<>();
        map.put("index","customers");
        map.put("id","1");
        map.put("age",50);
        map.put("sex","male");
        boolean res = elasticSearchClient.bulkUpdate(map);
        Assert.assertTrue(res);
    }

    @After
    public void end() throws IOException {
        elasticSearchClient.close();
    }
}

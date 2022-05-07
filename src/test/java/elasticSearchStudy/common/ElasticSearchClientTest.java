package elasticSearchStudy.common;

import org.apache.commons.math3.util.Pair;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    //@Test
    public void testBulkUpdate() throws IOException{
        Map<String,Object> map = new HashMap<>();
        map.put("index","customers");
        map.put("id","1");
        map.put("age",50);
        map.put("sex","male");
        boolean res = elasticSearchClient.bulkUpdate(map);
        Assert.assertTrue(res);
    }

    //@Test
    public void testSearch() throws IOException{
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        searchSourceBuilder.sort("balance", SortOrder.DESC);
        searchSourceBuilder.size(3);
        //必须匹配account_number=20
        //searchSourceBuilder.query(QueryBuilders.matchQuery("account_number",20));
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //boolQueryBuilder.should(QueryBuilders.matchQuery("address","nil"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("address","lane"));
        //设置范围查询
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("balance").gte(20000).lt(30000));
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.fetchSource(new String[]{"account_number","balance","address","address"},null);
        SearchRequest searchRequest = new SearchRequest(new String[]{"bank"},searchSourceBuilder);
        SearchHits searchHits = elasticSearchClient.search(searchRequest);
        for(SearchHit searchHit : searchHits.getHits()){
            System.out.println(searchHit.getSourceAsMap());
        }
    }

    //@Test
    public void testGetByDocId() throws IOException{
        GetResponse twitter = elasticSearchClient.getByDocId("1", "twitter");
        System.out.println("source="+twitter.getSource());
        System.out.println("fields="+twitter.getFields());
    }

    @Test
    public void testDeleteById() throws IOException{
        boolean res = elasticSearchClient.deleteById("1","twitter");
        Assert.assertTrue(res);

    }

    @Test
    public void testMget() throws IOException{
        List<Pair<String,String>> itemList = new ArrayList<>();
        itemList.add(new Pair<>("bank","2"));
        itemList.add(new Pair<>("twitter","1"));
        MultiGetResponse multiGetResponse = elasticSearchClient.mget(itemList);
        MultiGetItemResponse[] responses = multiGetResponse.getResponses();
        for(MultiGetItemResponse response : responses){
            if(response.isFailed() || !response.getResponse().isExists()){
                System.out.println("failure get index:"+response.getIndex()+",id="+response.getId());
            }else{
                System.out.println("success get index:"+response.getIndex()+",id="+response.getId());
                System.out.println(response.getResponse().getSourceAsMap());
            }
        }
    }

    /**
     * 测试读取date类型
     *
     * @throws IOException
     * @author chenwu on 2022.5.7
     */
    @Test
    public void testReadDate() throws IOException{
        GetResponse myIndexResponse = elasticSearchClient.getByDocId("3", "my_index");
        Map<String, Object> sourceAsMap = myIndexResponse.getSourceAsMap();
        String create_date = (String)sourceAsMap.get("create_date");
        System.out.println("create_date="+create_date);
    }

    @After
    public void end() throws IOException {
        elasticSearchClient.close();
    }
}

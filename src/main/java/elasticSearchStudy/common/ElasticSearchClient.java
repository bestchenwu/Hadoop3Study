package elasticSearchStudy.common;

import common.constants.SymbolConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * elasticSearch的客户端
 *
 * @author chenwu on 2021.6.22
 */
public class ElasticSearchClient implements Closeable {

    private List<HttpHost> hostsAndPorts = new ArrayList<>();
    private RestHighLevelClient restHighLevelClient;

    private static ElasticSearchClient instance;

    private ElasticSearchClient(String configFileName) {
        loadFromProperties(configFileName);
        RestClientBuilder restClientBuilder =
                RestClient.builder(hostsAndPorts.toArray(new HttpHost[hostsAndPorts.size()]));
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
    }

    public static ElasticSearchClient getInstance(String configFileName) {
        synchronized (ElasticSearchClient.class) {
            if (instance == null) {
                instance = new ElasticSearchClient(configFileName);
            }
            return instance;
        }
    }

    private void loadFromProperties(String configFileName) {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(configFileName);
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String esHostPorts = properties.getProperty(ElasticSearchConstants.ES_HOST_PORTS);
        String[] splitArray = esHostPorts.split(SymbolConstants.SYMBOL_DH);
        for (String item : splitArray) {
            String[] hostPortArray = item.split(SymbolConstants.SYMBOL_MH);
            hostsAndPorts.add(new HttpHost(hostPortArray[0], Integer.parseInt(hostPortArray[1])));
        }
    }

    @Override
    public void close() throws IOException {
        this.restHighLevelClient.close();
    }

    /**
     * 批量更新
     *
     * @param map
     * @author chenwu on 2022.4.2
     */
    public boolean bulkUpdate(Map<String, Object> map) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        String indexName = (String) map.remove("index");
        String id = (String) map.remove("id");
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName).id(id).doc(map);
        bulkRequest.add(updateRequest);
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();
    }

    /**
     * 根据ID和index查询索引
     *
     * @param id
     * @param index
     * @return {@link GetResponse}
     * @throws IOException
     * @author chenwu on 2022.4.13
     */
    public GetResponse getByDocId(String id,String index) throws IOException{
        GetRequest getRequest = new GetRequest();
        getRequest  = getRequest.id(id).index(index);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        return getResponse;
    }

    /**
     * 根据指定条件搜索
     *
     * @param {@link SearchRequest} searchRequest
     * @return {@link SearchHits}
     * @throws IOException
     * @author chenwu on 2022.4.14
     */
    public SearchHits search(SearchRequest searchRequest) throws IOException{
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse.getHits();
    }

    /**
     * 根据文档ID和索引名称删除
     *
     * @param id
     * @param indexName
     * @author chenwu on 2022.4.14
     */
    public boolean deleteById(String id,String indexName) throws IOException{
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest = deleteRequest.index(indexName).id(id);
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        RestStatus status = deleteResponse.status();
        return status == RestStatus.OK;
    }

}

package elasticSearchStudy.common;

import common.constants.SymbolConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * elasticSearch的客户端
 *
 * @author chenwu on 2021.6.22
 */
public class ElasticSearchClient {

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

    private void loadFromProperties(String configFileName){
        InputStream resourceAsStream = getClass().getResourceAsStream(configFileName);
        Properties properties = new Properties();
        try{
            properties.load(resourceAsStream);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        String esHostPorts = properties.getProperty(ElasticSearchConstants.ES_HOST_PORTS);
        String[] splitArray = esHostPorts.split(SymbolConstants.SYMBOL_DH);
        for(String item : splitArray){
            String[] hostPortArray = item.split(SymbolConstants.SYMBOL_MH);
            hostsAndPorts.add(new HttpHost(hostPortArray[0],Integer.parseInt(hostPortArray[1])));
        }
    }
}

package es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.explain.ExplainRequest;
import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * @author sunguiyong
 * @date 2022/1/13 9:00 下午
 */
public class EsTest {

    public static void main(String[] args) throws Exception {
        //write();

        read();

        System.out.println();
    }

    private static void read() throws IOException {

        BoolQueryBuilder query = new BoolQueryBuilder();
        QueryBuilder term = new MatchQueryBuilder("field_name", "quick");
        query.must(term);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);

        String[] slices = new String[]{"search_score_demo_2"};
        SearchRequest searchRequest = new SearchRequest(slices, searchSourceBuilder);
        SearchResponse response = getClient().search(searchRequest, RequestOptions.DEFAULT);

        ExplainRequest explainRequest = new ExplainRequest();
        explainRequest.query(query);
        explainRequest.index("search_score_demo_2");
        explainRequest.id(response.getHits().getHits()[0].getId());
        ExplainResponse explainResponse = getClient().explain(explainRequest, RequestOptions.DEFAULT);
        System.out.println();
    }

    private static void write() throws IOException {
        String data = "{\"field_name\":\"quick\"}";

        BulkRequest bulkRequest = new BulkRequest();

        IndexRequest indexRequest = new IndexRequest("search_score_demo_2");
        indexRequest.source(data, XContentType.JSON);
        indexRequest.create(false);
        bulkRequest.add(indexRequest);

        /*indexRequest = new IndexRequest("search_score_demo");
        data = "{\"field_name\":\"the quick test adsd ddasd asds\"}";
        indexRequest.source(data, XContentType.JSON);
        indexRequest.create(false);
        bulkRequest.add(indexRequest);

        indexRequest = new IndexRequest("search_score_demo");
        data = "{\"field_name\":\"the quick test adsd ddasd asdsa asdf asdkkskdkksdksdksk\"}";
        indexRequest.source(data, XContentType.JSON);
        indexRequest.create(false);
        bulkRequest.add(indexRequest);*/

        getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        /*getClient().indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        System.out.println("将id为：" + indexRequest.id() + "的数据存入ES时存在失败的分片，原因为：" + failure.getCause());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("error" + e.getCause());
            }
        });*/
    }

    private static RestHighLevelClient getClient() {
        HttpHost host = new HttpHost("localhost", 9200);
        RestClientBuilder builder = RestClient.builder(host);
        return new RestHighLevelClient(builder);
    }
}

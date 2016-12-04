import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class ElasticMetods {
    Settings settings = null;
    TransportClient client = null;

    public ElasticMetods() {
        settings = Settings.builder().put("cluster.name", "my-application").build();
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        }catch (Exception e){
            System.out.println("error");
        }
    }

    public void PushWordsInElastic(ArrayList<String[]> word){
        int index = 1;
        IndexResponse response = null;
        for (int i = 0; i < word.size(); i++) {
            for (int j = 1; j < word.get(i).length; j++) {
                String json = "{" +
                        "\"word\":\""+word.get(i)[j]+"\"" +
                        "}";
                response = client.prepareIndex("book", "Stone").setId(String.valueOf(index))
                        .setSource(json)
                        .get();
                index++;

            }
        }
        System.out.println("Success");
    }

    public void SearchWord(String searchWord){
        SearchResponse searchResponse = client
                .prepareSearch("book")
                .setTypes("Stone")
                .setSearchType(SearchType.DEFAULT)
                .setQuery(QueryBuilders.termQuery("word",searchWord))
                .setFrom(0)
                .setSize(10000)
                .execute()
                .actionGet();
        System.out.println(searchResponse.toString());
        JsonElement element =  new JsonParser().parse(searchResponse.toString());
        JsonObject object = element.getAsJsonObject();
        object = object.getAsJsonObject("hits");
        System.out.println("Найдено совпадений - "+object.get("total"));
        JsonArray array = object.getAsJsonArray("hits");
        for (int i = 0; i < array.size(); i++) {
            System.out.println("Id - " +array.get(i).getAsJsonObject().get("_id").toString().replaceAll("\"",""));
        }
    }
}

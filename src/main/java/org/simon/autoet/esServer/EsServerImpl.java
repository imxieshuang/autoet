package org.simon.autoet.esServer;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.simon.autoet.track.Result;
import org.simon.autoet.util.ParseJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * es服务接口类
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:30
 */
public class EsServerImpl implements EsServer {

    private String hostName;
    private int port;
    private RestClient client;
    private List<String> createIndices = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(EsServerImpl.class);

    public EsServerImpl(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    @Override
    public RestClient getClient() {
        if (client == null) {
            this.client = RestClient.builder(new HttpHost(this.hostName, this.port, "http")).build();
        }
        return client;

    }


    @Override
    public Result indexBulk(String source) {
        RestClient client = getClient();
        HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
        Map<String, String> params = Collections.emptyMap();
        try {
            Response response = client.performRequest("POST", "/_bulk", params, entity);
            String responseStr = EntityUtils.toString(response.getEntity());

            return ParseJsonUtils.parseIndex(responseStr);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Result query(String index, String type, String query) {
        RestClient client = getClient();
        Map<String, String> params = Collections.emptyMap();
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        String endpoint = "/" + index + "/" + type + "/_search";
        try {
            Response response = client.performRequest("GET", endpoint, params, entity);
            String responseStr = EntityUtils.toString(response.getEntity());

            return ParseJsonUtils.parseQuery(responseStr);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean createIndex(String index, String mapping) {
        if (!existIndex(index)) {
            RestClient client = getClient();
            Map<String, String> params = Collections.emptyMap();
            HttpEntity entity = new NStringEntity(mapping, ContentType.APPLICATION_JSON);
            try {
                Response response = client.performRequest("PUT", "/" + index, params, entity);
                String responseStr = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = JSONObject.parseObject(responseStr);
                String acknowledged = jsonResponse.getString("acknowledged");
                if ("true".equals(acknowledged)) {
                    createIndices.add(index);
                    LOGGER.info("create " + index + " success");
                }

                return Boolean.valueOf(acknowledged);
            } catch (IOException e) {
                LOGGER.error("create index fail: " + index + ", result " + e.getMessage());
            }
        }
        LOGGER.error("index exit: " + index);
        return false;
    }

    @Override
    public Boolean deleteIndex(String index) {
        RestClient client = getClient();
        try {
            Response response = client.performRequest("DELETE", "/" + index);
            String responseStr = EntityUtils.toString(response.getEntity());

            JSONObject jsonResponse = JSONObject.parseObject(responseStr);
            String acknowledged = jsonResponse.getString("acknowledged");
            LOGGER.info("delete " + index + " success");
            return Boolean.valueOf(acknowledged);
        } catch (IOException e) {
            LOGGER.error("create index fail: " + index + ", result: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean existIndex(String index) {
        RestClient client = getClient();
        try {
            Response response = client.performRequest("HEAD", "/" + index);

            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (IOException e) {
            LOGGER.error("create index fail: " + index + ", result: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean existType(String index, String type) {
        RestClient client = getClient();
        try {
            Response response = client.performRequest("HEAD", "/index/_mapping/" + type);

            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (IOException e) {
            LOGGER.error("create index fail: " + index + ", result: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void close() {
        try {
            getClient().close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public List<String> getCreateIndices() {
        return createIndices;
    }
}

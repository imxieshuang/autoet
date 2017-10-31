package org.simon.autoet.elasticsearch;

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
        HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
        Map<String, String> params = Collections.emptyMap();
        try {
            Response response = getClient().performRequest("POST", "/_bulk", params, entity);
            String responseStr = EntityUtils.toString(response.getEntity());

            return ParseJsonUtils.parseIndex(responseStr);
        } catch (IOException e) {
            LOGGER.error("index document failed", e);
        }
        return null;
    }

    @Override
    public Result query(String index, String type, String query) {
        Map<String, String> params = Collections.emptyMap();
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        String endpoint = "/" + index + "/" + type + "/_search";
        try {
            Response response = getClient().performRequest("GET", endpoint, params, entity);
            String responseStr = EntityUtils.toString(response.getEntity());

            return ParseJsonUtils.parseQuery(responseStr);
        } catch (IOException e) {
            LOGGER.error("query index failed", e);
        }
        return null;
    }

    @Override
    public Boolean createIndex(String index, String mapping) {
        if (!existIndex(index)) {
            Map<String, String> params = Collections.emptyMap();
            HttpEntity entity = new NStringEntity(mapping, ContentType.APPLICATION_JSON);
            try {
                Response response = getClient().performRequest("PUT", "/" + index, params, entity);
                String responseStr = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = JSONObject.parseObject(responseStr);
                String acknowledged = jsonResponse.getString("acknowledged");
                if ("true".equals(acknowledged)) {
                    createIndices.add(index);
                    LOGGER.info("create " + index + " success");
                }
                return Boolean.valueOf(acknowledged);
            } catch (IOException e) {
                LOGGER.error("create index failed: " + index, e);
                return false;
            }
        }
        LOGGER.info("index exist: " + index);
        return true;
    }

    @Override
    public Boolean deleteIndex(String index) {
        try {
            Response response = getClient().performRequest("DELETE", "/" + index);
            String responseStr = EntityUtils.toString(response.getEntity());

            JSONObject jsonResponse = JSONObject.parseObject(responseStr);
            String acknowledged = jsonResponse.getString("acknowledged");
            LOGGER.info("delete " + index + " success");
            return Boolean.valueOf(acknowledged);
        } catch (IOException e) {
            LOGGER.error("delete index failed: " + index, e);
        }
        return false;
    }

    @Override
    public Boolean existIndex(String index) {
        try {
            Response response = getClient().performRequest("HEAD", "/" + index);

            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (IOException e) {
            LOGGER.error("exist index failed: " + index, e);
        }
        return false;
    }

    @Override
    public Boolean existType(String index, String type) {
        try {
            Response response = getClient().performRequest("HEAD", "/index/_mapping/" + type);

            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (IOException e) {
            LOGGER.error("exist type failed: " + index, e);
        }
        return false;
    }

    @Override
    public void close() {
        try {
            getClient().close();
        } catch (IOException e) {
            LOGGER.error("close elasticsearch client failed", e);
        }
    }

    public List<String> getCreateIndices() {
        return createIndices;
    }
}

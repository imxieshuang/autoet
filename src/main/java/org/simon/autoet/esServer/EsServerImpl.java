package org.simon.autoet.esServer;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.simon.autoet.trackServer.Result;
import org.simon.autoet.util.ParseJsonUtil;

/**
 * Created by Administrator on 2017/10/26.
 */
public class EsServerImpl implements EsServer {

    private String hostName;
    private int port;
    private RestClient client;
    private static final Log LOG = LogFactory.getLog(EsServerImpl.class);

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

            Result result = ParseJsonUtil.parseIndex(responseStr);
            return result;
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void query(String index, String type, String query) {
        RestClient client = getClient();
        Map<String, String> params = Collections.emptyMap();
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        String endpoint="/"+index+"/"+type+"/_search";
        try {
            Response response = client.performRequest("GET", endpoint, params, entity);
            String responseStr = EntityUtils.toString(response.getEntity());
            System.out.println(responseStr);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}

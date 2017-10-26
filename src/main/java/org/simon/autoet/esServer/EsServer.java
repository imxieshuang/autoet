package org.simon.autoet.esServer;

import org.elasticsearch.client.RestClient;
import org.simon.autoet.trackServer.Result;

/**
 * Created by Administrator on 2017/10/25.
 */
public interface EsServer {
    public RestClient getClient();


    public Result indexBulk(String source);

    public void query(String index, String type, String query);
}

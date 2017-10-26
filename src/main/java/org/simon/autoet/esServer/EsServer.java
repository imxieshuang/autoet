package org.simon.autoet.esServer;

import java.util.List;
import org.elasticsearch.client.RestClient;
import org.simon.autoet.trackServer.Result;

/**
 * Created by Administrator on 2017/10/25.
 */
public interface EsServer {
    public RestClient getClient();


    public Result indexBulk(String source);

    public Result query(String index, String type, String query);

    public Boolean createIndex(String index, String mapping);

    public Boolean deleteIndex(String index);

    public Boolean existIndex(String index);

    public Boolean existType(String index, String type);

    public List<String> getCreateIndices();

    public void close();
}

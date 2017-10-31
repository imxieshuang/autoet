package org.simon.autoet.elasticsearch;

import java.util.List;
import org.elasticsearch.client.RestClient;
import org.simon.autoet.track.Result;

/**
 * es服务接口
 * @author simon
 * @since 2017/10/28 12:29
 * @version V1.0
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

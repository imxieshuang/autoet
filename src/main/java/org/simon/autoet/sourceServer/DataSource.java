package org.simon.autoet.sourceServer;

import org.simon.autoet.trackServer.Result;

/**
 * 数据源接口
 * @author simon
 * @since 2017/10/28 12:35
 * @version V1.0
 */
public interface  DataSource {
    public Result insertEs(String index, String type, final int bulkSize,String data);
}

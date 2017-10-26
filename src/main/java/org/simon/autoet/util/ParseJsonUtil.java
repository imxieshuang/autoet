package org.simon.autoet.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.simon.autoet.trackServer.Result;

/**
 * Created by Administrator on 2017/10/26.
 */
public class ParseJsonUtil {

    public static Result parseIndex(String indexResponse) {
        JSONObject jsonResponse = JSONObject.parseObject(indexResponse);

        Long took = jsonResponse.getLong("took");
        String errors = jsonResponse.getString("errors");

        JSONArray items = jsonResponse.getJSONArray("items");
        int total = items.size();

        double throughput = total * 1000.0 / took;
        if (errors.equals("false")) {
            return new Result(took, total, total, 0, throughput, 0);
        }

        int error = 0;
        for (Object item : items) {
            JSONObject jsonItem = (JSONObject) item;
            JSONObject create = jsonItem.getJSONObject("create");
            String status = create.getString("status");
            if (status.equals("400")) {
                error++;
            }
        }
        return new Result(took, total - error, total, error, throughput, error / total);

    }
}

package org.simon.autoet.track;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simon.autoet.exception.AutoRuntimeException;
import org.simon.autoet.util.ParseJsonUtils;

/**
 * 定义本次测试
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:45
 */
public class Track {
    private String trackStr;
    private Challenge challenge;
    private Map<String, Operation> operationMap;
    private List<Indice> indices;

    public Track(String fileName) {
        loadTrack(fileName);
    }

    private void loadTrack(String fileName) {

        try {
            this.trackStr = ParseJsonUtils.readJsonFile(fileName);
        } catch (IOException e) {
            throw new AutoRuntimeException("parse track file failed: " + fileName, e);
        }

        JSONObject jsonTrack = JSON.parseObject(this.trackStr);

        JSONArray indicesArray = jsonTrack.getJSONArray("indices");
        parseIndice(indicesArray);

        JSONArray operationsArray = jsonTrack.getJSONArray("operations");
        parseOperations(operationsArray);

        JSONObject challengeObj = jsonTrack.getJSONObject("challenge");
        parseChallenges(challengeObj);


    }

    private void parseIndice(JSONArray indicesArray) {
        this.indices = new ArrayList<>();
        for (Object indiceObj : indicesArray) {
            JSONObject jsonIndice = (JSONObject) indiceObj;
            String index = jsonIndice.getString("index");
            String type = jsonIndice.getString("type");
            String mapping = jsonIndice.getString("mapping");
            String documents = jsonIndice.getString("documents");

            indices.add(new Indice(index, type, mapping, documents));
        }
    }

    private void parseOperations(JSONArray operationsArray) {
        this.operationMap = new HashMap<>();
        for (Object operationObj : operationsArray) {
            JSONObject jsonOperation = (JSONObject) operationObj;
            String name = jsonOperation.getString("name");
            String operationType = jsonOperation.getString("operation-type");
            String index = jsonOperation.getString("index");
            String type = jsonOperation.getString("type");
            if ("search".equals(operationType)) {
                String body = jsonOperation.getString("body");
                operationMap.put(name, new Operation(name, operationType, index, type, body));
            } else if ("index".equals(operationType)) {
                String bulkSize = jsonOperation.getString("bulk-size");
                String documents = jsonOperation.getString("documents");
                String mapping = jsonOperation.getString("mapping");
                operationMap.put(name, new Operation(name, operationType, index, type,
                        mapping, documents, Integer.parseInt(bulkSize)));
            }
        }
    }

    private void parseChallenges(JSONObject jsonChallenges) {

        String name = jsonChallenges.getString("name");
        JSONArray scheduleArray = jsonChallenges.getJSONArray("schedule");

        ArrayList<Schedule> schedules = new ArrayList<>();
        this.challenge = new Challenge(name, schedules);

        for (Object scheduleObj : scheduleArray) {
            JSONObject jsonSchedule = (JSONObject) scheduleObj;
            String operation = jsonSchedule.getString("operation");
            int iterations = jsonSchedule.getIntValue("iterations");

            schedules.add(new Schedule(1, iterations, operation));
        }


    }

    public String getTrackStr() {
        return trackStr;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Map<String, Operation> getOperationMap() {
        return operationMap;
    }

    public List<Indice> getIndices() {
        return indices;
    }
}

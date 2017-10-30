package org.simon.autoet.trackServer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simon.autoet.util.ParseJsonUtil;

/**
 * 定义本次测试
 * @author simon
 * @since 2017/10/28 12:45
 * @version V1.0
 */
public class Track {
    private String track;
    private Challenge challenge;
    private HashMap<String, Operation> operationMap;
    private ArrayList<Indice> indices;
    private static final Log LOG = LogFactory.getLog(Track.class);

    public Track(String fileName) {
        loadTrack(fileName);
    }

    private void loadTrack(String fileName) {
        try {
            this.track = ParseJsonUtil.readJsonFile(fileName);

            JSONObject jsonTrack = JSON.parseObject(this.track);

            JSONArray indicesArray = jsonTrack.getJSONArray("indices");
            parseIndice(indicesArray);

            JSONArray operationsArray = jsonTrack.getJSONArray("operations");
            parseOperations(operationsArray);

            JSONObject challengeObj = jsonTrack.getJSONObject("challenge");
            parseChallenges(challengeObj);

        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
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
            if (operationType.equals("search")) {
                String body = jsonOperation.getString("body");
                operationMap.put(name, new Operation(name, operationType, index, type, body));
            } else if (operationType.equals("index")) {
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
        this.challenge = new Challenge(name,schedules);

        for (Object scheduleObj : scheduleArray) {
            JSONObject jsonSchedule = (JSONObject) scheduleObj;
            String operation = jsonSchedule.getString("operation");
            int iterations = jsonSchedule.getIntValue("iterations");

            schedules.add(new Schedule(1, iterations, operation));
        }


    }

    public String getTrack() {
        return track;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public HashMap<String, Operation> getOperationMap() {
        return operationMap;
    }

    public ArrayList<Indice> getIndices() {
        return indices;
    }
}

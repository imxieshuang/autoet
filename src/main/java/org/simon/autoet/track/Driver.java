package org.simon.autoet.track;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simon.autoet.config.Config;
import org.simon.autoet.esServer.EsServer;
import org.simon.autoet.esServer.EsServerImpl;
import org.simon.autoet.source.DataSource;
import org.simon.autoet.source.FileSource;
import org.simon.autoet.util.ParseJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于执行挑战
 * @author simon
 * @since 2017/10/28 12:43
 * @version V1.0
 */
public class Driver {
    private Track track;
    private EsServer esServer;
    private Config config;
    private static final Logger LOGGER = LoggerFactory.getLogger(EsServerImpl.class);


    public Driver(Track track, EsServer esServer, Config config) {
        this.track = track;
        this.esServer = esServer;
        this.config = config;
    }

    public Map<String, Result> run() {
        Challenge challenge = this.track.getChallenge();
        List<Indice> indices = this.track.getIndices();
        Map<String, Operation> operationMap = this.track.getOperationMap();
        String trackName = this.track.getTrackStr();

        ArrayList<Schedule> schedules = challenge.getSchedules();
        DataSource fileSource = new FileSource(esServer);

        runIndices(indices, fileSource);

        Map<String, Result> resultMap = runChallenge(operationMap, schedules, fileSource);
        deleteIndex();

        LOGGER.info("track complete " + trackName);
        return resultMap;
    }

    private Map<String, Result> runChallenge(Map<String, Operation> operationMap, List<Schedule> schedules, DataSource fileSource) {
        Map<String, Result> resultMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            Operation operation = operationMap.get(schedule.getOperation());
            if ("search".equals(operation.getOperationType())) {
                Result result = esServer.query(operation.getIndex(), operation.getType(), operation.getBody());
                for (int i = 0; i < schedule.getIterations() - 1; i++) {
                    result.avg(esServer.query(operation.getIndex(), operation.getType(), operation.getBody()));
                }
                LOGGER.info("operation complete " + operation.getName());
                resultMap.put(operation.getName(), result);
            } else if ("index".equals(operation.getOperationType())) {
                String mappingFile = config.getMappingsDir() + File.separator + operation.getMapping();
                String documentFile = config.getDataDir() + File.separator + operation.getDocuments();
                try {
                    String mapping = ParseJsonUtils.readJsonFile(mappingFile);

                    esServer.createIndex(operation.getIndex(), mapping);

                    Result result = fileSource.insertEs(operation.getIndex(),
                            operation.getType(), operation.getBulkSize(), documentFile);
                    resultMap.put(operation.getName(), result);
                    LOGGER.info("operation complete " + operation.getName());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return resultMap;
    }

    private void runIndices(List<Indice> indices, DataSource fileSource) {
        for (Indice indice : indices) {
            String mappingFile = config.getMappingsDir() + File.separator + indice.getMapping();
            String documentFile = config.getDataDir() + File.separator + indice.getDocuments();
            try {
                String mapping = ParseJsonUtils.readJsonFile(mappingFile);
                Boolean indexBoolean = esServer.createIndex(indice.getIndex(), mapping);
                if (indexBoolean) {
                    fileSource.insertEs(indice.getIndex(), indice.getType(), 2000, documentFile);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void deleteIndex() {
        List<String> createIndices = esServer.getCreateIndices();
        for (String index : createIndices) {
            Boolean deleteIndex = esServer.deleteIndex(index);
            if (deleteIndex)
                LOGGER.info("delete " + index + " success");
        }
    }
}

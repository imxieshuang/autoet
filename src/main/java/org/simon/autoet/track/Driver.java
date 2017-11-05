package org.simon.autoet.track;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simon.autoet.config.Config;
import org.simon.autoet.elasticsearch.EsServer;
import org.simon.autoet.exception.AutoRuntimeException;
import org.simon.autoet.source.DataSource;
import org.simon.autoet.source.FileSource;
import org.simon.autoet.util.ParseJsonUtils;


/**
 * 用于执行挑战
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:43
 */
public class Driver {
    private Track track;
    private EsServer esServer;
    private Config config;
    private static final Logger LOGGER = LogManager.getLogger(Driver.class);


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

        List<Schedule> schedules = challenge.getSchedules();
        DataSource fileSource = new FileSource(esServer);

        runIndices(indices, fileSource);

        Map<String, Result> resultMap = runChallenge(operationMap, schedules, fileSource);
        if (challenge.getAutoManaged()) {
            deleteIndex();
        }

        LOGGER.info("track complete " + trackName);
        return resultMap;
    }

    private Map<String, Result> runChallenge(Map<String, Operation> operationMap, List<Schedule> schedules, DataSource fileSource) {
        Map<String, Result> resultMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            Operation operation = operationMap.get(schedule.getOperation());
            if (OperationType.SEARCH.equals(operation.getOperationType())) {
                Result result = esServer.query(operation.getIndex(), operation.getType(), operation.getBody());
                for (int i = 0; i < schedule.getIterations() - 1; i++) {
                    Result targetResult = esServer.query(operation.getIndex(), operation.getType(), operation.getBody());
                    result.avg(targetResult);
                    result.minResult(targetResult);
                    result.maxResult(targetResult);
                }
                LOGGER.info("operation complete " + operation.getName());
                resultMap.put(operation.getName(), result);
            } else if (OperationType.INDEX.equals(operation.getOperationType())) {
                String mappingFile = config.getMappingsDir() + File.separator + operation.getMapping();
                String documentFile = config.getDataDir() + File.separator + operation.getDocuments();

                String mapping = null;
                try {
                    mapping = ParseJsonUtils.readJsonFile(mappingFile);
                } catch (IOException e) {
                    throw new AutoRuntimeException("parse mapping failed", e);
                }

                if (esServer.createIndex(operation.getIndex(), mapping)) {
                    Result result = fileSource.insertEs(operation.getIndex(),
                            operation.getType(), operation.getBulkSize(), documentFile);
                    resultMap.put(operation.getName(), result);
                } else {
                    throw new AutoRuntimeException("create index failed: " + operation.getIndex());
                }
                LOGGER.info("operation complete " + operation.getName());

            }
        }
        return resultMap;
    }

    private void runIndices(List<Indice> indices, DataSource fileSource) {
        for (Indice indice : indices) {
            String mappingFile = config.getMappingsDir() + File.separator + indice.getMapping();
            String documentFile = config.getDataDir() + File.separator + indice.getDocuments();
            String mapping = null;
            try {
                mapping = ParseJsonUtils.readJsonFile(mappingFile);
            } catch (IOException e) {
                throw new AutoRuntimeException("parse mapping failed", e);
            }

            if (esServer.createIndex(indice.getIndex(), mapping)) {
                fileSource.insertEs(indice.getIndex(), indice.getType(), indice.getBulkSize(), documentFile);
            } else {
                throw new AutoRuntimeException("create index failed: " + indice.getIndex());
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

package org.simon.autoet.trackServer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simon.autoet.config.Config;
import org.simon.autoet.esServer.EsServer;
import org.simon.autoet.sourceServer.DataSource;
import org.simon.autoet.sourceServer.FileSource;
import org.simon.autoet.util.ParseJsonUtil;

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
    private static final Log LOG = LogFactory.getLog(Driver.class);

    public Driver(Track track, EsServer esServer, Config config) {
        this.track = track;
        this.esServer = esServer;
        this.config = config;
    }

    public HashMap<String, Result> run() {
        Challenge challenge = this.track.getChallenge();
        ArrayList<Indice> indices = this.track.getIndices();
        HashMap<String, Operation> operationMap = this.track.getOperationMap();
        String trackName = this.track.getTrack();

        ArrayList<Schedule> schedules = challenge.getSchedules();
        DataSource fileSource = new FileSource(esServer);

        for (Indice indice : indices) {
            String mappingFile = config.getMappingsDir() + File.separator + indice.getMapping();
            String documentFile = config.getDataDir() + File.separator + indice.getDocuments();
            try {
                String mapping = ParseJsonUtil.readJsonFile(mappingFile);
                Boolean indexBoolean = esServer.createIndex(indice.getIndex(), mapping);
                if (indexBoolean) {
                    fileSource.insertEs(indice.getIndex(), indice.getType(), 2000, documentFile);
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

        HashMap<String, Result> resultMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            Operation operation = operationMap.get(schedule.getOperation());
            if (operation.getOperationType().equals("search")) {
                Result result = esServer.query(operation.getIndex(), operation.getType(), operation.getBody());
                for (int i = 0; i < schedule.getIterations() - 1; i++) {
                    result.avg(esServer.query(operation.getIndex(), operation.getType(), operation.getBody()));
                }
                LOG.info("operation complete " + operation.getName());
                resultMap.put(operation.getName(), result);
            } else if (operation.getOperationType().equals("index")) {
                String mappingFile = config.getMappingsDir() + File.separator + operation.getMapping();
                String documentFile = config.getDataDir() + File.separator + operation.getDocuments();
                try {
                    String mapping = ParseJsonUtil.readJsonFile(mappingFile);
                    Boolean indexExist = esServer.existType(operation.getIndex(), operation.getType());
                    if (!indexExist) {
                        Boolean newIndexExist = esServer.createIndex(operation.getIndex(), mapping);
                        if (!newIndexExist)
                            break;
                    }
                    Result result = fileSource.insertEs(operation.getIndex(),
                            operation.getType(), operation.getBulkSize(), documentFile);
                    resultMap.put(operation.getName(), result);
                    LOG.info("operation complete " + operation.getName());
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
        }
        deleteIndex();

        LOG.info("track complete " + trackName);
        return resultMap;
    }

    private void deleteIndex() {
        List<String> createIndices = esServer.getCreateIndices();
        for (String index : createIndices) {
            Boolean deleteIndex = esServer.deleteIndex(index);
            if (deleteIndex)
                LOG.info("delete " + index + " success");
        }
    }
}

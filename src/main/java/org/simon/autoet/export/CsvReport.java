package org.simon.autoet.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simon.autoet.trackServer.Result;

/**
 * Created by Administrator on 2017/10/25.
 */
public class CsvReport implements Report {
    private static final Log LOG = LogFactory.getLog(CsvReport.class);

    public void wiriteCsv(HashMap<String, Result> resultMap, String fileName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            for (Map.Entry<String, Result> resultEntry : resultMap.entrySet()) {
                String operationName = resultEntry.getKey();
                Result result = resultEntry.getValue();
                double throughout = result.getThroughout();
                long took = result.getTook();
                double errorRate = result.getErrorRate();
                String lines = "Throughput," + operationName + "," + throughout + "ops/s" + "\n" +
                        "consume time," + operationName + "," + took + "ms" + "\n" +
                        "error rate," + operationName + "," + errorRate + "%" + "\n";
                writer.append(lines);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }


    }
}

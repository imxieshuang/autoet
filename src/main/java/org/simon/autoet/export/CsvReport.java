package org.simon.autoet.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simon.autoet.track.OperationType;
import org.simon.autoet.track.Result;

/**
 * 以csv格式输出结果
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:34
 */
public class CsvReport implements Report {
    private static final Logger LOGGER = LogManager.getLogger(CsvReport.class);

    public void wiriteCsv(Map<String, Result> resultMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, Result> resultEntry : resultMap.entrySet()) {
                String operationName = resultEntry.getKey();
                Result result = resultEntry.getValue();
                double throughout = result.getThroughout();
                double minThroughout = result.getMinThroughout();
                double maxThroughout = result.getMaxThroughout();
                long took = result.getTook();
                double errorRate = result.getErrorRate();
                String unit = "s/ops";
                if (result.getType() != null && OperationType.INDEX.equals(result.getType())) {
                    unit = "doc/s";
                }
                String lines = "Min throughput," + operationName + "," + minThroughout + unit + "\n" +
                        "Median throughput," + operationName + "," + throughout + unit + "\n" +
                        "Max throughput," + operationName + "," + maxThroughout + unit + "\n" +
                        "consume time," + operationName + "," + took + "ms" + "\n" +
                        "error rate," + operationName + "," + errorRate + "%" + "\n";
                writer.append(lines);
            }
        } catch (IOException e) {
            LOGGER.error("write csv report failed", e);
        }
    }
}

package org.simon.autoet.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.simon.autoet.track.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 以csv格式输出结果
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:34
 */
public class CsvReport implements Report {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReport.class);

    public void wiriteCsv(Map<String, Result> resultMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
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
            LOGGER.error("write csv report failed", e);
        }
    }
}

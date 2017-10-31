package org.simon.autoet.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.compress.compressors.bzip2.BZip2Utils;
import org.simon.autoet.esServer.EsServer;
import org.simon.autoet.esServer.EsServerImpl;
import org.simon.autoet.track.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取文件导入es
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:42
 */
public class FileSource implements DataSource {
    private EsServer esServer;
    private static final Logger LOGGER = LoggerFactory.getLogger(EsServerImpl.class);

    public FileSource(EsServer esServer) {
        this.esServer = esServer;
    }

    private BufferedReader readData(String fileName) throws IOException {
        return new BufferedReader(new FileReader(new File(fileName)));
    }

    private BufferedReader readBzip2File(String fileName) throws Exception {
        boolean compressedFilename = BZip2Utils.isCompressedFilename(fileName);
        if (compressedFilename) {
            String uncompressedFileName = BZip2Utils.getUncompressedFilename(fileName);
            return readData(uncompressedFileName);
        }
        return readData(fileName);
    }

    @Override
    public Result insertEs(String index, String type, final int bulkSize, String fileName) {
        BufferedReader reader = null;
        Result result = new Result();
        try {
            reader = readBzip2File(fileName);
            String head = "{\"index\":{\"_index\":\"" + index + "\",\"_type\":\"" + type + "\"}}\n";

            String line;
            StringBuilder source = new StringBuilder();
            int increaseBulk = 1;
            while ((line = reader.readLine()) != null) {
                String temp = head + line + "\n";
                source.append(temp);
                increaseBulk++;

                if (increaseBulk % bulkSize == 0) {
                    result.avg(esServer.indexBulk(source.toString()));
                    source = new StringBuilder();
                    LOGGER.info("insert elasticsearch document count: " + increaseBulk);
                }
            }

            result.avg(esServer.indexBulk(source.toString()));
            LOGGER.info("insert elasticsearch document count: " + increaseBulk);
            source = new StringBuilder();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return result;

    }
}

package org.simon.autoet.source;

import com.google.common.base.Strings;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simon.autoet.elasticsearch.EsServer;
import org.simon.autoet.track.Result;


/**
 * 读取文件导入es
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:42
 */
public class FileSource implements DataSource {
    private EsServer esServer;
    private static final Logger LOGGER = LogManager.getLogger(FileSource.class);

    public FileSource(EsServer esServer) {
        this.esServer = esServer;
    }

    private BufferedReader readData(String fileName) throws IOException {
        return new BufferedReader(new FileReader(new File(fileName)));
    }

    private BufferedReader readBzip2File(String fileName) throws IOException {
        boolean compressedFilename = BZip2Utils.isCompressedFilename(fileName);
        if (compressedFilename) {
            String uncompressedFileName = BZip2Utils.getUncompressedFilename(fileName);
            if (!Files.exists(Paths.get(uncompressedFileName))) {
                decompressBzip2(fileName, uncompressedFileName);
            }
            return readData(uncompressedFileName);
        }
        return readData(fileName);
    }

    private void decompressBzip2(String compressFileName, String unCompressFileName) {
        OutputStream out = null;
        BZip2CompressorInputStream bzin = null;
        try {
            InputStream fin = Files.newInputStream(Paths.get(compressFileName));
            BufferedInputStream in = new BufferedInputStream(fin);
            out = Files.newOutputStream(Paths.get(unCompressFileName));
            bzin = new BZip2CompressorInputStream(in);
            final byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = bzin.read(buffer))) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            LOGGER.error("compress bzip2 file failed: " + compressFileName, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("close write uncompress file failed: " + unCompressFileName, e);
                }
            }
            if (bzin != null) {
                try {
                    bzin.close();
                } catch (IOException e) {
                    LOGGER.error("close read compress file failed: " + compressFileName, e);
                }
            }
        }
    }

    @Override
    public Result insertEs(String index, String type, final int bulkSize, String fileName) {
        Result result = new Result();
        try (BufferedReader reader = readBzip2File(fileName)) {
            String head = "{\"index\":{\"_index\":\"" + index + "\",\"_type\":\"" + type + "\"}}\n";

            String line;
            StringBuilder source = new StringBuilder();
            int increaseBulk = 0;
            while ((line = reader.readLine()) != null) {
                String temp = head + line + "\n";
                source.append(temp);
                increaseBulk++;

                if (increaseBulk % bulkSize == 0) {
                    Result targetResult = esServer.indexBulk(source.toString());
                    result.avg(targetResult);
                    result.minResult(targetResult);
                    result.maxResult(targetResult);
                    source = new StringBuilder();
                    LOGGER.info("insert elasticsearch document count: " + increaseBulk);
                }
            }

            if (!Strings.isNullOrEmpty(source.toString())) {
                Result targetResult = esServer.indexBulk(source.toString());
                result.avg(targetResult);
                result.minResult(targetResult);
                result.maxResult(targetResult);
                LOGGER.info("insert elasticsearch document count: " + increaseBulk);

            }
        } catch (Exception e) {
            LOGGER.error("insert document failed", e);
        }
        return result;

    }
}

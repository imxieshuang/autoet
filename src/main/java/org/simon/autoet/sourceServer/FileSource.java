package org.simon.autoet.sourceServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.compress.compressors.bzip2.BZip2Utils;
import org.simon.autoet.esServer.EsServer;
import org.simon.autoet.trackServer.Result;

/**
 * Created by Administrator on 2017/10/25.
 */
public class FileSource extends DataSource {
    private EsServer esServer;

    public FileSource(EsServer esServer) {
        this.esServer = esServer;
    }

    public BufferedReader readData(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        return reader;
        // Stream<String> lines = Files.lines(Paths.get(fileName));
        // return Files.readLines(new File(fileName), Charsets.UTF_8);
    }

    public BufferedReader readBzip2File(String fileName) throws Exception {
        boolean compressedFilename = BZip2Utils.isCompressedFilename(fileName);
        if (compressedFilename) {
            fileName = BZip2Utils.getUncompressedFilename(fileName);
        }
        return readData(fileName);
    }

    public Result insertEs(String index, String type, final int bulkSize, String fileName) throws Exception {
        BufferedReader reader = readBzip2File(fileName);

        String head = "{\"index\":{\"_index\":\"" + index + "\",\"_type\":\"" + type + "\"}}\n";

        String line;
        StringBuilder source = new StringBuilder();
        Result result = new Result();
        int increaseBulk = 1;
        while ((line = reader.readLine()) != null) {
            String temp = head + line + "\n";
            source.append(temp);
            increaseBulk++;
            if (increaseBulk == bulkSize) {
                result.avg(esServer.indexBulk(source.toString()));
                source = new StringBuilder();
                increaseBulk = 1;
            }
        }

        result.avg(esServer.indexBulk(source.toString()));
        source = new StringBuilder();
        increaseBulk = 1;

        reader.close();
        return result;
    }
}

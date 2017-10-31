package org.simon.autoet.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件合并测试
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/31 11:57
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            LOGGER.error("参数不够");
            System.exit(1);
        }

        File dir = new File(args[0]);

        if (dir.isDirectory()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))) {
                readFile(dir, writer);
            } catch (IOException e) {
                LOGGER.error("writer file failed",e);
            }
        }
    }

    private static void readFile(File dir, BufferedWriter writer) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    readFile(file, writer);
                } else {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            writer.append(line);
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        LOGGER.error("read file failed", e);
                    }

                }
            }
        }

    }
}

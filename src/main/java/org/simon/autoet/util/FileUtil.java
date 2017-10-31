package org.simon.autoet.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.simon.autoet.esServer.EsServerImpl;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(EsServerImpl.class);

    private FileUtil() {
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            LOGGER.error("参数不够");
            System.exit(1);
        }

        File dir = new File(args[0]);

        if (dir.isDirectory()) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(args[1]));
                readFile(dir, writer);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
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
                    BufferedReader reader = null;
                    FileReader in = null;
                    try {
                        in = new FileReader(file);
                        reader = new BufferedReader(in);

                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            writer.append(line);
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                LOGGER.error(e.getMessage());
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                LOGGER.error(e.getMessage());
                            }
                        }
                    }
                }
            }
        }

    }
}

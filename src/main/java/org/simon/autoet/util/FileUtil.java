package org.simon.autoet.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件合并测试
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/31 11:57
 */
public class FileUtil {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("参数不够");
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
                        e.printStackTrace();
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
                    try {
                        reader = new BufferedReader(new FileReader(file));

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
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }
}

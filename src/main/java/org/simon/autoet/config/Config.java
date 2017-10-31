package org.simon.autoet.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.simon.autoet.esServer.EsServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置类
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:28
 */
public class Config {
    //auto.data=data
    // auto.log=log
    // auto.track=metrics
    private String dataDir;
    private String logDir;
    private String tracksDir;
    private String mappingsDir;
    private String trackFile;
    private String reportFile;
    private static final Logger LOGGER = LoggerFactory.getLogger(EsServerImpl.class);


    public Config() {
        Properties properties = new Properties();
        String userDir = System.getProperty("user.dir");
        String configFile = userDir + File.separator + "config" + File.separator + "auto_config.properties";
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(Paths.get(configFile));
            properties.load(inputStream);
            this.dataDir = userDir + File.separator + properties.getProperty("auto.data");
            this.logDir = userDir + File.separator + properties.getProperty("auto.log");
            this.tracksDir = userDir + File.separator + properties.getProperty("auto.tracks");
            this.mappingsDir = userDir + File.separator + properties.getProperty("auto.mappings");
            this.trackFile = userDir + File.separator + this.tracksDir + File.separator + "track.json";
            this.reportFile = userDir + File.separator + "report.csv";
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    public String getReportFile() {
        return reportFile;
    }

    public String getTrackFile() {
        return trackFile;
    }

    public String getMappingsDir() {
        return mappingsDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public String getTracksDir() {
        return tracksDir;
    }

    public String getLogDir() {
        return logDir;
    }
}

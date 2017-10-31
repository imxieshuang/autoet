package org.simon.autoet.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private static final Log LOG = LogFactory.getLog(Config.class);

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
            LOG.error(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
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

    public String getLogDir() {
        return logDir;
    }

    public String getTracksDir() {
        return tracksDir;
    }

    public static Log getLOG() {
        return LOG;
    }
}

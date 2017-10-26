package org.simon.autoet.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 2017/10/25.
 */
public class Config {
    //auto.data=data
    // auto.log=log
    // auto.track=metrics
    private String dataDir;
    private String logDir;
    private String tracksDir;
    private String mappingsDir;
    private static final Log LOG = LogFactory.getLog(Config.class);

    public Config() {
        Properties properties = new Properties();
        String userDir = System.getProperty("user.dir");
        String configFile = userDir + File.separator + "config" + File.separator + "auto_config.properties";
        try {
            properties.load(Files.newInputStream(Paths.get(configFile)));
            this.dataDir = userDir + File.separator + properties.getProperty("auto.data");
            this.logDir = userDir + File.separator + properties.getProperty("auto.log");
            this.tracksDir = userDir + File.separator + properties.getProperty("auto.tracks");
            this.mappingsDir = userDir + File.separator + properties.getProperty("auto.mappings");
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
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

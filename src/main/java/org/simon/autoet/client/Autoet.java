package org.simon.autoet.client;

import com.google.common.base.Strings;
import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.Option;
import java.io.File;
import java.util.Map;
import javax.inject.Inject;
import org.simon.autoet.config.Config;
import org.simon.autoet.elasticsearch.EsServer;
import org.simon.autoet.elasticsearch.EsServerImpl;
import org.simon.autoet.export.CsvReport;
import org.simon.autoet.track.Driver;
import org.simon.autoet.track.Result;
import org.simon.autoet.track.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令行定义
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:31
 */
@Command(name = "autoet", description = "auto test es")
public class Autoet {
    @Inject
    public HelpOption helpOption;

    @Option(name = {"--port"}, description = "port")
    public int port = 9200;

    @Option(name = {"--host"}, description = "host")
    public String host = "localhost";

    @Option(name = {"--report-file"}, description = "report file")
    public String reportFile;

    @Option(name = {"--track-file"}, description = "report file")
    public String trackFile;

    private static final Logger LOGGER = LoggerFactory.getLogger(EsServerImpl.class);

    public void run() {
        Config config = new Config();
        trackFile = config.getTrackFile();
        reportFile = config.getReportFile();

        String tracksDir = config.getTracksDir();
        if (Strings.isNullOrEmpty(trackFile)) {
            trackFile = tracksDir + File.separator + trackFile;
        }

        Track track = new Track(trackFile);
        EsServer esServer = new EsServerImpl(host, port);

        Driver driver = new Driver(track, esServer, config);
        Map<String, Result> resultMap = driver.run();
        CsvReport csvReport = new CsvReport();
        csvReport.wiriteCsv(resultMap, reportFile);
        LOGGER.info("run complete");
        esServer.close();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getReportFile() {
        return reportFile;
    }

    public String getTrackFile() {
        return trackFile;
    }
}

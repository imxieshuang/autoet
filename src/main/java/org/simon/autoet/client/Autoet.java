package org.simon.autoet.client;

import com.google.common.base.Strings;
import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.Option;
import java.io.File;
import java.util.HashMap;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simon.autoet.config.Config;
import org.simon.autoet.esServer.EsServerImpl;
import org.simon.autoet.export.CsvReport;
import org.simon.autoet.trackServer.Driver;
import org.simon.autoet.trackServer.Result;
import org.simon.autoet.trackServer.Track;

/**
 * Created by Administrator on 2017/10/26.
 */

@Command(name = "autoet", description = "auto test es")
public class Autoet {
    @Inject
    public HelpOption helpOption;

    @Option(name = {"--port"}, required = true, description = "port")
    public int port = 9200;

    @Option(name = {"--host"}, description = "host")
    public String host = "localhost";

    @Option(name = {"--report-file"}, description = "report file")
    public String reportFile;

    @Option(name = {"--track-file"}, description = "report file")
    public String trackFile;

    private static final Log LOG = LogFactory.getLog(Autoet.class);

    public void run() {
        Config config = new Config();
        String tracksDir = config.getTracksDir();

        if (Strings.isNullOrEmpty(trackFile)){
            trackFile = tracksDir + File.separator + trackFile;
        }

        Track track = new Track(trackFile);
        EsServerImpl esServer = new EsServerImpl(host, port);

        Driver driver = new Driver(track, esServer, config);
        HashMap<String, Result> resultMap = driver.run();
        CsvReport csvReport = new CsvReport();
        csvReport.wiriteCsv(resultMap, reportFile);
        LOG.info("run complete");
        esServer.close();
    }
}

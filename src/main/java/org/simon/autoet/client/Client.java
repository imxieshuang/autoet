package org.simon.autoet.client;

import io.airlift.airline.SingleCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 2017/10/25.
 */
public class Client {
    private static final Log LOG = LogFactory.getLog(Client.class);


    public static void main(String[] args) {
        Autoet autoet = SingleCommand.singleCommand(Autoet.class).parse(args);
        if (autoet.helpOption.showHelpIfRequested()) {
            return;
        }
        autoet.run();
    }
}

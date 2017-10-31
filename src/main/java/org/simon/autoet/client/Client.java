package org.simon.autoet.client;

import io.airlift.airline.SingleCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 程序运行入口类
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:28
 */
public class Client {
    private static final Log LOG = LogFactory.getLog(Client.class);


    public static void main(String[] args) {
        try {
            Autoet autoet = SingleCommand.singleCommand(Autoet.class).parse(args);
            if (autoet.helpOption.showHelpIfRequested()) {
                return;
            }
            autoet.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            LOG.error(e.getMessage());
        }
    }
}

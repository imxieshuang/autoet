package org.simon.autoet.client;

import io.airlift.airline.SingleCommand;
import org.simon.autoet.esServer.EsServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 程序运行入口类
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:28
 */
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsServerImpl.class);

    public static void main(String[] args) {
        try {
            Autoet autoet = SingleCommand.singleCommand(Autoet.class).parse(args);
            if (autoet.helpOption.showHelpIfRequested()) {
                return;
            }
            autoet.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            LOGGER.error(e.getMessage());
        }
    }
}

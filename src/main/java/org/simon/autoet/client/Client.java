package org.simon.autoet.client;

import io.airlift.airline.SingleCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 程序运行入口类
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:28
 */
public class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    private Client() {
    }

    public static void main(String[] args) {
        Autoet autoet = null;
        try {
            autoet = SingleCommand.singleCommand(Autoet.class).parse(args);
        } catch (Exception e) {
            LOGGER.error("parse Command failed", e);
        }
        if (autoet != null) {
            if (autoet.helpOption.showHelpIfRequested()) {
                return;
            }
            autoet.run();
        }

    }
}

package org.simon.autoet.client;

import io.airlift.airline.SingleCommand;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

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

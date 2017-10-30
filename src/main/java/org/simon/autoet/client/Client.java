package org.simon.autoet.client;

import io.airlift.airline.SingleCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 程序运行入口类
 * @author simon
 * @since 2017/10/28 12:28
 * @version V1.0
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

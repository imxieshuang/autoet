package org.simon.autoet;

import java.io.IOException;
import org.junit.Test;
import org.simon.autoet.util.ParseJsonUtils;

/**
 * Created by Administrator on 2017/10/26.
 */
public class UtilTest {
    @Test
    public void readJson() throws IOException {
        String s = ParseJsonUtils.readJsonFile("D:\\input\\track.json");
    }
}

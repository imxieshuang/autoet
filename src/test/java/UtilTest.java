import java.io.IOException;
import org.junit.Test;
import org.simon.autoet.util.ParseJsonUtil;

/**
 * Created by Administrator on 2017/10/26.
 */
public class UtilTest {
    @Test
    public void readJson() throws IOException {
        String s = ParseJsonUtil.readJsonFile("D:\\input\\track.json");
        System.out.println(s);
    }
}

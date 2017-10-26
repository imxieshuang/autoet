import org.junit.Test;
import org.simon.autoet.esServer.EsServerImpl;

/**
 * Created by Administrator on 2017/10/26.
 */
public class ESServerTest {
    @Test
    public void index() {

        String jsonString = "{\"index\":{\"_index\":\"test\",\"_type\":\"type1\"}}\n" +
                "{\"field1\":\"value1\",\"field1\":\"value1\",\"field1\":\"value1\"}\n{\"index\":{\"_index\":\"test\",\"_type\":\"type1\"}}\n" +
                "{\"field1\":\"value1\",\"field1\":\"value1\"}\n";

        EsServerImpl server = new EsServerImpl("localhost", 9202);
        server.indexBulk(jsonString);
    }

    @Test
    public void query() {

        String jsonString = "{\n" +
                "    \"query\": {\n" +
                "        \"match_all\": {}\n" +
                "    }\n" +
                "}";

        EsServerImpl server = new EsServerImpl("localhost", 9202);
        server.query("test","type1",jsonString);
    }


}

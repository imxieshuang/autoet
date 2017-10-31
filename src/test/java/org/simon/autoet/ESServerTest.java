package org.simon.autoet;

import org.junit.Before;
import org.junit.Test;
import org.simon.autoet.esServer.EsServerImpl;

/**
 * Created by Administrator on 2017/10/26.
 */
public class ESServerTest {
    private EsServerImpl server;

    @Before
    public void clientTest(){
        this.server = new EsServerImpl("localhost", 9202);
    }

    @Test
    public void indexTest() {

        String jsonString = "{\"index\":{\"_index\":\"test\",\"_type\":\"type1\"}}\n" +
                "{\"field1\":\"value1\",\"field1\":\"value1\",\"field1\":\"value1\"}\n{\"index\":{\"_index\":\"test\",\"_type\":\"type1\"}}\n" +
                "{\"field1\":\"value1\",\"field1\":\"value1\"}\n";

        server.indexBulk(jsonString);
    }

    @Test
    public void queryTest() {
        String jsonString = "{\n" +
                "    \"query\": {\n" +
                "        \"match_all\": {}\n" +
                "    }\n" +
                "}";
        server.query("test","type1",jsonString);
    }

    @Test
    public void createIndexTest(){
        String jsonString = "{\n" +
                "    \"settings\" : {\n" +
                "        \"number_of_shards\" : 1\n" +
                "    },\n" +
                "    \"mappings\" : {\n" +
                "        \"type\" : {\n" +
                "            \"properties\" : {\n" +
                "                \"field1\" : { \"type\" : \"string\" }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        System.out.println(server.createIndex("createtest",jsonString));
    }

    @Test
    public void deleteIndex(){
        System.out.println(server.deleteIndex("createtest"));
    }

    @Test
    public void existIndex(){
        System.out.println(server.existIndex("test2"));
    }
}

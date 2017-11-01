package org.simon.autoet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.simon.autoet.elasticsearch.EsServerImpl;


/**
 * Created by Administrator on 2017/10/26.
 */
public class ESServerTest {
    private EsServerImpl server;
    private static final Logger LOGGER = LogManager.getLogger(EsServerImpl.class);

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
        Boolean createtest = server.createIndex("createtest", jsonString);
        LOGGER.error(createtest.toString());

    }

    @Test
    public void deleteIndex(){
        server.deleteIndex("createtest");
    }

    @Test
    public void existIndex(){
        server.existIndex("test2");
    }
}

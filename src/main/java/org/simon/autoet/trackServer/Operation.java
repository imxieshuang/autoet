package org.simon.autoet.trackServer;

/**
 * 测试操作项
 * @author simon
 * @since 2017/10/28 12:44
 * @version V1.0
 */
public class Operation {
    private String name;
    private String operationType;
    private String index;
    private String type;
    private String mapping;
    private String documents;
    private String body;
    private int bulkSize;

    public Operation(String name, String operationType, String index, String type, String body) {
        this.name = name;
        this.operationType = operationType;
        this.index = index;
        this.type = type;
        this.body = body;
    }

    public Operation(String name, String operationType, String index, String type, String mapping, String documents, int bulkSize) {
        this.name = name;
        this.operationType = operationType;
        this.index = index;
        this.type = type;
        this.mapping = mapping;
        this.documents = documents;
        this.bulkSize = bulkSize;
    }

    public String getMapping() {
        return mapping;
    }

    public String getDocuments() {
        return documents;
    }

    public String getName() {
        return name;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public int getBulkSize() {
        return bulkSize;
    }
}

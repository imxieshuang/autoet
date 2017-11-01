package org.simon.autoet.track;

/**
 * 定义track数据源，用于测试
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:44
 */
public class Indice {
    private String index;
    private String type;
    private String mapping;
    private String documents;
    private Integer bulkSize;

    public Indice(String index, String type, String mapping, String documents, Integer bulkSize) {
        this.index = index;
        this.type = type;
        this.mapping = mapping;
        this.documents = documents;
        this.bulkSize = bulkSize;
    }

    public Integer getBulkSize() {
        return bulkSize;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getMapping() {
        return mapping;
    }

    public String getDocuments() {
        return documents;
    }
}

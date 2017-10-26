package org.simon.autoet.trackServer;

/**
 * Created by Administrator on 2017/10/25.
 */
public class Indice {
    private String index;
    private String type;
    private String mapping;
    private String documents;

    public Indice(String index, String type, String mapping, String documents) {
        this.index = index;
        this.type = type;
        this.mapping = mapping;
        this.documents = documents;
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

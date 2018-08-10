package net.cnki.odatax.model;

/**
 * @author hudianwei
 * @date 2018/8/2 15:29
 */
public class DataProperty {

    private String name = null;

    private String caption = null;

    private String mapping = null;

    private String filter = null;

    private String expession = null;

    private String dictionary = null;
    private String groupCodeName = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getExpession() {
        return expession;
    }

    public void setExpession(String expession) {
        this.expession = expession;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public String getGroupCodeName() {
        return groupCodeName;
    }

    public void setGroupCodeName(String groupCodeName) {
        this.groupCodeName = groupCodeName;
    }
}


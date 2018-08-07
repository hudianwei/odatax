package net.cnki.odatax.model;

/**
 * @author hudianwei
 * @date 2018/8/2 15:28
 */

import java.util.List;

/**
 * 数据模型
 *
 */
public class DataModel {

    private String domain = null;

    private String name = null;

    private String caption = null;

    private String description = null;

    private String dataSource = null;

    private String dataView = null;

    private List<DataFilter> dataFilter = null;

    private List<DataProperty> properties = null;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataView() {
        return dataView;
    }

    public void setDataView(String dataView) {
        this.dataView = dataView;
    }

    public List<DataFilter> getDataFilter() {
        return dataFilter;
    }

    public void setDataFilter(List<DataFilter> dataFilter) {
        this.dataFilter = dataFilter;
    }

    public List<DataProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<DataProperty> properties) {
        this.properties = properties;
    }

}

package net.cnki.odatax.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hudianwei
 * @date 2018/8/2 14:26
 */
public class DataStore {
    private List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
    private Long totalCount = 0L;
    private Integer errorCode = null;
    private String errorMessage = null;
    private String description = null;
    private BigDecimal processingTime = new BigDecimal(0);

    public List<Map<String, Object>> getRecords() {
        return records;
    }

    public void setRecords(List<Map<String, Object>> records) {
        this.records = records;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(BigDecimal processingTime) {
        this.processingTime = processingTime;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "DataStore [records=" + records + ", totalCount=" + totalCount
                + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
                + ", description=" + description + ", ProcessingTime="
                + processingTime + "]";
    }
}

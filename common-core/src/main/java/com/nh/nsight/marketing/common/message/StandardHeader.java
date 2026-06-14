package com.nh.nsight.marketing.common.message;

import jakarta.validation.constraints.NotBlank;

public class StandardHeader {

    @NotBlank
    private String systemId;

    @NotBlank
    private String businessCode;

    @NotBlank
    private String serviceId;

    @NotBlank
    private String transactionCode;

    @NotBlank
    private String processingType;

    private String guid;
    private String traceId;
    private String channelId;
    private String userId;
    private String branchId;
    private String centerId;
    private String clientIp;
    private String requestTime;
    private String responseTime;
    private String transactionIntime;
    private String transactionOuttime;
    private String systemDate;
    private String bizDate;
    private String apId;

    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }
    public String getBusinessCode() { return businessCode; }
    public void setBusinessCode(String businessCode) { this.businessCode = businessCode; }
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getTransactionCode() { return transactionCode; }
    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }
    public String getProcessingType() { return processingType; }
    public void setProcessingType(String processingType) { this.processingType = processingType; }
    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    public String getCenterId() { return centerId; }
    public void setCenterId(String centerId) { this.centerId = centerId; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
    public String getRequestTime() { return requestTime; }
    public void setRequestTime(String requestTime) { this.requestTime = requestTime; }
    public String getResponseTime() { return responseTime; }
    public void setResponseTime(String responseTime) { this.responseTime = responseTime; }
    public String getTransactionIntime() { return transactionIntime; }
    public void setTransactionIntime(String transactionIntime) { this.transactionIntime = transactionIntime; }
    public String getTransactionOuttime() { return transactionOuttime; }
    public void setTransactionOuttime(String transactionOuttime) { this.transactionOuttime = transactionOuttime; }
    public String getSystemDate() { return systemDate; }
    public void setSystemDate(String systemDate) { this.systemDate = systemDate; }
    public String getBizDate() { return bizDate; }
    public void setBizDate(String bizDate) { this.bizDate = bizDate; }
    public String getApId() { return apId; }
    public void setApId(String apId) { this.apId = apId; }

    public StandardHeader copy() {
        StandardHeader copy = new StandardHeader();
        copy.setSystemId(systemId);
        copy.setBusinessCode(businessCode);
        copy.setServiceId(serviceId);
        copy.setTransactionCode(transactionCode);
        copy.setProcessingType(processingType);
        copy.setGuid(guid);
        copy.setTraceId(traceId);
        copy.setChannelId(channelId);
        copy.setUserId(userId);
        copy.setBranchId(branchId);
        copy.setCenterId(centerId);
        copy.setClientIp(clientIp);
        copy.setRequestTime(requestTime);
        copy.setResponseTime(responseTime);
        copy.setTransactionIntime(transactionIntime);
        copy.setTransactionOuttime(transactionOuttime);
        copy.setSystemDate(systemDate);
        copy.setBizDate(bizDate);
        copy.setApId(apId);
        return copy;
    }
}

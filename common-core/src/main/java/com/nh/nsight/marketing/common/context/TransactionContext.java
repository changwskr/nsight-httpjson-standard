package com.nh.nsight.marketing.common.context;

import com.nh.nsight.marketing.common.message.StandardHeader;
import java.time.Instant;

public class TransactionContext {
    private final StandardHeader header;
    private final Instant startTime;
    private String pathBusinessCode;
    private StandardHeader requestHeader;

    public TransactionContext(StandardHeader header, Instant startTime) {
        this(header, null, startTime);
    }

    public TransactionContext(StandardHeader header, StandardHeader requestHeader, Instant startTime) {
        this.header = header;
        this.requestHeader = requestHeader;
        this.startTime = startTime;
    }

    public StandardHeader getHeader() { return header; }
    public Instant getStartTime() { return startTime; }
    public String getPathBusinessCode() { return pathBusinessCode; }
    public void setPathBusinessCode(String pathBusinessCode) { this.pathBusinessCode = pathBusinessCode; }
    public StandardHeader getRequestHeader() { return requestHeader; }
    public void setRequestHeader(StandardHeader requestHeader) { this.requestHeader = requestHeader; }
    public String getGuid() { return header.getGuid(); }
    public String getTraceId() { return header.getTraceId(); }
    public String getBusinessCode() { return header.getBusinessCode(); }
    public String getServiceId() { return header.getServiceId(); }
    public String getTransactionCode() { return header.getTransactionCode(); }
}

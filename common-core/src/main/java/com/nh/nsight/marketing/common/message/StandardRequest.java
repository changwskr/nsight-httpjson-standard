package com.nh.nsight.marketing.common.message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class StandardRequest<T> {

    @Valid
    @NotNull
    private StandardHeader header;

    private T body;

    public StandardHeader getHeader() { return header; }
    public void setHeader(StandardHeader header) { this.header = header; }
    public T getBody() { return body; }
    public void setBody(T body) { this.body = body; }
}

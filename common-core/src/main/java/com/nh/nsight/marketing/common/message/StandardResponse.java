package com.nh.nsight.marketing.common.message;

public class StandardResponse<T> {
    private StandardHeader header;
    private Result result;
    private T body;

    public static <T> StandardResponse<T> of(StandardHeader header, Result result, T body) {
        StandardResponse<T> response = new StandardResponse<>();
        response.setHeader(header);
        response.setResult(result);
        response.setBody(body);
        return response;
    }

    public StandardHeader getHeader() { return header; }
    public void setHeader(StandardHeader header) { this.header = header; }
    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }
    public T getBody() { return body; }
    public void setBody(T body) { this.body = body; }
}

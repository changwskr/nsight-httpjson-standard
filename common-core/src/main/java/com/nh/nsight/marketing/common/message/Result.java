package com.nh.nsight.marketing.common.message;

public class Result {
    private String status;
    private String resultCode;
    private String messageCode;
    private String message;
    private String errorCode;
    private String errorMessage;
    private long elapsedTimeMs;

    public static Result success(long elapsedTimeMs) {
        Result result = new Result();
        result.setStatus(ResultStatus.SUCCESS.name());
        result.setResultCode("S0000");
        result.setMessageCode("MSG-COM-0001");
        result.setMessage("정상 처리되었습니다.");
        result.setElapsedTimeMs(elapsedTimeMs);
        return result;
    }

    public static Result fail(String errorCode, String message, long elapsedTimeMs) {
        Result result = new Result();
        result.setStatus(ResultStatus.FAIL.name());
        result.setResultCode("E0001");
        result.setMessageCode("MSG-COM-9999");
        result.setMessage(message);
        result.setErrorCode(errorCode);
        result.setErrorMessage(message);
        result.setElapsedTimeMs(elapsedTimeMs);
        return result;
    }

    public static Result error(String errorCode, String message, long elapsedTimeMs) {
        Result result = fail(errorCode, message, elapsedTimeMs);
        result.setStatus(ResultStatus.ERROR.name());
        return result;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResultCode() { return resultCode; }
    public void setResultCode(String resultCode) { this.resultCode = resultCode; }
    public String getMessageCode() { return messageCode; }
    public void setMessageCode(String messageCode) { this.messageCode = messageCode; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public long getElapsedTimeMs() { return elapsedTimeMs; }
    public void setElapsedTimeMs(long elapsedTimeMs) { this.elapsedTimeMs = elapsedTimeMs; }
}

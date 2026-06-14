package com.nh.nsight.marketing.common.web.validation;

import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.common.message.ProcessingType;
import com.nh.nsight.marketing.common.util.GuidUtil;
import com.nh.nsight.marketing.common.web.config.BusinessModuleProperties;
import org.springframework.stereotype.Component;

@Component
public class StandardHeaderValidator {
    private final BusinessModuleProperties properties;

    public StandardHeaderValidator(BusinessModuleProperties properties) {
        this.properties = properties;
    }

    public void validateAndNormalize(StandardHeader header, String pathBusinessCode) {
        if (header == null) {
            throw new BusinessException("E-COM-HDR-0001", "표준 전문 header가 없습니다.");
        }
        if (isBlank(header.getSystemId())) {
            header.setSystemId(properties.getSystemId());
        }
        if (isBlank(header.getBusinessCode())) {
            header.setBusinessCode(firstNotBlank(pathBusinessCode, properties.getBusinessCode()));
        }
        if (isBlank(header.getBusinessCode())) {
            throw new BusinessException("E-COM-HDR-0002", "businessCode는 필수입니다.");
        }
        if (!isBlank(pathBusinessCode) && !pathBusinessCode.equalsIgnoreCase(header.getBusinessCode())) {
            throw new BusinessException("E-COM-HDR-0003", "URL 업무코드와 전문 업무코드가 다릅니다.");
        }
        if (!isBlank(properties.getBusinessCode()) && !properties.getBusinessCode().equalsIgnoreCase(header.getBusinessCode())) {
            throw new BusinessException("E-COM-HDR-0004", "모듈 업무코드와 전문 업무코드가 다릅니다.");
        }
        required(header.getServiceId(), "serviceId", "E-COM-HDR-0005");
        required(header.getTransactionCode(), "transactionCode", "E-COM-HDR-0006");
        required(header.getProcessingType(), "processingType", "E-COM-HDR-0007");
        try {
            ProcessingType.valueOf(header.getProcessingType());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("E-COM-HDR-0008", "허용되지 않은 processingType입니다: " + header.getProcessingType());
        }
        if (isBlank(header.getGuid())) {
            header.setGuid(GuidUtil.newGuid());
        }
        if (isBlank(header.getTraceId())) {
            header.setTraceId(GuidUtil.newTraceId());
        }
        if (isBlank(header.getApId())) {
            header.setApId(properties.getApId());
        }
    }

    private void required(String value, String name, String code) {
        if (isBlank(value)) {
            throw new BusinessException(code, name + "는 필수입니다.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String firstNotBlank(String first, String second) {
        if (!isBlank(first)) return first.toUpperCase();
        if (!isBlank(second)) return second.toUpperCase();
        return null;
    }
}

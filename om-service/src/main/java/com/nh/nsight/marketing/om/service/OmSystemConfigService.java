package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import com.nh.nsight.marketing.om.support.OmBodySupport;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmSystemConfigService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;

    public OmSystemConfigService(OmOperationRule rule, OmOperationDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>();
        String category = OmBodySupport.stringValue(body, "configCategory");
        if (category != null) {
            criteria.put("configCategory", category);
        }

        List<Map<String, Object>> rows = dao.searchSystemConfigs(criteria);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "환경설정 조회");
        result.put("readOnlyNotice", "application.yml·Hikari·MyBatis 등은 조회만 제공하며 화면에서 직접 수정할 수 없습니다.");
        result.put("rows", rows);
        result.put("totalCount", rows.size());
        return result;
    }
}

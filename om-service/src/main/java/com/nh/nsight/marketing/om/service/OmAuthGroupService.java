package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import com.nh.nsight.marketing.om.support.OmBodySupport;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmAuthGroupService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;

    public OmAuthGroupService(OmOperationRule rule, OmOperationDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>();
        String authGroupId = OmBodySupport.stringValue(body, "authGroupId");
        if (authGroupId != null) {
            criteria.put("authGroupId", authGroupId);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "권한그룹 관리");
        result.put("rows", dao.searchAuthGroups(criteria));
        return result;
    }
}

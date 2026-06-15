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
public class OmMenuService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;

    public OmMenuService(OmOperationRule rule, OmOperationDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>();
        String useYn = OmBodySupport.stringValue(body, "useYn");
        if (useYn != null) {
            criteria.put("useYn", useYn);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "메뉴 관리");
        result.put("rows", dao.searchMenus(criteria));
        return result;
    }
}

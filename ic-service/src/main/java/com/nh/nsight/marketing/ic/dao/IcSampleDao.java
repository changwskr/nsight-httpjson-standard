package com.nh.nsight.marketing.ic.dao;

import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class IcSampleDao {
    public Map<String, Object> selectSample(TransactionContext context, Map<String, Object> body) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("sampleId", "IC-SAMPLE-0001");
        data.put("sampleName", "Integration Customer sample transaction");
        data.put("input", body);
        data.put("note", "실제 업무에서는 이 위치에서 MyBatis Mapper를 호출한다.");
        return data;
    }
}

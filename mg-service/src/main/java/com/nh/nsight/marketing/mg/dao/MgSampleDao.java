package com.nh.nsight.marketing.mg.dao;

import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MgSampleDao {
    public Map<String, Object> selectSample(TransactionContext context, Map<String, Object> body) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("sampleId", "MG-SAMPLE-0001");
        data.put("sampleName", "Message sample transaction");
        data.put("input", body);
        data.put("note", "실제 업무에서는 이 위치에서 MyBatis Mapper를 호출한다.");
        return data;
    }
}

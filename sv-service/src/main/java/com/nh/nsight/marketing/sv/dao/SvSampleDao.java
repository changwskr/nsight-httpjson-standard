package com.nh.nsight.marketing.sv.dao;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.sv.mapper.SvSampleMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class SvSampleDao {
    private final SvSampleMapper mapper;

    public SvSampleDao(SvSampleMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, Object> selectSample(TransactionContext context, Map<String, Object> body) {
        System.out.println("====================================================================[SvSampleDao.selectSample] start");
        System.out.println("context: " + context);
        System.out.println("body: " + body);
        Map<String, Object> parameter = new LinkedHashMap<>();
        parameter.put("input", body);
        Map<String, Object> data = new LinkedHashMap<>(mapper.selectSampleWithLogging(parameter));
        data.put("sampleId", "SV-SAMPLE-0001");
        data.put("sampleName", "Single View sample transaction");
        data.put("input", body);
        data.put("note", "실제 업무에서는 이 위치에서 MyBatis Mapper를 호출한다.");
        System.out.println("result: " + data);
        System.out.println("====================================================================[SvSampleDao.selectSample] end");
        return data;
    }
}

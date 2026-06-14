package com.nh.nsight.marketing.common.web.processor;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.message.StandardRequest;
import com.nh.nsight.marketing.common.message.StandardResponse;
import com.nh.nsight.marketing.common.web.dispatch.TransactionDispatcher;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TCF {
    private final STF stf;
    private final TransactionDispatcher transactionDispatcher;
    private final ETF etf;

    public TCF(STF stf, TransactionDispatcher transactionDispatcher, ETF etf) {
        this.stf = stf;
        this.transactionDispatcher = transactionDispatcher;
        this.etf = etf;
    }

    public StandardResponse<Object> process(String pathBusinessCode, StandardRequest<Map<String, Object>> request) {
        System.out.println("\n ======================================================================[TCF.process] start");
        System.out.println("pathBusinessCode: " + pathBusinessCode);
        System.out.println("request: " + request);

        System.out.println("\n ======================================================================[STF.preProcess] start");
        TransactionContext context = stf.preProcess(pathBusinessCode, request);
        System.out.println("\n ======================================================================[STF.preProcess] end");

        try {
            System.out.println("\n ======================================================================[TransactionDispatcher] start");
            Object body = transactionDispatcher.dispatch(context, request.getBody());
            System.out.println("\n ======================================================================[TransactionDispatcher] end");

            System.out.println("\n ======================================================================[ETF] start");
            StandardResponse<Object> response = etf.success(context, body);
            System.out.println("response: " + response);
            System.out.println("\n ======================================================================[ETF] end");
            System.out.println("\n ======================================================================[TCF.process] end");
            return response;
        } catch (BusinessException e) {
            StandardResponse<Object> response = etf.businessFail(context, e);
            System.out.println("\n ======================================================================[ETF] end");
            System.out.println("response: " + response);
            System.out.println("\n ======================================================================[TCF.process] end");
            return response;
        } catch (Exception e) {
            StandardResponse<Object> response = etf.systemError(context, e);
            System.out.println("response: " + response);
            System.out.println("\n ======================================================================[ETF] end");
            System.out.println("\n ======================================================================[TCF.process] end");
            return response;
        }
    }
}

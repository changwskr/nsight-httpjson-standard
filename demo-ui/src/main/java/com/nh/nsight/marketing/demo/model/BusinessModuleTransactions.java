package com.nh.nsight.marketing.demo.model;

import java.util.List;

public record BusinessModuleTransactions(
        String code,
        String name,
        String group,
        String contextPath,
        int localPort,
        List<BusinessTransactionInfo> transactions
) {
}

package com.product_agent.log.application.port.out;

import com.product_agent.log.domain.DlqLog;

public interface RegisterDlqLogPort {

    DlqLog registerDlqLog(DlqLog dqlLog);
}

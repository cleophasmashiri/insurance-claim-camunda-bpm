package com.muhacha.bpm.demos.claims.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class CreditCheck implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(CreditCheck.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        delegateExecution.setVariable("passedCreditCheck", Boolean.TRUE);
        LOGGER.info("Notification sent");
    }
}

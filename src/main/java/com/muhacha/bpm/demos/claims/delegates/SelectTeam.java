package com.muhacha.bpm.demos.claims.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SelectTeam implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(SelectTeam.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Notification sent");

        String accidentSeverity = (String) delegateExecution.getVariable("accidentSeverity");

        String team = "Team 0";
        if("Minor".equals(accidentSeverity)) {
            team = "Team 1";
        } else if("Major".equals(accidentSeverity)) {
            team = "Team 2";
        } else if("Writeoff".equals(accidentSeverity)) {
            team = "Team 3";
        }

        delegateExecution.setVariable("team", team);

    }
}

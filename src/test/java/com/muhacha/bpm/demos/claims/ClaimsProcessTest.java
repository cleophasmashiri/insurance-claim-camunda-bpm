package com.muhacha.bpm.demos.claims;


import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@Deployment(resources = "claims.bpmn")
public class ClaimsProcessTest {

    @Test
    public void givenCustomerHasSubmittedAClaimThatIsAcceptableThenSuccessEmail() {

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", "97773737");

        ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey("claims", variables);
        System.out.println("Process instance: " + processInstance + " started");

        assertThat(processInstance)
                        .isStarted()
                        .isWaitingAt("select_consultant_task")
                        .task()
                        .hasCandidateGroup("Managers");

    }
}

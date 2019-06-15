package com.muhacha.bpm.demos.claims;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.complete;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.variable.Variables.fileValue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Deployment(resources = "claims.bpmn")
public class ClaimsTest {



        @Test
    public void givenHasHadMinorAccidentWhenClaimIsSubmittedThenTeam1ShouldBeSelected() {

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", "97773737");
        variables.put("accidentSeverity", "Major");

        ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey("claims", variables);
        System.out.println("Process instance: " + processInstance + " started");

        RuntimeService runtimeService = processEngine().getRuntimeService();
        variables = runtimeService.getVariables(processInstance.getId());
        assertThat("Team 2").isEqualTo(variables.get("team"));

    }


    @Test
    public void givenCustomerHasSubmittedAcceptableAppSuccessThenSuccessEmailSent() {

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", "97773737");
        variables.put("accidentSeverity", "Major");

        String photo = "car1.jpg";
        InputStream contractSream = ClaimsDemoApplication.class.getClassLoader().getResourceAsStream(photo);
        variables.put("photo1", fileValue(photo)
                .file(contractSream)
                .mimeType("application/jpg")
                .create());

        ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey("claims", variables);
        System.out.println("Process instance: " + processInstance + " started");

        assertThat(processInstance)
                .isStarted()
                .hasPassed("select_team")
                .isWaitingAt("select_consultant_task")
                .task()
                .hasCandidateGroup("Managers");

        complete(task(), Variables.createVariables().putValue("assignedConsultant", "Mike"));

        assertThat(processInstance)
                .isWaitingAt("inspect_vehicle_task")
        .task()
        .hasCandidateGroup("Consultant");

        Map<String, Object> reviewVariables = new HashMap<>();
                String photo2 = "car2.jpg";
        InputStream photo2Stream = ClaimsDemoApplication.class.getClassLoader().getResourceAsStream(photo2);
        reviewVariables.put("photo2", fileValue(photo2)
                .file(photo2Stream)
                .mimeType("application/jpg")
                .create());

        reviewVariables.put("notes", "Consultant comments");
        complete(task(), reviewVariables);

        assertThat(processInstance)
                        .isWaitingAt("manager_review_task")
                        .task()
                        .hasCandidateGroup("Managers");

         complete(task(), Variables.createVariables().putValue("approved", Boolean.TRUE));

         assertThat(processInstance)
                         .hasPassed("send_approved_email")
                         .isEnded();


    }
}

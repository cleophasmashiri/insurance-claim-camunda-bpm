package com.muhacha.bpm.demos.claims;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;


@RunWith(SpringRunner.class)
@SpringBootTest
@Deployment(resources = {"loanRequest.bpmn"})
public class ClaimsDemoApplicationTests {

	@Test
	public void givenCustomerSubmittedLoanRequestThatMeetsScoreThenApprovedNotificationIsSent() {

		Map<String, Object> variables = new HashMap<>();

		ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey("loanRequest", variables);

		assertThat(processInstance)
				.isStarted()
		.hasPassed("submitted_application", "requested_credit_check");
	}

}

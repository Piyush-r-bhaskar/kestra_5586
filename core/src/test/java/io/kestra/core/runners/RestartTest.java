package io.kestra.core.runners;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.junit.annotations.LoadFlows;
import io.kestra.plugin.core.flow.ForEachItemCaseTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

@KestraTest(startRunner = true)
public class RestartTest {
    @Inject
    private RestartCaseTest restartCaseTest;

    @Inject
    private ForEachItemCaseTest forEachItemCaseTest;

    @Test
    @LoadFlows({"flows/valids/restart_last_failed.yaml"})
    void restartFailedThenSuccess() throws Exception {
        restartCaseTest.restartFailedThenSuccess();
    }

    @Test
    @LoadFlows({"flows/valids/restart_always_failed.yaml"})
    void restartFailedThenFailureWithGlobalErrors() throws Exception {
        restartCaseTest.restartFailedThenFailureWithGlobalErrors();
    }

    @RetryingTest(5)
    @LoadFlows({"flows/valids/restart_local_errors.yaml"})
    void restartFailedThenFailureWithLocalErrors() throws Exception {
        restartCaseTest.restartFailedThenFailureWithLocalErrors();
    }

    @Test
    @LoadFlows({"flows/valids/restart-each.yaml"})
    void replay() throws Exception {
        restartCaseTest.replay();
    }

    @Test
    @LoadFlows({"flows/valids/restart-parent.yaml", "flows/valids/restart-child.yaml"})
    void restartSubflow() throws Exception {
        restartCaseTest.restartSubflow();
    }

    @Test
    @LoadFlows({"flows/valids/restart-for-each-item.yaml", "flows/valids/restart-child.yaml"})
    void restartForEachItem() throws Exception {
        forEachItemCaseTest.restartForEachItem();
    }
}

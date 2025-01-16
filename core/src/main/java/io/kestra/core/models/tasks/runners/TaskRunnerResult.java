package io.kestra.core.models.tasks.runners;

import io.kestra.core.models.tasks.Output;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class TaskRunnerResult<T extends TaskRunnerDetailResult> implements Output {
    private int exitCode;
    private AbstractLogConsumer logConsumer;
    private T details;

    @SuppressWarnings("unchecked")
    public TaskRunnerResult(int exitCode, AbstractLogConsumer logConsumer) {
        this.exitCode = exitCode;
        this.logConsumer = logConsumer;
        this.details = (T) TaskRunnerDetailResult.builder().build();
    }
}

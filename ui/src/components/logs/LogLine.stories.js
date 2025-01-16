import LogLine from "./LogLine.vue";

export default {
    title: "Components/Logs/LogLine",
    component: LogLine,
    argTypes: {
        cursor: {
            control: "boolean",
            description: "Shows a cursor icon on the left side of the log line"
        },
        filter: {
            control: "text",
            description: "Text to filter log messages"
        },
        level: {
            control: "select",
            options: ["INFO", "WARNING", "ERROR", "DEBUG"],
            description: "Log level"
        },
        excludeMetas: {
            control: "array",
            description: "Array of meta fields to exclude from display"
        },
        title: {
            control: "boolean",
            description: "Shows taskId or flowId as a title"
        }
    }
};

const Template = (args) => ({
    components: {LogLine},
    setup() {
        return {args};
    },
    template: "<LogLine v-bind=\"args\" />"
});

export const Info = Template.bind({});
Info.args = {
    cursor: true,
    log: {
        level: "INFO",
        message: "This is an info message",
        timestamp: new Date().toISOString(),
        namespace: "test-namespace",
        flowId: "flow-123",
        executionId: "exec-456"
    },
    level: "INFO",
    filter: "",
    excludeMetas: [],
    title: false
};

export const Warning = Template.bind({});
Warning.args = {
    cursor: true,
    log: {
        level: "WARNING",
        message: "This is a warning message",
        timestamp: new Date().toISOString(),
        namespace: "test-namespace",
        flowId: "flow-123",
        executionId: "exec-456"
    },
    level: "WARNING",
    filter: "",
    excludeMetas: [],
    title: false
};

export const Error = Template.bind({});
Error.args = {
    cursor: true,
    log: {
        level: "ERROR",
        message: "This is an error message",
        timestamp: new Date().toISOString(),
        namespace: "test-namespace",
        flowId: "flow-123",
        executionId: "exec-456"
    },
    level: "ERROR",
    filter: "",
    excludeMetas: [],
    title: false
};

export const WithTitle = Template.bind({});
WithTitle.args = {
    cursor: true,
    log: {
        level: "INFO",
        message: "This is a message with title",
        timestamp: new Date().toISOString(),
        namespace: "test-namespace",
        flowId: "flow-123",
        executionId: "exec-456",
        taskId: "task-789"
    },
    level: "INFO",
    filter: "",
    excludeMetas: [],
    title: true
};

export const WithFilter = Template.bind({});
WithFilter.args = {
    cursor: true,
    log: {
        level: "INFO",
        message: "This is a filterable message",
        timestamp: new Date().toISOString(),
        namespace: "test-namespace",
        flowId: "flow-123",
        executionId: "exec-456"
    },
    level: "INFO",
    filter: "filterable",
    excludeMetas: [],
    title: false
};

export const WithMarkdown = Template.bind({});
WithMarkdown.args = {
    cursor: true,
    log: {
        level: "INFO",
        message: "This message contains **markdown** and a [link](https://kestra.io)",
        timestamp: new Date().toISOString(),
        namespace: "test-namespace",
        flowId: "flow-123",
        executionId: "exec-456"
    },
    level: "INFO",
    filter: "",
    excludeMetas: [],
    title: false
};

export const WithExcludedMetas = Template.bind({});
WithExcludedMetas.args = {
    cursor: true,
    log: {
        level: "INFO",
        message: "This message has excluded meta fields",
        timestamp: new Date().toISOString(),
        namespace: "test-namespace",
        flowId: "flow-123",
        executionId: "exec-456"
    },
    level: "INFO",
    filter: "",
    excludeMetas: ["namespace", "flowId"],
    title: false
}; 
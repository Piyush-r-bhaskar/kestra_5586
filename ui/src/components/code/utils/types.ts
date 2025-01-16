import {defineComponent} from "vue";
import type {RouteRecordName, RouteParams} from "vue-router";

export type Schemas = {
    $ref?: string;
    $schema?: string;
    definitions?: {
        [key: string]: object;
    };
};

export type Field = {
    component: ReturnType<typeof defineComponent>;
    value: any;
    label: string;
    required?: boolean;
    disabled?: boolean;
};

export type LabelField = Omit<Field, "value"> & {
    value: [string, string][];
};

type InputField = Field & {
    inputs: any[];
};

type VariableField = Field & {
    variables: any[];
};

type Main = {
    id: Field;
    namespace: Field;
    description: Field;
};

type ConcurrencyField = Field & {
    root: string;
    schema: object;
};

type General = {
    retry: Field;
    labels: LabelField;
    inputs: InputField;
    outputs: Field;
    variables: VariableField;
    concurrency: ConcurrencyField;
    pluginDefaults: Field;
    disabled: Field;
};

export type Fields = {
    main: Main;
    general: General;
};

export type Breadcrumb = {
    label: string;
    to: {
        name: RouteRecordName;
        params: RouteParams;
    };
};

export type CollapseItem = {
    title: string;
    elements?: Record<string, any>[];
};

export type Sections = {
    main: CollapseItem[];
    segments: CollapseItem[];
};

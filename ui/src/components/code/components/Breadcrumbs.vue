<template>
    <el-breadcrumb class="p-4">
        <el-breadcrumb-item
            v-for="(breadcrumb, index) in breadcrumbs"
            :key="index"
            class="item"
        >
            <router-link :to="breadcrumb.to">
                {{ breadcrumb.label }}
            </router-link>
        </el-breadcrumb-item>
    </el-breadcrumb>
</template>

<script setup lang="ts">
    import {computed} from "vue";

    import {Breadcrumb} from "../utils/types";

    import {useRoute} from "vue-router";
    const route = useRoute();

    import {useI18n} from "vue-i18n";
    const {t} = useI18n({useScope: "global"});

    const props = defineProps({flow: {type: Object, required: true}});

    const params = {
        namespace: route.params.namespace,
        id: props.flow.id ?? "new",
        tab: "edit",
    };

    const breadcrumbs = computed<Breadcrumb[]>(() => {
        return [
            {
                label: props.flow.id ?? t("create_flow"),
                to: {name: route.name, params},
            },
            ...(route.query.section
                ? [
                    {
                        label:
                            route.query.identifier === "new"
                                ? t(`no_code.creation.${route.query.section}`)
                                : route.query.identifier,
                        to: {name: route.name, params},
                    },
                ]
                : []),
        ];
    });
</script>

<style scoped lang="scss">
@import "../styles/code.scss";

.item:last-child > .el-breadcrumb__inner > a {
    color: $code-primary !important;
}
</style>

<template>
    <div class="h-100 p-4">
        <div class="d-flex justify-content-between align-items-center">
            <span class="fs-6 fw-bold">
                {{ t("dashboard.empty_next_scheduled") }}
            </span>
            <RouterLink
                :to="{name: 'admin/triggers'}"
            >
                <el-button type="primary" size="small" text>
                    {{ t("dashboard.see_all") }}
                </el-button>
            </RouterLink>
        </div>

        <div class="pt-4" v-if="loading">
            <el-table :data="skeletonData" class="scheduled" :height="240">
                <el-table-column :label="$t('namespace')">
                    <template #default>
                        <el-skeleton-item variant="text" style="width: 100%" />
                    </template>
                </el-table-column>
                <el-table-column :label="$t('flow')">
                    <template #default>
                        <el-skeleton-item variant="text" style="width: 100%" />
                    </template>
                </el-table-column>
                <el-table-column :label="$t('trigger')" width="100">
                    <template #default>
                        <el-skeleton-item variant="text" style="width: 80px" />
                    </template>
                </el-table-column>
                <el-table-column :label="$t('next')" width="120">
                    <template #default>
                        <el-skeleton-item variant="text" style="width: 100px" />
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="pt-4" v-else-if="triggers.results.length">
            <el-table
                :data="triggers.results"
                class="scheduled"
                :height="240"
            >
                <el-table-column :label="$t('namespace')">
                    <template #default="scope">
                        <RouterLink
                            :to="{
                                name: 'namespaces/update',
                                params: {
                                    id: scope.row.namespace,
                                },
                            }"
                        >
                            <el-tooltip
                                :content="scope.row.namespace"
                                placement="right"
                            >
                                <span class="text-truncate">
                                    {{ scope.row.namespace }}
                                </span>
                            </el-tooltip>
                        </RouterLink>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('flow')">
                    <template #default="scope">
                        <RouterLink
                            :to="{
                                name: 'flows/update',
                                params: {
                                    namespace: scope.row.namespace,
                                    id: scope.row.flowId,
                                },
                            }"
                        >
                            <el-tooltip
                                :content="scope.row.flowId"
                                placement="right"
                            >
                                <span class="text-truncate">
                                    {{ scope.row.flowId }}
                                </span>
                            </el-tooltip>
                        </RouterLink>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('trigger')" width="100">
                    <template #default="scope">
                        <el-tooltip
                            :content="scope.row.triggerId"
                            placement="right"
                        >
                            <span class="text-truncate">
                                {{ scope.row.triggerId }}
                            </span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column :label="$t('next')" width="120">
                    <template #default="scope">
                        <date-ago :date="scope.row.next" />
                    </template>
                </el-table-column>
            </el-table>
            <div class="d-flex justify-content-end">
                <el-pagination
                    v-model:current-page="currentPage"
                    @current-change="loadTriggers"
                    :total="triggers.total"
                    layout="prev, pager, next, total"
                    :page-size="5"
                    size="small"
                    class="pt-3"
                />
            </div>
        </div>
        <NoData v-else />
    </div>
</template>

<script setup>
    import {onBeforeMount, ref} from "vue";
    import {useStore} from "vuex";
    import {useI18n} from "vue-i18n";

    import NoData from "../../../../layout/NoData.vue";
    import DateAgo from "../../../../layout/DateAgo.vue";

    import {RouterLink} from "vue-router";

    const props = defineProps({
        flow: {
            type: String,
            required: false,
            default: null,
        },
        namespace: {
            type: String,
            required: false,
            default: null,
        },
        loading: {
            type: Boolean,
            default: false
        }
    });

    const store = useStore();
    const {t} = useI18n({useScope: "global"});

    const triggers = ref({results: [], total: 0});
    const currentPage = ref(1);

    const skeletonData = Array(5).fill({});

    const loadTriggers = (page = 1) => {
        store
            .dispatch("trigger/findTriggers", {
                namespace: props.namespace,
                flowId: props.flow,
                size: 5,
                page,
                sort: "next:asc",
                emptyNext: true,
            })
            .then((response) => {
                if (!response) return;
                triggers.value = response;
            });
    };

    onBeforeMount(() => {
        loadTriggers();
    });
</script>

<style lang="scss" scoped>
.scheduled {
    background: var(--ks-background-table-row);
}

:deep(.el-skeleton-item) {
    background: var(--ks-background-skeleton);
}
</style>

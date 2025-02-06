<template>
    <span data-component="FILENAME_PLACEHOLDER" v-if="labels">
        <el-tag
            v-for="(value, key) in labelMap"
            :key="key"
            :type="checked(key, value) ? 'primary' : undefined"
            class="me-1 labels"
            size="small"
            disable-transitions
            @click="onClicked({key, value})"
        >
            <router-link v-if="filterEnabled" :to="link(key, value)">
                {{ key }}: {{ value }}
            </router-link>
            <template v-else>{{ key }}: {{ value }}</template>
        </el-tag>
    </span>
</template>

<script>
    export default {
        props: {
            labels: {
                type: Object,
                default: () => {}
            },
            filterEnabled: {
                type: Boolean,
                default: true
            }
        },
        computed: {
            labelMap() {
                if (Array.isArray(this.labels)) {
                    return Object.fromEntries(this.labels.map(label => [label.key, label.value]));
                } else {
                    return this.labels;
                }
            }
        },
        methods: {
            getLabelsFromQuery() {
                const labels = new Map();
                (this.$route.query.labels !== undefined ?
                    (typeof(this.$route.query.labels) === "string" ? [this.$route.query.labels] : this.$route.query.labels)  :
                    []
                )
                    .forEach(label => {
                        const separatorIndex = label.indexOf(":");

                        if (separatorIndex === -1) {
                            return;
                        }

                        labels.set(label.slice(0, separatorIndex), label.slice(separatorIndex + 1));
                    });

                return labels;
            },
            checked(key, value) {
                return this.getLabelsFromQuery().has(key) && this.getLabelsFromQuery().get(key) === value;
            },
            link(key, value) {
                const labels = this.getLabelsFromQuery();

                if (labels.has(key)) {
                    labels.delete(key);
                } else {
                    labels.set(key, value);
                }

                const qs = {
                    ...this.$route.query,
                    ...{"labels": Array.from(labels.keys()).map((key) => key + ":" + labels.get(key))}
                };

                delete qs.page;

                return {name: this.$route.name, params: this.$route.params, query: qs};
            },
            onClicked({key, value}) {
                const currentQuery = {...this.$route.query};
                const labels = currentQuery.labels ? currentQuery.labels.split(",") : [];

                const labelIndex = labels.findIndex(label => label.startsWith(`${key}:`));
                if (labelIndex !== -1) {
                    labels.splice(labelIndex, 1);
                } else {
                    labels.push(`${key}:${value}`);
                }
                this.$router.push({query: {...currentQuery, labels: labels.join(",")}});
            }
        }
    };
</script>
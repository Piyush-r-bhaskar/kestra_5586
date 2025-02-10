<template>
    <span v-if="label" class="text-lowercase">
        {{ $t(`filters.options.${label}`) }}
    </span>
    <span v-if="comparator" class="comparator">{{ comparator }}</span>
    <span v-if="value">{{ !comparator ? ":" : "" }}{{ value }}</span>
</template>

<script setup lang="ts">
    import {computed} from "vue";

    import {CurrentItem} from "../utils/types";

    const props = defineProps<{ option: CurrentItem }>();

    import moment from "moment";
    const DATE_FORMAT = localStorage.getItem("dateFormat") || "llll";

    const formatter = (date: Date) => moment(date).format(DATE_FORMAT);

    const UNKNOWN = "unknown";

    const label = computed(() => props.option.label);
    const comparator = computed(() => props.option?.comparator?.label);
    const value = computed(() => {
        const {value, label, comparator} = props.option;

        if (!value.length) return;
        if (label === "labels") {
            return Array.isArray(value) && value.length === 1 ? value[0] : value;
        }
        if (label !== "absolute_date" && comparator?.label !== "between") {
            return `${value.join(", ")}`;
        }

        if (typeof value[0] !== "string") {
            const {startDate, endDate} = value[0];
            return `${startDate ? formatter(new Date(startDate)) : UNKNOWN}:and:${endDate ? formatter(new Date(endDate)) : UNKNOWN}`;
        }

        return UNKNOWN;
    });
</script>

<style scoped lang="scss">
    span {
        padding: 0.33rem 0.35rem;
        display: inline-block;

        .comparator {
            background: #f6f6f6;
            border-left: 4px solid #ffffff;
            border-right: 4px solid #ffffff;

            html.dark & {
                background: rgba(49, 49, 49, 0.5);
                border-color: #1e202a;
            }
        }

        &:first-child{
            background: #f6f6f6;

            html.dark & {
                background: rgba(49, 49, 49, 0.5);
            }
        }
    }
</style>

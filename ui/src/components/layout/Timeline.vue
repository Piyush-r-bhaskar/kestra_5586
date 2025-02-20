<template>
    <div class="timeline">
        <div v-for="(history, index) in histories" :key="'timeline-' + index" class="timeline-item">
            <div class="timeline-content">
                <span class="timeline-date">{{ $filters.date(history.date, 'iso') }}</span>
                <span class="timeline-state">{{ history.state }}</span>
            </div>
            <div class="timeline-dot" :style="getStyle(history.state)" />
        </div>
    </div>
</template>

<script setup>
    import {defineProps} from "vue";

    const StatusRemap = {
        "failed": "error",
        "warn": "warning",
    };

    defineProps({
        histories: {
            type: Array,
            default: () => []
        }
    });

    const getStyle = (state) => {
        const statusVarname = (StatusRemap[state.toLowerCase()] ?? state)?.toLowerCase();
        return {
            backgroundColor: `var(--ks-chart-${statusVarname})`
        };
    };
</script>

<style lang="scss" scoped>
.timeline {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px;
    margin-bottom: 10px;
    border-radius: 5px;
    background-color: var(--ks-background-card);
    box-shadow: 0px 2px 4px 0px var(--ks-card-shadow);
    border: 1px solid var(--ks-border-primary);

    .timeline-item {
        position: relative;
        text-align: center;
        flex: 1;

        &:not(:last-child)::after {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translateY(-50%);
            width: calc(100% - 12px);
            height: 1px;
            background-color: var(--ks-border-primary);
            z-index: 0;
        }

        .timeline-dot {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 12px;
            height: 12px;
            border-radius: 50%;
            z-index: 10;
        }

        .timeline-content {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
            font-size: 12px;

            .timeline-date {
                margin-bottom: 35px;
                font-size: 12px;
                color: var(--ks-content-tertiary);
            }

            .timeline-state {
                text-transform: uppercase;
                color: var(--ks-content-secondary);
            }
        }
    }
}
</style>

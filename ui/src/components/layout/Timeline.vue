<template>
    <div class="timeline">
        <div v-for="(history, index) in histories" :key="'timeline-' + index" class="timeline-item">
            <div class="timeline-content">
                <span class="timeline-date">{{ $filters.date(history.date, 'iso') }}</span>
                <div class="timeline-dot" :style="getStyle(history.state)" />
                <span class="timeline-state">{{ history.state }}</span>
            </div>
        </div>
    </div>
</template>

<script setup>

    defineProps({
        histories: {
            type: Array,
            default: () => []
        }
    });

    const getStyle = (state) => {
        const statusVarname = state.toLowerCase();
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
    position: relative;
    margin-bottom: 10px;
    border-radius: 5px;
    background-color: var(--ks-background-body);
    box-shadow: 0px 2px 4px 0px var(--ks-card-shadow);
    border: 1px solid var(--ks-border-primary);

    @media (max-width: 768px) {
        flex-direction: column;
        align-items: start;
        padding: 30px 20px;
    }

    .timeline-item {
        position: relative;
        text-align: center;
        flex: 1;

        @media (max-width: 768px) {
            width: 100%;
            margin-bottom: 30px;
            text-align: center;

            &:last-child {
                margin-bottom: 0;
            }
        }

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

            @media (max-width: 768px) {
                top: 100%;
                left: 50%;
                transform: translateX(-50%);
                width: 1px;
                height: 20px;
                margin-top: 6px;
            }
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

            @media (max-width: 768px) {
                position: relative;
                display: inline-block;
                top: auto;
                left: auto;
                transform: none;
            }
        }

        .timeline-content {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
            font-size: 12px;

            .timeline-date {
                margin-bottom: 1rem;
                font-size: 12px;
                color: var(--ks-content-tertiary);

                @media (max-width: 768px) {
                    margin-bottom: 0;
                }
            }

            .timeline-state {
                margin-top: 0.5rem;
                color: var(--ks-content-secondary);

                @media (max-width: 768px) {
                    margin-top: 0;
                }
            }
        }
    }
}
</style>

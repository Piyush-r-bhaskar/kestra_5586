package io.kestra.core.models.dashboards.charts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.dashboards.ChartOption;
import io.kestra.core.models.dashboards.DataFilter;
import io.kestra.core.validations.DataChartValidation;
import io.kestra.plugin.core.dashboard.chart.Bar;
import io.kestra.plugin.core.dashboard.chart.Pie;
import io.kestra.plugin.core.dashboard.chart.Table;
import io.kestra.plugin.core.dashboard.chart.TimeSeries;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@Plugin
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@EqualsAndHashCode
@DataChartValidation
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
//@JsonSubTypes({
//    @JsonSubTypes.Type(value = Bar.class, name = "io.kestra.plugin.core.dashboard.chart.Bar"),
//    @JsonSubTypes.Type(value = Pie.class, name = "io.kestra.plugin.core.dashboard.chart.Pie"),
//    @JsonSubTypes.Type(value = Table.class, name = "io.kestra.plugin.core.dashboard.chart.Table"),
//    @JsonSubTypes.Type(value = TimeSeries.class, name = "io.kestra.plugin.core.dashboard.chart.TimeSeries")
//})
public abstract class DataChart<P extends ChartOption, D extends DataFilter<?, ?>> extends Chart<P> implements io.kestra.core.models.Plugin {
    @NotNull
    private D data;

    public Integer minNumberOfAggregations() {
        return null;
    }

    public Integer maxNumberOfAggregations() {
        return null;
    }
}

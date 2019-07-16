package com.vaadin.poc.spring;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.poc.model.Project;

import java.util.Arrays;
import java.util.List;

public class ProjectChartComponent extends VerticalLayout {
    private List<Project> projects;
    final Chart chart = new Chart();

    public ProjectChartComponent(Project... project) {
        this.projects = Arrays.asList(project);

        setWidthFull();
        initChart();
    }

    private void initChart() {
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Points");
        configuration.getChart().setType(ChartType.SPLINE);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setVerticalAlign(VerticalAlign.BOTTOM);
        legend.setAlign(HorizontalAlign.CENTER);

        XAxis xAxis = configuration.getxAxis();
        xAxis.setTickPixelInterval(50);

        projects.forEach(project -> {
            final DataSeries series = new DataSeries();
            series.setPlotOptions(new PlotOptionsSpline());
            series.setName(project.getProjectName());

            project.getPoints().forEach(p -> series.add(new DataSeriesItem(p.getX(), p.getY())));
            configuration.addSeries(series);
        });

        add(chart);
    }
}

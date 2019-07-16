package com.vaadin.poc.spring;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.poc.model.Project;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ProjectDetailsComponent extends VerticalLayout {
    private Project project;

    public ProjectDetailsComponent(Project project) {
        this.project = project;

        add(new H3(project.getProjectName()));
        add(project.getProjectDescription());

        Div subProjects = new Div();
        if (project.getSubProjects() == null || project.getSubProjects().size() == 0) {
            subProjects.add("Have no sub projects");
        } else {
            String text = "Have " + project.getSubProjects().size() + " sub projects: ";
            text += project.getSubProjects().stream().map(Project::getProjectName).collect(Collectors.joining(", "));
            subProjects.add(text);
        }
        add(subProjects);

        Div points = new Div();
        points.add("Points: " + Arrays.toString(project.getPoints().toArray()));
        add(points);
    }

}

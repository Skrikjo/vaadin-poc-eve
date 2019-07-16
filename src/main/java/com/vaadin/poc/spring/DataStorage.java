package com.vaadin.poc.spring;

import com.vaadin.poc.model.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataStorage {
    private List<Project> projectsGridItems = Project.getRootItems();
    private List<Project> dataObjectsGridItems = Project.getRootItems();


    public DataStorage() {
    }

    public List<Project> getProjectsGridItems() {
        return projectsGridItems;
    }

    public List<Project> getDataObjectsGridItems() {
        return dataObjectsGridItems;
    }
}

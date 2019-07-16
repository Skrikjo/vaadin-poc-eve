package com.vaadin.poc.model;

import java.util.*;
import java.util.stream.Collectors;

public class Project {
    private static String[] ICONS = {"vaadin:archive", "vaadin:briefcase", "vaadin:clipboard-pulse", "vaadin:code",
            "vaadin:cluster", "vaadin:compile", "vaadin:cog", "vaadin:connect"};
    private static long ID_COUNTER = 0;

    private long id;
    private String projectName;
    private String projectDescription;
    private List<Project> subProjects = new ArrayList<>();
    private String icon;
    private List<Point> points;

    public Project(String projectName, String projectDescription) {
        this.id = ID_COUNTER++;
        this.projectName = projectName + " " + id;
        this.projectDescription = projectDescription + " " + id;
        points = generateRandomPoints();
    }

    public Project(String projectName, String projectDescription, String icon) {
        this(projectName, projectDescription);
        this.icon = icon;
    }

    private List<Point> generateRandomPoints() {
        List<Point> points = new ArrayList<>();
        Random r = new Random();
        int length = r.nextInt(20);
        for (int i = 0; i <= length; i++) {
            points.add(new Point(r.nextInt(100), r.nextInt(100)));
        }

       return points.stream().sorted(Comparator.comparingInt(Point::getX)).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public List<Project> getSubProjects() {
        return subProjects;
    }

    public void setSubProjects(List<Project> subProjects) {
        this.subProjects = subProjects;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static List<Project> getRootItems() {
        List<Project> projects = getTenItems("Root", false);
        projects.forEach(project -> project.setSubProjects(getTenItems("Sub", true)));

        return projects;
    }

    public static List<Project> getTenItems(String prefix, boolean withSub) {
        List<Project> projects = new ArrayList<>();

        Random r = new Random();
        for (int i = 1; i <= 5; i++) {
            Project project = new Project(prefix + " Project: SN#" ,
                    prefix + " Long Project Description ", ICONS[r.nextInt(8)]);
            if (withSub) {
                project.setSubProjects(getTenItems("Sub Sub",false));
            }

            projects.add(project);
        }

        return projects;
    }

    public class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "{" +
                    "x: " + x +
                    ", y: " + y +
                    '}';
        }
    }
}

package com.vaadin.poc.spring;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.poc.model.Project;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Tag("data-object-view")
@JsModule("./src/data-object-view.js")
@JsModule("./styles/shared-styles.js")
public class DataObjectView extends PolymerTemplate<TemplateModel> {

    @Id("details")
    private Button details;

    @Id("diagram")
    private Button diagram;

    @Id("title")
    private Div title;

    @Id("tabContent")
    private Div tabContent;

    @Id("footerDate")
    private Span footerDate;

    @Id("tabs")
    private Tabs tabs;

    @Id("hideBtn")
    private Button hideBtn;

    @Id("fullSizeBtn")
    private Button fullSizeBtn;

    @Id("sideBySideBtn")
    private Button sideBySideBtn;

    @Id("icons-cont")
    private HorizontalLayout iconsCont;

    @Id("footer")
    private HorizontalLayout footer;

    private ContextMenu detailContext;
    private ContextMenu diagramContext;

    @Autowired
    private DataStorage dataStorage;

    private Set<Project> projects = new HashSet<>();
    private Map<String, Tab> tabsLableMap = new HashMap<>();
    private Map<String, Component> tabsComponentMap = new HashMap<>();
    private String allDiagramsLabel = "All Diagrams";
    private String allDetailsLabel = "All Details";


    public DataObjectView() {
        iconsCont.setSpacing(false);
        iconsCont.setVisible(false);
        footer.setSpacing(false);
        footer.setVisible(false);
    }

    @PostConstruct
    private void init() {
        detailContext = new ContextMenu(details);
        detailContext.setOpenOnClick(true);
        details.setEnabled(false);
        addAllDetailsMenuItem();

        diagramContext = new ContextMenu(diagram);
        diagramContext.setOpenOnClick(true);
        diagram.setEnabled(false);
        addAllDiagramsMenuItem();

        addListener(ItemDroppedEvent.class, this::projectDroppedToWindow);
        updateTitle();

        tabs.addSelectedChangeListener(this::updateTabContent);
    }

    public void updateTabContent(Tabs.SelectedChangeEvent selectedChangeEvent) {
        tabContent.removeAll();
        if (tabs.getSelectedTab() != null) {
            tabContent.add(tabsComponentMap.get(tabs.getSelectedTab().getLabel()));
        }
    }

    private <T extends ComponentEvent<?>> void projectDroppedToWindow(ItemDroppedEvent event) {
        Project project = findProjectById(dataStorage.getDataObjectsGridItems(), event.getId());

        if (project == null || projects.contains(project)) {
            return;
        }

        projects.add(project);

        iconsCont.setVisible(true);
        footer.setVisible(true);
        details.setEnabled(true);
        diagram.setEnabled(true);
        updateTitle();

//          Adding elements to detail and diagram menus
        addDetailContextMenuItem(project);
        addDiagramContextMenuItem(project);

        tabsComponentMap.put(allDiagramsLabel, new ProjectChartComponent(projects.toArray(new Project[projects.size()])));
        tabsComponentMap.put(allDetailsLabel, getAllDetails());
        if (tabs.getSelectedTab() != null &&
                (tabs.getSelectedTab().getLabel().equals(allDiagramsLabel) || tabs.getSelectedTab().getLabel().equals(allDetailsLabel))) {
            updateTabContent(null);
        }

        if (projects.size() == 1) {
            createAllDiagramsTab();
        }
    }

    private Project findProjectById(List<Project> list, long id) {
        if (list == null) {
            return null;
        }

        for (Project project : list) {
            if (project.getId() == id) {
                return project;
            }
            Project target = findProjectById(project.getSubProjects(), id);
            if (target != null) {
                return target;
            }
        }

        return null;
    }

    private void updateTitle() {
        if (projects.size() == 0) {
            setText("No projects selected");
        } else if (projects.size() == 1) {
            setText("Project with Id: " + projects.stream().findFirst().get().getId());
        } else {
            String ids = projects.stream().map(p -> Long.toString(p.getId())).collect(Collectors.joining(","));
            setText("Projects with Ids: [" + ids + "]");
        }
    }

    private void addDiagramContextMenuItem(Project p) {
        diagramContext.addItem(p.getProjectName(), event -> {
            String label = "Diagram: " + p.getProjectName();
            Tab tab = tabsLableMap.get(label);

            if (tab == null) {
                tabsComponentMap.put(label,  new ProjectChartComponent(p));
                tab = new Tab(label);
                tabsLableMap.put(label,  tab);
                tabs.add(tab);
            }

            tabs.setSelectedTab(tab);
        });
    }

    private void addDetailContextMenuItem(Project p) {
        detailContext.addItem(p.getProjectName(), event -> {
            String label = "Details: " + p.getProjectName();
            Tab tab = tabsLableMap.get(label);

            if (tab == null) {
                tabsComponentMap.put(label,  new ProjectDetailsComponent(p));
                tab = new Tab(label);
                tabsLableMap.put(label,  tab);
                tabs.add(tab);
            }

            tabs.setSelectedTab(tab);
        });
    }

    private void addAllDiagramsMenuItem() {
        diagramContext.addItem(allDiagramsLabel, event -> {
            Tab tab = tabsLableMap.get(allDiagramsLabel);
            tabs.setSelectedTab(tab);
        });
        diagramContext.add(new Hr());
    }

    private void createAllDiagramsTab() {
        Tab tab = new Tab(allDiagramsLabel);
        tabs.add(tab);
        tabsLableMap.put(allDiagramsLabel,  tab);

        tabs.setSelectedTab(tab);
    }

    private void addAllDetailsMenuItem() {
        detailContext.addItem(allDetailsLabel, event -> {
            Tab tab = tabsLableMap.get(allDetailsLabel);
            tabsComponentMap.put(allDetailsLabel,  getAllDetails());

            if (tab == null) {
                tab = new Tab(allDetailsLabel);
                tabs.add(tab);
            }
            tabsLableMap.put(allDetailsLabel,  tab);

            tabs.setSelectedTab(tab);
        });

        detailContext.add(new Hr());
    }

    private VerticalLayout getAllDetails() {
        VerticalLayout cont = new VerticalLayout();
        cont.setHeightFull();

        projects.forEach(p -> cont.add(new ProjectDetailsComponent(p)));

        return cont;
    }

    public void setText(String text) {
        title.setText(text);
        footerDate.setText(text);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        updateTabContent(null);
    }

    public void addHideBtnClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        hideBtn.addClickListener(listener);
    }

    public void addFullSizeBtnClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        fullSizeBtn.addClickListener(listener);
    }
    public void addSideBySideBtnBtnClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        sideBySideBtn.addClickListener(listener);
    }

    //  Drop event, that will be fired in client when project will be dropped to component.
//  Event will have id of dropped project
    @DomEvent("item-dropped")
    public static class ItemDroppedEvent extends ComponentEvent<DataObjectView> {
        private final int id;

        public ItemDroppedEvent(DataObjectView source, boolean fromClient, @EventData("event.id") int id) {
            super(source, fromClient);
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }
}

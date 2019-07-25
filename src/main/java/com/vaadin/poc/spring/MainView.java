package com.vaadin.poc.spring;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.poc.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;

import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Route
@Tag("main-view")
@JsModule("./src/main-view.js")
@JsModule("./styles/shared-styles.js")
public class MainView extends PolymerTemplate<TemplateModel> {

    @Id("vaadinButton")
    private Button vaadinButton;
    @Id("vaadinButton1")
    private Button vaadinButton1;
    @Id("vaadinButton2")
    private Button vaadinButton2;
    @Id("vaadinButton3")
    private Button vaadinButton3;
    @Id("vaadinButton4")
    private Button vaadinButton4;

    @Id("projectTreeGrid")
    private TreeGrid<Project> projectTreeGrid;
    @Id("filesGrid")
    private TreeGrid<Project> filesGrid;

    @Id("firstWindow")
    private DataObjectView firstWindow;
    @Id("secondWindow")
    private DataObjectView secondWindow;

    @Autowired
    private DataStorage dataStorage;

    public MainView() {
        vaadinButton3.setEnabled(false);

    }

    @PostConstruct
    private void init() {
        addContextMenus();
        addProjectsTreeGrid();
        addDraggableTreeGrid();

        firstWindow.addFullSizeBtnClickListener(e -> showFirstWindow());
        firstWindow.addHideBtnClickListener(e -> showSecondWindow());
        firstWindow.addSideBySideBtnBtnClickListener(e -> showBothWindows());

        secondWindow.addFullSizeBtnClickListener(e -> showSecondWindow());
        secondWindow.addHideBtnClickListener(e -> showFirstWindow());
        secondWindow.addSideBySideBtnBtnClickListener(e -> showBothWindows());
    }

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    private void addProjectsTreeGrid() {
        projectTreeGrid.setWidthFull();
        projectTreeGrid.setHeightFull();

        AbstractHierarchicalDataProvider<Project, Boolean> projectsProvider = createProjectsProvider(dataStorage.getProjectsGridItems());



        projectTreeGrid.setDataProvider(projectsProvider);
        projectTreeGrid.addColumn(TemplateRenderer.<Project> of(
            "<vaadin-grid-tree-toggle class$='[[item.cssClassName]]' leaf='[[item.leaf]]' expanded='{{expanded}}' level='[[level]]'>" +
                    "<iron-icon icon='[[item.icon]]' class='treeGridIcon'></iron-icon>" +
                    " [[item.name]]</vaadin-grid-tree-toggle>")
            .withProperty("name", Project::getProjectName)
            .withProperty("icon", Project::getIcon)
            .withProperty("leaf", (item) -> !projectsProvider.hasChildren(item)));
    }

    private void addDraggableTreeGrid() {
        filesGrid.setWidthFull();
        filesGrid.setHeightFull();

        AbstractHierarchicalDataProvider<Project, Boolean> projectsProvider = createProjectsProvider(dataStorage.getDataObjectsGridItems());
        filesGrid.setDataProvider(projectsProvider);
        filesGrid.addColumn(TemplateRenderer.<Project> of(
                "<vaadin-grid-tree-toggle class$='[[item.cssClassName]]' leaf='[[item.leaf]]' expanded='{{expanded}}' level='[[level]]' projectId=[[item.projectId]]>" +
                        "[[item.name]]</vaadin-grid-tree-toggle>")
                .withProperty("name", Project::getProjectName)
                .withProperty("projectId", Project::getId)
                .withProperty("leaf", (item) -> !projectsProvider.hasChildren(item)))
                .setHeader("Draggable Project").setFlexGrow(6);
    }

    private AbstractHierarchicalDataProvider<Project, Boolean> createProjectsProvider(List<Project> items) {
        AbstractHierarchicalDataProvider<Project, Boolean> projectsProvider = new AbstractHierarchicalDataProvider<Project, Boolean>() {
            @Override
            public boolean isInMemory() {
                return false;
            }

            @Override
            public int getChildCount(HierarchicalQuery<Project, Boolean> hierarchicalQuery) {
                if (Objects.isNull(hierarchicalQuery.getParent())) {
                    return items.size();
                }
                return hierarchicalQuery.getParent().getSubProjects().size();
            }

            @Override
            public Stream<Project> fetchChildren(HierarchicalQuery<Project, Boolean> hierarchicalQuery) {
                if (Objects.isNull(hierarchicalQuery.getParent())) {
                    return items.stream();
                }
                return hierarchicalQuery.getParent().getSubProjects().stream();
            }

            @Override
            public boolean hasChildren(Project project) {
                return !project.getSubProjects().isEmpty();
            }
        };

        return projectsProvider;
    }

    private void addContextMenus() {
        ContextMenu contextMenu = new ContextMenu(vaadinButton);
        contextMenu.setOpenOnClick(true);
        contextMenu.addItem("Show first window", event -> showFirstWindow());
        contextMenu.addItem("Show second window",event -> showSecondWindow());
        contextMenu.addItem("Show both windows",event -> showBothWindows());

        ContextMenu contextMenu1 = new ContextMenu(vaadinButton1);
        contextMenu1.setOpenOnClick(true);
        contextMenu1.addItem("First menu item 1",
                event -> showNotification("Menu item was selected"));
        contextMenu1.addItem("Second menu item 1",
                event -> showNotification("Menu item was selected"));
        MenuItem subMenuItem = contextMenu1.addItem("SubMenu Item 1");
        SubMenu subMenu = subMenuItem.getSubMenu();
        subMenu.addItem("First sub menu item 1",
                event -> showNotification("Sub menu item was selected"));
        subMenu.addItem("Second sub  menu item 1",
                event -> showNotification("Sub menu item was selected"));

        ContextMenu contextMenu2 = new ContextMenu(vaadinButton2);
        contextMenu2.setOpenOnClick(true);
        contextMenu2.addItem("First menu item 2",
                event -> showNotification("Menu item was selected"));
        contextMenu2.addItem("Second menu item 2",
                event -> showNotification("Menu item was selected"));
        contextMenu2.addItem("Third menu item 2",
                event -> showNotification("Menu item was selected"));
        contextMenu2.addItem("Fourth menu item 2",
                event -> showNotification("Menu item was selected"));
    }

    private void showFirstWindow() {
        firstWindow.getElement().setProperty("style", "width: 100%");
        firstWindow.setVisible(true);

        secondWindow.setVisible(false);
    }

    private void showSecondWindow() {
        firstWindow.setVisible(false);

        secondWindow.getElement().setProperty("style", "width: 100%");
        secondWindow.setVisible(true);
    }

    private void showBothWindows() {
        firstWindow.getElement().setProperty("style", "width: 50%");
        firstWindow.setVisible(true);

        secondWindow.getElement().setProperty("style", "width: 50%");
        secondWindow.setVisible(true);
    }

    private void showNotification(String message) {
        Notification notification = new Notification(message, 3000, Notification.Position.TOP_END);
        notification.open();
    }
}

package com.example.aem.bmad.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Sling Model for the Navigation component.
 *
 * Specification: SPEC-NAV-001
 * User Story: US-CA-010
 *
 * @see <a href="../bmad/03-Architecture-Design/component-design.md#navigation-component">Component Design</a>
 */
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {NavigationModel.class, ComponentExporter.class},
    resourceType = NavigationModel.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NavigationModel implements ComponentExporter {

    static final String RESOURCE_TYPE = "aem-bmad-showcase/components/content/navigation";

    private static final int DEFAULT_STRUCTURE_DEPTH = 2;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ScriptVariable
    private Page currentPage;

    @ValueMapValue
    private String navigationRoot;

    @ValueMapValue
    private Integer structureDepth;

    @ValueMapValue
    private Boolean skipNavigationRoot;

    @ValueMapValue
    private Boolean collectAllPages;

    private List<NavigationItem> items;
    private String currentPath;

    @PostConstruct
    protected void init() {
        if (currentPage != null) {
            currentPath = currentPage.getPath();
        }
        items = buildNavigationTree();
    }

    private List<NavigationItem> buildNavigationTree() {
        if (navigationRoot == null || navigationRoot.isEmpty()) {
            return Collections.emptyList();
        }

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager == null) {
            return Collections.emptyList();
        }

        Page rootPage = pageManager.getPage(navigationRoot);
        if (rootPage == null) {
            return Collections.emptyList();
        }

        List<NavigationItem> navItems = new ArrayList<>();
        int depth = structureDepth != null ? structureDepth : DEFAULT_STRUCTURE_DEPTH;
        boolean skipRoot = skipNavigationRoot != null && skipNavigationRoot;
        boolean collectAll = collectAllPages != null && collectAllPages;

        if (!skipRoot) {
            navItems.add(createNavigationItem(rootPage, 0, depth, collectAll));
        } else {
            Iterator<Page> children = rootPage.listChildren(new NavigationPageFilter(collectAll));
            while (children.hasNext()) {
                navItems.add(createNavigationItem(children.next(), 0, depth, collectAll));
            }
        }

        return navItems;
    }

    private NavigationItem createNavigationItem(Page page, int level, int maxDepth, boolean collectAll) {
        List<NavigationItem> children = new ArrayList<>();

        if (level < maxDepth) {
            Iterator<Page> childPages = page.listChildren(new NavigationPageFilter(collectAll));
            while (childPages.hasNext()) {
                children.add(createNavigationItem(childPages.next(), level + 1, maxDepth, collectAll));
            }
        }

        return new NavigationItem(
            page.getPath(),
            page.getPath() + ".html",
            page.getNavigationTitle() != null ? page.getNavigationTitle() : page.getTitle(),
            isActive(page.getPath()),
            isCurrent(page.getPath()),
            level,
            children
        );
    }

    private boolean isActive(String pagePath) {
        return currentPath != null && currentPath.startsWith(pagePath);
    }

    private boolean isCurrent(String pagePath) {
        return currentPath != null && currentPath.equals(pagePath);
    }

    public String getNavigationRoot() {
        return navigationRoot;
    }

    public int getStructureDepth() {
        return structureDepth != null ? structureDepth : DEFAULT_STRUCTURE_DEPTH;
    }

    public List<NavigationItem> getItems() {
        return items != null ? items : Collections.emptyList();
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    /**
     * Page filter for navigation - excludes hidden pages unless collectAll is true.
     */
    private static class NavigationPageFilter extends PageFilter {
        private final boolean collectAll;

        NavigationPageFilter(boolean collectAll) {
            this.collectAll = collectAll;
        }

        @Override
        public boolean includes(Page page) {
            return collectAll || !page.isHideInNav();
        }
    }

    /**
     * Represents a single navigation item.
     */
    public static class NavigationItem {
        private final String path;
        private final String url;
        private final String title;
        private final boolean active;
        private final boolean current;
        private final int level;
        private final List<NavigationItem> children;

        public NavigationItem(String path, String url, String title, boolean active,
                              boolean current, int level, List<NavigationItem> children) {
            this.path = path;
            this.url = url;
            this.title = title;
            this.active = active;
            this.current = current;
            this.level = level;
            this.children = children;
        }

        public String getPath() { return path; }
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public boolean isActive() { return active; }
        public boolean isCurrent() { return current; }
        public int getLevel() { return level; }
        public List<NavigationItem> getChildren() { return children; }
        public boolean hasChildren() { return children != null && !children.isEmpty(); }
    }
}

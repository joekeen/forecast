package au.id.keen.forecast.view;

import au.id.keen.forecast.view.account.AccountsView;
import au.id.keen.forecast.view.scheduled.ScheduledView;
import au.id.keen.forecast.view.tag.TagsView;
import au.id.keen.forecast.view.transaction.TransactionsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;

import java.util.HashMap;
import java.util.Map;

@CssImport("./styles/styles.css")
public class MainView extends AppLayout implements BeforeEnterObserver {

    private Tabs tabs = new Tabs();
    private Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();

    public MainView() {
        addMenuTab("Dashboard", DefaultView.class);
        addMenuTab("Accounts", AccountsView.class);
        addMenuTab("Scheduled", ScheduledView.class);
        addMenuTab("Tags", TagsView.class);
        addMenuTab("Transactions", TransactionsView.class);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
        setupNavbar();
    }

    private void setupNavbar() {
        Anchor logout = new Anchor("logout", "Log out");
        logout.addClassName("logout");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logout
        );
        header.addClassName("header");
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void addMenuTab(String label, Class<? extends Component> target) {
        Tab tab = new Tab(new RouterLink(label, target));
        navigationTargetToTab.put(target, tab);
        tabs.add(tab);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tabs.setSelectedTab(navigationTargetToTab.get(event.getNavigationTarget()));
    }
}
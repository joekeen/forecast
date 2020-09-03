package au.id.keen.forecast.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
public class DefaultView extends VerticalLayout {
    public DefaultView() {
        add(new H2("Pay scheduled"));
    }
}
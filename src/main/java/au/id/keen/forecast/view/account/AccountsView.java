package au.id.keen.forecast.view.account;

import au.id.keen.forecast.view.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "accounts", layout = MainView.class)
public class AccountsView extends VerticalLayout {

    public AccountsView() {
    }
}
package au.id.keen.forecast.view.transaction;

import au.id.keen.forecast.entity.dto.TransactionDTO;
import au.id.keen.forecast.entity.param.TransactionDaoParam;
import au.id.keen.forecast.service.TransactionService;
import au.id.keen.forecast.view.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaddon.CustomMediaQuery;

@Route(value = "transactions", layout = MainView.class)
public class TransactionsView extends VerticalLayout {

    private final TransactionService transactionService;

    private Grid<TransactionDTO> grid = new Grid<>();

    public TransactionsView(TransactionService pTransactionService) {
        this.transactionService = pTransactionService;

        setPadding(false);
        setSizeFull();
        configureGrid();

        CustomMediaQuery tableStackQuery = new CustomMediaQuery(this::toggleColumn);
        tableStackQuery.setQuery("(min-width: 600px)");
        add(tableStackQuery, getToolBar(), grid);

    }

    private Component getToolBar() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(true);

        layout.setWidthFull();
        layout.addClassName("toolbar");

        Button button = new Button("Add", new Icon(VaadinIcon.PLUS)); // TODO: click handler
//        button.getElement().getStyle().set("margin-left", "auto");
        H3 header = new H3("Transactions");
        layout.add(header, button);
        layout.expand(header);

        layout.setAlignItems(Alignment.END);

        return layout;
    }

    private void configureGrid() {
        grid.setSizeFull();

        TransactionDaoParam param = new TransactionDaoParam();
        param.setUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        CallbackDataProvider<TransactionDTO, Void> provider = DataProvider
                .fromCallbacks(query -> {
                            param.setOffset((long) query.getOffset());
                            param.setLimit((long) query.getLimit());
                            // TODO: order?
//                            param.setOrderSpecifiers(new OrderSpecifier[]{QTransaction.transaction.date.desc()});
                            return transactionService.find(param).stream();
                        },
                        query -> Long.valueOf(transactionService.count(param)).intValue()
                );
        grid.setDataProvider(provider);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addColumn(TemplateRenderer.<TransactionDTO>of(
                "<dl>" +
                        "<dt>Date</dt><dd>[[item.date]]</dd>" +
                        "<dt>Amount</dt><dd>[[item.amount]]</dd>" +
                        "<dt>Account</dt><dd>[[item.account]]</dd>" +
                        "<dt>Tags</dt><dd>[[item.tags]]</dd>" +
                        "<dt>Note</dt><dd>[[item.note]]</dd>" +
                        "</dl>")
                .withProperty("date", t -> t.getDate().toString())
                .withProperty("amount", t -> t.getAmount().toString())
                .withProperty("account", t -> t.getAccount().getName())
                .withProperty("tags", t -> StringUtils.join(t.getTagsNames(), ", "))
                .withProperty("note", TransactionDTO::getNote)
        ).setKey("table-stack").setVisible(false);

        grid.addColumn(TransactionDTO::getDate).setHeader("Date");
        grid.addColumn(TransactionDTO::getAmount).setHeader("Amount").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(t -> t.getAccount().getName()).setHeader("Account");
        grid.addColumn(t -> StringUtils.join(t.getTagsNames(), ", ")).setHeader("Tags");
        grid.addColumn(TransactionDTO::getNote).setHeader("Note");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
//        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
//        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void toggleColumn(boolean visible){
        grid.getColumns().forEach(c -> c.setVisible(visible));
        grid.getColumnByKey("table-stack").setVisible(!visible);
    }
}
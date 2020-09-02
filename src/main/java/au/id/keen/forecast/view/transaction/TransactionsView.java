package au.id.keen.forecast.view.transaction;

import au.id.keen.forecast.entity.dto.TagDTO;
import au.id.keen.forecast.entity.dto.TransactionDTO;
import au.id.keen.forecast.entity.param.TransactionDaoParam;
import au.id.keen.forecast.service.TransactionService;
import au.id.keen.forecast.view.MainView;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Collectors;

@Route(value = "transactions", layout = MainView.class)
public class TransactionsView extends VerticalLayout {

    private final TransactionService transactionService;

    private Grid<TransactionDTO> grid = new Grid<>();

    public TransactionsView(TransactionService pTransactionService) {
        this.transactionService = pTransactionService;

        setSizeFull();
        configureGrid();

        add(grid);

    }

    private void configureGrid() {
        grid.setSizeFull();

        TransactionDaoParam param = new TransactionDaoParam();
        param.setUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        CallbackDataProvider<TransactionDTO, Void> provider = DataProvider
                .fromCallbacks(query -> {
                            param.setPage(query.getOffset() / query.getLimit());
                            param.setSize(query.getLimit());
                            // TODO: order?
//                            param.setOrderSpecifiers(new OrderSpecifier[]{QTransaction.transaction.date.desc()});
                            return transactionService.find(param).stream();
                        },
                        query -> Long.valueOf(transactionService.count(param)).intValue()
                );
        grid.setDataProvider(provider);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addColumn(TransactionDTO::getDate).setHeader("Date");
        grid.addColumn(TransactionDTO::getAmount).setHeader("Amount").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(t -> t.getAccount().getName()).setHeader("Account");
        grid.addColumn(t -> StringUtils.join(t.getTagsNames(), ", ")).setHeader("Tags");
        grid.addColumn(TransactionDTO::getNote).setHeader("Note");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
//        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }
}
package au.id.keen.forecast.view.scheduled;

import au.id.keen.forecast.entity.Transaction;
import au.id.keen.forecast.entity.dto.TagDTO;
import au.id.keen.forecast.entity.dto.TransactionDTO;
import au.id.keen.forecast.entity.dto.TransactionScheduleDTO;
import au.id.keen.forecast.entity.param.TagDaoParam;
import au.id.keen.forecast.entity.param.TransactionScheduleDaoParam;
import au.id.keen.forecast.service.TagService;
import au.id.keen.forecast.service.TransactionScheduleService;
import au.id.keen.forecast.view.MainView;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;
import java.util.List;

@Route(value = "scheduled", layout = MainView.class)
public class ScheduledView extends VerticalLayout {

    private final TransactionScheduleService transactionScheduleService;

    private Grid<TransactionScheduleDTO> grid = new Grid<>();

    public ScheduledView(TransactionScheduleService pTransactionScheduleService) {
        this.transactionScheduleService = pTransactionScheduleService;

        setSizeFull();
        configureGrid();

        add(grid);

        updateGrid();
    }

    private void updateGrid() {
        TransactionScheduleDaoParam param = new TransactionScheduleDaoParam();
        param.setUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        grid.setItems(transactionScheduleService.find(param));
    }

    private void configureGrid() {
        grid.setSizeFull();

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addColumn(TransactionScheduleDTO::getStartDate).setHeader("Date");
        grid.addColumn(TransactionScheduleDTO::getAmount).setHeader("Amount").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(t -> t.getAccount().getName()).setHeader("Account");
        grid.addColumn(t -> StringUtils.join(t.getTagsNames(), ", ")).setHeader("Tags");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }
}
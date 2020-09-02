package au.id.keen.forecast.view.tag;

import au.id.keen.forecast.entity.dto.TagDTO;
import au.id.keen.forecast.entity.param.TagDaoParam;
import au.id.keen.forecast.service.TagService;
import au.id.keen.forecast.view.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;
import java.util.List;

@Route(value = "tags", layout = MainView.class)
public class TagsView extends VerticalLayout {

    private final TagService tagService;

    private Grid<TagDTO> grid = new Grid<>();

    public TagsView(TagService pTagService) {
        this.tagService = pTagService;

        setSizeFull();
        configureGrid();

        add(grid);

        updateGrid();
    }

    private void updateGrid() {
        TagDaoParam param = new TagDaoParam();
        param.setUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<TagDTO> tags = tagService.findDTO(param);
        grid.setItems(tags);
    }

    private void configureGrid() {
        grid.setSizeFull();
        Grid.Column<TagDTO> nameColumn =
                grid.addColumn(TagDTO::getName)
                .setHeader("Name")
                .setComparator(Comparator.comparing(t -> t.getName().toLowerCase()));

        grid.sort(GridSortOrder.asc(nameColumn).build());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }
}
package au.id.keen.forecast.view.tag;

import au.id.keen.forecast.entity.dto.TagDTO;
import au.id.keen.forecast.entity.param.TagDaoParam;
import au.id.keen.forecast.service.TagService;
import au.id.keen.forecast.view.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;
import java.util.List;

@Route(value = "tags", layout = MainView.class)
public class TagsView extends VerticalLayout {

    private final TagService tagService;

    private Grid<TagDTO> grid = new Grid<>();
    private TagForm form;

    public TagsView(TagService pTagService) {
        setPadding(false);

        this.tagService = pTagService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new TagForm();
        form.addListener(TagForm.SaveEvent.class, this::saveTag);
        form.addListener(TagForm.CloseEvent.class, e -> closeEditor());
//        form.addListener(TagForm.DeleteEvent.class, this::deleteTag);

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);

        updateGrid();
        closeEditor();
    }

    private Component getToolBar() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(true);

        layout.setWidthFull();
        layout.addClassName("toolbar");

        Button button = new Button("Add", new Icon(VaadinIcon.PLUS), click -> addTag());
        H3 header = new H3("Tags");
        layout.add(header, button);
        layout.expand(header);

        layout.setAlignItems(Alignment.END);

        return layout;
    }

    private void updateGrid() {
        TagDaoParam param = new TagDaoParam();
        param.setUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<TagDTO> tags = tagService.findDTO(param);
        grid.setItems(tags);
    }

    private void closeEditor() {
        form.setTag(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addClassName("grid");
        Grid.Column<TagDTO> nameColumn =
                grid.addColumn(TagDTO::getName)
                        .setHeader("Name")
                        .setComparator(Comparator.comparing(t -> t.getName().toLowerCase()));

        grid.sort(GridSortOrder.asc(nameColumn).build());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event ->
                editTag(event.getValue()));
    }

    /*
    Tag related
     */
    private void saveTag(TagForm.SaveEvent event) {
        // TODO: save new/updated tag
//        tagService.saveOrUpdate
        updateGrid();
        closeEditor();
    }

    private void addTag() {
        grid.asSingleSelect().clear();
        editTag(new TagDTO());
    }

    private void editTag(TagDTO dto) {
        if (dto == null) {
            closeEditor();
        } else {
            form.setTag(dto);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
package au.id.keen.forecast.view.tag;

import au.id.keen.forecast.entity.dto.TagDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class TagForm extends FormLayout {

    private TagDTO tag;

    TextField name = new TextField("Name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");

    Binder<TagDTO> binder = new BeanValidationBinder<>(TagDTO.class);

    public TagForm() {
        addClassName("form");

        binder.bindInstanceFields(this);

        add(name, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        delete.addClickListener(event -> {
            new Notification("Not implemented", 1000).open();
        });

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(tag);
            fireEvent(new SaveEvent(this, tag));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public void setTag(TagDTO tag) {
        this.tag = tag;
        binder.readBean(tag);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class TagFormEvent extends ComponentEvent<TagForm> {
        private final TagDTO tag;

        protected TagFormEvent(TagForm source, TagDTO tag) {
            super(source, false);
            this.tag = tag;
        }

        public TagDTO getTag() {
            return tag;
        }
    }

    public static class SaveEvent extends TagFormEvent {
        SaveEvent(TagForm source, TagDTO tag) {
            super(source, tag);
        }
    }

    public static class DeleteEvent extends TagFormEvent {
        DeleteEvent(TagForm source, TagDTO tag) {
            super(source, tag);
        }
    }

    public static class CloseEvent extends TagFormEvent {
        CloseEvent(TagForm source) {
            super(source, null);
        }
    }

}

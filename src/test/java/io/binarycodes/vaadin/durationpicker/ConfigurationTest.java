package io.binarycodes.vaadin.durationpicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.browserless.BrowserlessTest;
import com.vaadin.browserless.ViewPackages;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that the {@link Configuration} options surfaced through
 * {@link DurationPicker} and {@link DurationPicker.Builder} are honoured at
 * runtime: only requested units appear in the popup, custom unit labels are
 * used, and the close-button label is the one supplied by the caller.
 */
@ViewPackages
class ConfigurationTest extends BrowserlessTest {

    private ConfigurationView view;

    @BeforeEach
    void openView() {
        this.view = navigate(ConfigurationView.class);
    }

    @Test
    void varargsConstructor_appliesFieldLabelToInnerTextField() {
        final TextField field = innerTextField(this.view.hoursMinutesPicker);
        Assertions.assertEquals("Time spent", field.getLabel());
    }

    @Test
    void varargsConstructor_popupContainsOnlyHoursAndMinutesWithDefaultLabels() {
        openPopup(this.view.hoursMinutesPicker);

        final List<String> labels = popupUnitLabels(this.view.hoursMinutesPicker);

        Assertions.assertEquals(List.of("h", "m"), labels);
    }

    @Test
    void varargsConstructor_usesCustomCloseButtonLabel() {
        openPopup(this.view.hoursMinutesPicker);

        Assertions.assertEquals("Confirm", popupCloseButton(this.view.hoursMinutesPicker).getText());
    }

    @Test
    void builderWithSingleUnit_popupContainsOnlyThatUnit() {
        openPopup(this.view.hoursOnlyPicker);

        Assertions.assertEquals(List.of("h"), popupUnitLabels(this.view.hoursOnlyPicker));
        Assertions.assertEquals("Save", popupCloseButton(this.view.hoursOnlyPicker).getText());
    }

    @Test
    void builderCustomLabels_popupUsesSuppliedUnitLabels() {
        openPopup(this.view.labeledPicker);

        Assertions.assertEquals(
                List.of("days", "hrs", "min", "sec"),
                popupUnitLabels(this.view.labeledPicker));
    }

    @Test
    void builderCustomLabels_popupUsesSuppliedCloseLabel() {
        openPopup(this.view.labeledPicker);

        Assertions.assertEquals("Done", popupCloseButton(this.view.labeledPicker).getText());
    }

    private void openPopup(DurationPicker picker) {
        final Button popupButton = findAll(innerTextField(picker), Button.class).getFirst();
        test(popupButton).click();
    }

    private List<String> popupUnitLabels(DurationPicker picker) {
        return findAll(popupView(picker), IntegerField.class).stream()
                .map(IntegerField::getLabel)
                .toList();
    }

    private Button popupCloseButton(DurationPicker picker) {
        // The popup view holds the close button as a direct child after the
        // wrapper that contains the integer fields.
        return popupView(picker).getChildren()
                .filter(Button.class::isInstance)
                .map(Button.class::cast)
                .findFirst()
                .orElseThrow(() -> new AssertionError("close button missing from popup"));
    }

    private DurationPickerPopupView popupView(DurationPicker picker) {
        final Popover popover = picker.getChildren()
                .filter(Popover.class::isInstance)
                .map(Popover.class::cast)
                .findFirst()
                .orElseThrow(() -> new AssertionError("picker has no Popover child"));
        return findFirst(popover, DurationPickerPopupView.class)
                .orElseThrow(() -> new AssertionError("popup was not populated -- did you call openPopup?"));
    }

    private static TextField innerTextField(DurationPicker picker) {
        return picker.getChildren()
                .filter(TextField.class::isInstance)
                .map(TextField.class::cast)
                .findFirst()
                .orElseThrow(() -> new AssertionError("picker has no TextField child"));
    }

    private static <T extends Component> Optional<T> findFirst(Component root, Class<T> type) {
        final List<T> matches = findAll(root, type);
        return matches.isEmpty() ? Optional.empty() : Optional.of(matches.getFirst());
    }

    private static <T extends Component> List<T> findAll(Component root, Class<T> type) {
        final List<T> result = new ArrayList<>();
        walk(root, c -> {
            if (type.isInstance(c)) {
                result.add(type.cast(c));
            }
        });
        return result;
    }

    private static void walk(Component root, Consumer<Component> visitor) {
        root.getChildren().forEach(child -> {
            visitor.accept(child);
            walk(child, visitor);
        });
    }
}

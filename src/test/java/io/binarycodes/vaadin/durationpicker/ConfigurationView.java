package io.binarycodes.vaadin.durationpicker;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Test view that exposes pickers configured with different units and labels.
 * The pickers are public so {@link ConfigurationTest} can inspect their state
 * without going through reflection.
 */
@Route("configuration")
public class ConfigurationView extends VerticalLayout {

    public final DurationPicker hoursMinutesPicker;
    public final DurationPicker hoursOnlyPicker;
    public final DurationPicker labeledPicker;

    public ConfigurationView() {
        // Only hours and minutes are configured -- days/seconds must not appear.
        this.hoursMinutesPicker = new DurationPicker(
                "Time spent",
                "Confirm",
                DurationUnit.HOURS,
                DurationUnit.MINUTES);

        // A single-unit picker built via the builder. The builder makes it easy
        // to verify the popup renders exactly the fields you asked for.
        this.hoursOnlyPicker = new DurationPicker.Builder()
                .fieldLabel("Hours")
                .closePopupLabel("Save")
                .hours()
                .build();

        // Picker with fully customised unit labels and a customised close
        // button label.
        this.labeledPicker = new DurationPicker.Builder()
                .fieldLabel("Custom")
                .closePopupLabel("Done")
                .days()
                .hours()
                .minutes()
                .seconds()
                .customLabels("days", "hrs", "min", "sec")
                .build();

        add(this.hoursMinutesPicker, this.hoursOnlyPicker, this.labeledPicker);
    }
}

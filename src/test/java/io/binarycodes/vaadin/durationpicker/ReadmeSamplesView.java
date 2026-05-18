package io.binarycodes.vaadin.durationpicker;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

/**
 * Mirrors the snippets in README.md. Each public field is exercised by
 * {@link ReadmeSamplesTest}, so the snippets stay in sync with real behaviour.
 */
@Route("readme-samples")
public class ReadmeSamplesView extends VerticalLayout {

    public final DurationPicker simplePicker;
    public final List<Duration> simplePickerChanges = new ArrayList<>();

    public final DurationPicker hoursMinutesPicker;

    public final DurationPicker builderPicker;

    public final DurationPicker boundPicker;
    public final Binder<Task> binder;

    public ReadmeSamplesView() {
        // Sample 1: simple constructor with a value-change listener.
        this.simplePicker = new DurationPicker("Duration", "Ok");
        this.simplePicker.addValueChangeListener(event -> this.simplePickerChanges.add(event.getValue()));

        // Sample 2: restricting the available units via varargs.
        this.hoursMinutesPicker = new DurationPicker(
                "Time spent",
                "Ok",
                DurationUnit.HOURS,
                DurationUnit.MINUTES);

        // Sample 3: builder with step values and custom unit labels.
        this.builderPicker = new DurationPicker.Builder()
                .fieldLabel("Duration")
                .closePopupLabel("Done")
                .days()
                .hours()
                .minutes(5)
                .customLabels("days", "hrs", "min", "sec")
                .build();

        // Sample 4: Binder integration on a Duration property.
        this.boundPicker = new DurationPicker("Estimate", "Ok");
        this.binder = new Binder<>(Task.class);
        this.binder.forField(this.boundPicker).bind(Task::getEstimate, Task::setEstimate);

        add(this.simplePicker, this.hoursMinutesPicker, this.builderPicker, this.boundPicker);
    }

    public static class Task {
        private Duration estimate;

        public Duration getEstimate() {
            return this.estimate;
        }

        public void setEstimate(Duration estimate) {
            this.estimate = estimate;
        }
    }
}

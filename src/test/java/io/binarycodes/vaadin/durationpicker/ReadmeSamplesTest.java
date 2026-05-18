package io.binarycodes.vaadin.durationpicker;

import java.time.Duration;

import com.vaadin.browserless.BrowserlessTest;
import com.vaadin.browserless.ViewPackages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies the snippets shown in README.md by exercising the public API of
 * {@link DurationPicker} from {@link ReadmeSamplesView}.
 */
@ViewPackages
class ReadmeSamplesTest extends BrowserlessTest {

    private ReadmeSamplesView view;

    @BeforeEach
    void openView() {
        this.view = navigate(ReadmeSamplesView.class);
    }

    @Test
    void simpleConstructor_storesValueAndNotifiesListener() {
        final Duration target = Duration.ofHours(2).plusMinutes(30);

        this.view.simplePicker.setValue(target);

        Assertions.assertEquals(target, this.view.simplePicker.getValue());
        Assertions.assertTrue(this.view.simplePickerChanges.contains(target),
                "the value-change listener from the README should observe the new value");
    }

    @Test
    void simpleConstructor_emptyValueIsZeroDuration() {
        Assertions.assertEquals(Duration.ZERO, this.view.simplePicker.getEmptyValue());
    }

    @Test
    void varargsConstructor_storesValueWithRestrictedUnits() {
        final Duration target = Duration.ofHours(1).plusMinutes(45);

        this.view.hoursMinutesPicker.setValue(target);

        Assertions.assertEquals(target, this.view.hoursMinutesPicker.getValue());
    }

    @Test
    void builder_buildsConfiguredPickerThatHoldsValues() {
        final Duration target = Duration.ofDays(1).plusHours(3).plusMinutes(10);

        this.view.builderPicker.setValue(target);

        // The minutes(5) step in the builder rounds non-multiples up to the
        // next step; multiples of 5 must round-trip unchanged.
        Assertions.assertEquals(target, this.view.builderPicker.getValue());
    }

    @Test
    void binder_writesAndReadsDurationOnBean() throws Exception {
        final ReadmeSamplesView.Task task = new ReadmeSamplesView.Task();
        final Duration estimate = Duration.ofHours(4);

        this.view.boundPicker.setValue(estimate);
        this.view.binder.writeBean(task);

        Assertions.assertEquals(estimate, task.getEstimate());

        task.setEstimate(Duration.ofMinutes(15));
        this.view.binder.readBean(task);

        Assertions.assertEquals(Duration.ofMinutes(15), this.view.boundPicker.getValue());
    }
}

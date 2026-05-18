package io.binarycodes.vaadin.durationpicker;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.vaadin.addons.dramafinder.AbstractBasePlaywrightIT;
import org.vaadin.addons.dramafinder.element.ButtonElement;
import org.vaadin.addons.dramafinder.element.IntegerFieldElement;
import org.vaadin.addons.dramafinder.element.PopoverElement;
import org.vaadin.addons.dramafinder.element.TextFieldElement;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(classes = TestApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class PlaywrightSmokeIT extends AbstractBasePlaywrightIT {

    @LocalServerPort
    private int port;

    @Override
    public String getUrl() {
        return "http://localhost:" + this.port + "/";
    }

    @Override
    public String getView() {
        return "configuration";
    }

    @Test
    void typedDuration_isAcceptedAndPreserved() {
        final var timeSpent = TextFieldElement.getByLabel(this.page, "Time spent");
        timeSpent.assertVisible();

        timeSpent.setValue("1h30m");
        timeSpent.assertValue("1h30m");
    }

    @Test
    void popup_varargsPickerShowsOnlyHoursAndMinutes() {
        final var content = openPopupFor("Time spent");

        assertThat(content.locator(IntegerFieldElement.FIELD_TAG_NAME)).hasCount(2);
        assertThat(unitLabelLocator(content)).hasText(new String[] {"h", "m"});
    }

    @Test
    void popup_singleUnitBuilderShowsOnlyHours() {
        final var content = openPopupFor("Hours");

        assertThat(content.locator(IntegerFieldElement.FIELD_TAG_NAME)).hasCount(1);
        assertThat(unitLabelLocator(content)).hasText(new String[] {"h"});
    }

    @Test
    void popup_appliesEnteredValuesToTextFieldOnClose_hm() {
        final var content = openPopupFor("Time spent");

        final var fields = content.locator(IntegerFieldElement.FIELD_TAG_NAME);
        new IntegerFieldElement(fields.nth(0)).setValue("2");
        new IntegerFieldElement(fields.nth(1)).setValue("15");

        ButtonElement.getByText(content, "Confirm").click();

        TextFieldElement.getByLabel(this.page, "Time spent").assertValue("2h15m");
    }

    @Test
    void popup_customLabelsBuilderShowsAllUnitsWithSuppliedLabels() {
        final var content = openPopupFor("Custom");

        assertThat(content.locator(IntegerFieldElement.FIELD_TAG_NAME)).hasCount(4);
        assertThat(unitLabelLocator(content)).hasText(new String[] {"days", "hrs", "min", "sec"});
    }

    @Test
    void popup_appliesEnteredValuesToTextFieldOnClose_dhms() {
        final var content = openPopupFor("Custom");

        final var fields = content.locator(IntegerFieldElement.FIELD_TAG_NAME);
        new IntegerFieldElement(fields.nth(0)).setValue("2");
        new IntegerFieldElement(fields.nth(1)).setValue("23");
        new IntegerFieldElement(fields.nth(2)).setValue("15");
        new IntegerFieldElement(fields.nth(3)).setValue("5");

        ButtonElement.getByText(content, "Done").click();

        TextFieldElement.getByLabel(this.page, "Custom").assertValue("2d23h15m5s");
    }


    private Locator openPopupFor(String fieldLabel) {
        final var field = TextFieldElement.getByLabel(this.page, fieldLabel);
        field.getSuffixLocator().click();

        final var popover = new PopoverElement(this.page);
        popover.assertOpen();
        return popover.getContentLocator();
    }

    /**
     * Locates the slotted {@code <label>} child of every {@code vaadin-integer-field} in
     * the popover, in DOM order. Vaadin renders the label property as a child element
     * with {@code slot="label"}, so its text content is the configured unit label.
     */
    private static Locator unitLabelLocator(Locator content) {
        return content.locator(IntegerFieldElement.FIELD_TAG_NAME + " > label");
    }
}

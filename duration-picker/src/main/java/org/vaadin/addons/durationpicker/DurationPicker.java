package org.vaadin.addons.durationpicker;


import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class DurationPicker extends CustomField<Duration> {

    /* configurations */
    private final Configuration configuration;

    private final String textFieldId;
    private final Popover popup;
    private Registration popupCloseRegistration;

    /* the eventual value of this field */
    private DurationData value;

    private TextField field;
    private Button popupButton;

    public DurationPicker(String fieldLabel, String closeButtonLabel) {
        this(new Configuration(fieldLabel, closeButtonLabel, Arrays.asList(DurationUnit.values())));
    }

    public DurationPicker(String fieldLabel, String closeButtonLabel, final DurationUnit... units) {
        this(new Configuration(fieldLabel, closeButtonLabel, Arrays.asList(units)));
    }

    private DurationPicker(final Configuration configuration) {
        this.configuration = configuration;

        this.value = new DurationData(configuration);
        this.textFieldId = UUID.randomUUID().toString();

        this.popup = new Popover();
        this.popup.setFor(textFieldId);
        this.popup.setPosition(PopoverPosition.BOTTOM);
        this.popup.setOpenOnClick(false);
        this.add(popup);

        initView();
    }

    private void initView() {
        this.field = new TextField(configuration.getFieldLabel());
        this.field.setId(textFieldId);

        this.field.setAllowedCharPattern("[0-9hdms]");
        this.field.addValueChangeListener(event -> {
            var interimValue = new DurationData(configuration, event.getValue());

            if (interimValue.isValid()) {
                this.value = interimValue;
                /* after resolving step values, the input string may be different */
                if (!event.getValue().equals(this.value.toString())) {
                    this.field.setValue(this.value.toString());
                }
            } else {
                this.value = new DurationData(this.configuration);
                this.setInvalid(true);
            }
        });

        this.popupButton = new Button(VaadinIcon.CLOCK.create(), clickEvent -> showDurationPickerPopup(field));
        this.field.setSuffixComponent(this.popupButton);

        add(field);
    }

    private void showDurationPickerPopup(TextField field) {
        this.popup.removeAll();
        if (this.popupCloseRegistration != null) {
            this.popupCloseRegistration.remove();
        }

        var dialog = new DurationPickerPopupView(value, configuration, this.popup::close);
        this.popup.add(dialog);
        this.popup.open();

        this.popupCloseRegistration = popup.addOpenedChangeListener(event -> {
            field.setValue(dialog.getValue().toString());
            updateValue();
        });
    }

    @Override
    public Duration getEmptyValue() {
        return new DurationData(this.configuration).getDuration();
    }

    @Override
    protected Duration generateModelValue() {
        return this.value.getDuration();
    }

    @Override
    protected void setPresentationValue(Duration duration) {
        this.value = new DurationData(this.configuration, duration);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        this.field.setReadOnly(readOnly);
        this.popupButton.setEnabled(!readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.field.setEnabled(enabled);
    }

    @Override
    public void setInvalid(boolean invalid) {
        super.setInvalid(invalid);
        // There is a timing bug, workaround
        field.getElement().executeJs("return 0")
                .then(res -> field.setInvalid(invalid));
    }

    public static class Builder {
        private final Configuration configuration;

        public Builder() {
            configuration = new Configuration();
        }

        public Builder fieldLabel(String fieldLabel) {
            this.configuration.setFieldLabel(fieldLabel);
            return this;
        }

        public Builder closePopupLabel(String closePopupLabel) {
            this.configuration.setClosePopupLabel(closePopupLabel);
            return this;
        }

        public Builder days() {
            this.configuration.addUnit(DurationUnit.DAYS);
            return this;
        }

        public Builder hours() {
            this.configuration.addUnit(DurationUnit.HOURS);
            return this;
        }

        public Builder hours(int stepValue) {
            this.configuration.addUnit(DurationUnit.HOURS);
            this.configuration.setHoursStepValue(stepValue);
            return this;
        }

        public Builder minutes() {
            this.configuration.addUnit(DurationUnit.MINUTES);
            return this;
        }

        public Builder minutes(int stepValue) {
            this.configuration.addUnit(DurationUnit.MINUTES);
            this.configuration.setMinutesStepValue(stepValue);
            return this;
        }

        public Builder seconds() {
            this.configuration.addUnit(DurationUnit.SECONDS);
            return this;
        }

        public Builder seconds(int stepValue) {
            this.configuration.addUnit(DurationUnit.SECONDS);
            this.configuration.setSecondsStepValue(stepValue);
            return this;
        }

        public Builder customLabels(String daysLabel, String hoursLabel, String minutesLabel, String secondsLabel) {
            this.configuration.setDaysLabel(daysLabel);
            this.configuration.setHoursLabel(hoursLabel);
            this.configuration.setMinutesLabel(minutesLabel);
            this.configuration.setSecondsLabel(secondsLabel);
            return this;
        }

        public DurationPicker build() {
            return new DurationPicker(configuration);
        }
    }
}

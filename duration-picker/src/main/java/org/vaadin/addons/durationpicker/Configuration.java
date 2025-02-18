package org.vaadin.addons.durationpicker;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private static final int DEFAULT_STEP_VALUE = 1;

    private String fieldLabel;
    private String closePopupLabel;

    private final List<DurationUnit> units;
    private int hoursStepValue;
    private int minutesStepValue;
    private int secondsStepValue;

    private String daysLabel;
    private String hoursLabel;
    private String minutesLabel;
    private String secondsLabel;

    Configuration() {
        units = new ArrayList<>();
        hoursStepValue = DEFAULT_STEP_VALUE;
        minutesStepValue = DEFAULT_STEP_VALUE;
        secondsStepValue = DEFAULT_STEP_VALUE;

        fieldLabel = "Duration";
        closePopupLabel = "Ok";

        daysLabel = "d";
        hoursLabel = "h";
        minutesLabel = "m";
        secondsLabel = "s";
    }

    public Configuration(String fieldLabel, String closePopupLabel, List<DurationUnit> units) {
        this();
        this.fieldLabel = fieldLabel;
        this.closePopupLabel = closePopupLabel;
        this.units.addAll(units);
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getClosePopupLabel() {
        return closePopupLabel;
    }

    public void setClosePopupLabel(String closePopupLabel) {
        this.closePopupLabel = closePopupLabel;
    }

    public void addUnit(DurationUnit durationUnit) {
        this.units.add(durationUnit);
    }

    public List<DurationUnit> getUnits() {
        return units;
    }

    public int getDaysStepValue() {
        return DEFAULT_STEP_VALUE;
    }

    public int getHoursStepValue() {
        return hoursStepValue;
    }

    public void setHoursStepValue(int hoursStepValue) {
        this.hoursStepValue = hoursStepValue;
    }

    public int getMinutesStepValue() {
        return minutesStepValue;
    }

    public void setMinutesStepValue(int minutesStepValue) {
        this.minutesStepValue = minutesStepValue;
    }

    public int getSecondsStepValue() {
        return secondsStepValue;
    }

    public void setSecondsStepValue(int secondsStepValue) {
        this.secondsStepValue = secondsStepValue;
    }

    public String getDaysLabel() {
        return daysLabel;
    }

    public void setDaysLabel(String daysLabel) {
        this.daysLabel = daysLabel;
    }

    public String getHoursLabel() {
        return hoursLabel;
    }

    public void setHoursLabel(String hoursLabel) {
        this.hoursLabel = hoursLabel;
    }

    public String getMinutesLabel() {
        return minutesLabel;
    }

    public void setMinutesLabel(String minutesLabel) {
        this.minutesLabel = minutesLabel;
    }

    public String getSecondsLabel() {
        return secondsLabel;
    }

    public void setSecondsLabel(String secondsLabel) {
        this.secondsLabel = secondsLabel;
    }
}

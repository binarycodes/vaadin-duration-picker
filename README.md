# Duration Picker

A Vaadin Flow component for entering durations (days, hours, minutes, seconds). It is a `CustomField<Duration>`, so it integrates with `Binder` and produces a standard `java.time.Duration` value.

The field offers two input modes:

- Type a duration directly in the text field using a compact syntax like `1d2h30m15s`.
- Open the popup (clock icon) and pick the value with steppers.

## Add to your project

The addon is published to Maven Central. Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.binarycodes.vaadin</groupId>
    <artifactId>duration-picker</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### Simple

Pass a field label and the popup's close-button label. By default all units (days, hours, minutes, seconds) are enabled.

```java
DurationPicker picker = new DurationPicker("Duration", "Ok");

picker.addValueChangeListener(event -> {
    Duration duration = event.getValue();
    // ...
});
```

### Restricting the available units

Pass only the units you want as varargs:

```java
DurationPicker picker = new DurationPicker(
        "Time spent",
        "Ok",
        DurationUnit.HOURS,
        DurationUnit.MINUTES);
```

### Builder

For finer control (step values, custom unit labels) use the builder:

```java
DurationPicker picker = new DurationPicker.Builder()
        .fieldLabel("Duration")
        .closePopupLabel("Done")
        .days()
        .hours()
        .minutes(5)   // step value: minutes increment in multiples of 5
        .customLabels("days", "hrs", "min", "sec")
        .build();
```

### Binder integration

Because `DurationPicker` is a `CustomField<Duration>`, it binds directly to a `Duration` property:

```java
Binder<Task> binder = new Binder<>(Task.class);
binder.forField(picker).bind(Task::getEstimate, Task::setEstimate);
```

## License

Apache License 2.0. See [LICENSE.md](LICENSE.md).

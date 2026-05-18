package io.binarycodes.vaadin.durationpicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
public class TestApp implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }
}

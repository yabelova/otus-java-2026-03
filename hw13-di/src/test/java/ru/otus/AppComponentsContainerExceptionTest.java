package ru.otus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.appcontainer.exceptions.AppComponentsContainerException;
import ru.otus.appcontainer.exceptions.DuplicateComponentNameException;
import ru.otus.appcontainer.exceptions.NoUniqueComponentException;
import ru.otus.appcontainer.exceptions.NoSuchComponentException;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.*;

class AppComponentsContainerExceptionTest {

    @DisplayName("Non-config class without @AppComponentsContainerConfig — AppComponentsContainerException")
    @Test
    void shouldThrowAppComponentsContainerExceptionForNonConfigClass() {
        assertThatThrownBy(() -> new AppComponentsContainerImpl(String.class))
                .isInstanceOf(AppComponentsContainerException.class);
    }

    @DisplayName("Two methods with the same name — DuplicateComponentNameException")
    @Test
    void shouldThrowDuplicateComponentNameException() {
        assertThatThrownBy(() -> new AppComponentsContainerImpl(ConfigWithDuplicateNames.class))
                .isInstanceOf(DuplicateComponentNameException.class);
    }

    @DisplayName("Multiple beans found by type — NoUniqueComponentException")
    @Test
    void shouldThrowNoUniqueComponentException() {
        var ctx = new AppComponentsContainerImpl(ConfigWithTwoSameComponents.class);

        assertThatThrownBy(() -> ctx.getAppComponent(EquationPreparer.class))
                .isInstanceOf(NoUniqueComponentException.class);
    }

    @DisplayName("No bean found by type — NoSuchComponentException")
    @Test
    void shouldThrowNoSuchComponentExceptionWhenNotFoundByType() {
        var ctx = new AppComponentsContainerImpl(ConfigWithTwoSameComponents.class);

        assertThatThrownBy(() -> ctx.getAppComponent(PlayerService.class))
                .isInstanceOf(NoSuchComponentException.class);
    }

    @DisplayName("No bean found by name — NoSuchComponentException")
    @Test
    void shouldThrowNoSuchComponentExceptionWhenNotFoundByName() {
        var ctx = new AppComponentsContainerImpl(ConfigWithTwoSameComponents.class);

        assertThatThrownBy(() -> ctx.getAppComponent("equationPreparer3"))
                .isInstanceOf(NoSuchComponentException.class);
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithDuplicateNames {

        @AppComponent(order = 1, name = "equationPreparer")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer")
        public IOService ioService() {
            return new IOServiceStreams(System.out, System.in);
        }
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithTwoSameComponents {

        @AppComponent(order = 1, name = "equationPreparer1")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer2")
        public EquationPreparer equationPreparer2() {
            return new EquationPreparerImpl();
        }
    }
}

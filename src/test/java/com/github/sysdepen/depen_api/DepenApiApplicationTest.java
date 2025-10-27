package com.github.sysdepen.depen_api;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DepenApiApplicationTest {
    @Test
    void constructor_cobreConstrutorDefault() {
        // cobre DepenApiApplication()
        DepenApiApplication app = new DepenApiApplication();
        assertNotNull(app);
    }

    @Test
    void main_deveChamarSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext ctx = mock(ConfigurableApplicationContext.class);

            mocked.when(() -> SpringApplication.run(eq(DepenApiApplication.class), any(String[].class)))
                    .thenReturn(ctx);

            DepenApiApplication.main(new String[]{});

            mocked.verify(() -> SpringApplication.run(eq(DepenApiApplication.class), any(String[].class)));
        }
    }
}

package com.crud.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SwaggerConfig.class)
public class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void openAPIBeanShouldBePresent() {
        assertThat(openAPI).isNotNull();
    }

    @Test
    void securitySchemeShouldBeConfiguredCorrectly() {
        Components components = openAPI.getComponents();
        assertThat(components).isNotNull();
        assertThat(components.getSecuritySchemes()).containsKey("bearerAuth");

        SecurityScheme scheme = components.getSecuritySchemes().get("bearerAuth");
        assertThat(scheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
        assertThat(scheme.getScheme()).isEqualTo("bearer");
        assertThat(scheme.getBearerFormat()).isEqualTo("JWT");
    }

    @Test
    void securityRequirementShouldContainBearerAuth() {
        assertThat(openAPI.getSecurity()).isNotEmpty();
        boolean found = openAPI.getSecurity().stream()
            .anyMatch(req -> req.containsKey("bearerAuth"));
        assertThat(found).isTrue();
    }
}

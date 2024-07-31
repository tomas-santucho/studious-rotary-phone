package com.agile.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OpenAPIConfig Test")
class OpenAPIConfigTest {

    private OpenAPIConfig openAPIConfig;

    @BeforeEach
    void setUp() {
        openAPIConfig = new OpenAPIConfig();
    }

    @Nested
    @DisplayName("Unit Tests for OpenAPIConfig")
    class UnitTests {

        @Test
        @DisplayName("Given OpenAPIConfig, When customOpenAPI is called, Then it should return a configured OpenAPI instance")
        void givenOpenAPIConfig_whenCustomOpenAPICalled_thenShouldReturnConfiguredOpenAPIInstance() {
            // Given

            // When
            OpenAPI openAPI = openAPIConfig.customOpenAPI();

            // Then
            assertThat(openAPI).isNotNull();
            assertThat(openAPI.getInfo()).isNotNull();
            assertThat(openAPI.getInfo().getTitle()).isEqualTo("E-Commerce API");
            assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0");
            assertThat(openAPI.getInfo().getDescription()).isEqualTo("An Ecommerce API");
        }
    }

    @Nested
    @DisplayName("Integration Tests with Spring Context")
    class IntegrationTests {

        private AnnotationConfigApplicationContext context;

        @BeforeEach
        void setUp() {
            context = new AnnotationConfigApplicationContext(OpenAPIConfig.class);
        }

        @Test
        @DisplayName("Given Spring Context, When customOpenAPI bean is retrieved, Then it should return a configured OpenAPI instance")
        void givenSpringContext_whenCustomOpenAPIRetrieved_thenShouldReturnConfiguredOpenAPIInstance() {
            // Given

            // When
            OpenAPI openAPI = context.getBean(OpenAPI.class);

            // Then
            assertThat(openAPI).isNotNull();
            assertThat(openAPI.getInfo()).isNotNull();
            assertThat(openAPI.getInfo().getTitle()).isEqualTo("E-Commerce API");
            assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0");
            assertThat(openAPI.getInfo().getDescription()).isEqualTo("An Ecommerce API");
        }
    }
}

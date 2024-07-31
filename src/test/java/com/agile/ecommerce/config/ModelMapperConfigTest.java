package com.agile.ecommerce.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ModelMapperConfig Test")
class ModelMapperConfigTest {

    private ModelMapperConfig modelMapperConfig;

    @BeforeEach
    void setUp() {
        modelMapperConfig = new ModelMapperConfig();
    }

    @Nested
    @DisplayName("Unit Tests for ModelMapperConfig")
    class UnitTests {

        @Test
        @DisplayName("Given ModelMapperConfig, When modelMapper is called, Then it should return a configured ModelMapper instance")
        void givenModelMapperConfig_whenModelMapperIsCalled_thenItShouldReturnConfiguredModelMapperInstance() {
            // Given

            // When
            ModelMapper modelMapper = modelMapperConfig.modelMapper();

            // Then
            assertThat(modelMapper).isNotNull();
            assertThat(modelMapper.getConfiguration().getMatchingStrategy()).isEqualTo(MatchingStrategies.STRICT);
            assertThat(modelMapper.getConfiguration().isFieldMatchingEnabled()).isTrue();
            assertThat(modelMapper.getConfiguration().getFieldAccessLevel()).isEqualTo(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        }
    }

    @Nested
    @DisplayName("Integration Tests with Spring Context")
    class IntegrationTests {

        private AnnotationConfigApplicationContext context;

        @BeforeEach
        void setUp() {
            context = new AnnotationConfigApplicationContext(ModelMapperConfig.class);
        }

        @Test
        @DisplayName("Given Spring Context, When ModelMapper bean is retrieved, Then it should be a singleton")
        void givenSpringContext_whenModelMapperBeanIsRetrieved_thenItShouldBeSingleton() {
            // Given

            // When
            var modelMapper1 = context.getBean(ModelMapper.class);
            var modelMapper2 = context.getBean(ModelMapper.class);

            // Then
            assertThat(modelMapper1).isNotNull();
            assertThat(modelMapper2).isNotNull();
            assertThat(modelMapper1).isSameAs(modelMapper2);
        }

        @Test
        @DisplayName("Given Spring Context, When ModelMapper bean is retrieved, Then it should be correctly configured")
        void givenSpringContext_whenModelMapperBeanIsRetrieved_thenItShouldBeCorrectlyConfigured() {
            // Given

            // When
            var modelMapper = context.getBean(ModelMapper.class);

            // Then
            assertThat(modelMapper).isNotNull();
            assertThat(modelMapper.getConfiguration().getMatchingStrategy()).isEqualTo(MatchingStrategies.STRICT);
            assertThat(modelMapper.getConfiguration().isFieldMatchingEnabled()).isTrue();
            assertThat(modelMapper.getConfiguration().getFieldAccessLevel()).isEqualTo(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        }
    }
}

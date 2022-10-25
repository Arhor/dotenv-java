package com.github.arhor.dotenv;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DotenvConfigurerTest {

    @Test
    void DotenvConfiguration_builder_method_should_return_non_null_object() {
        // when
        final DotenvConfigurer configurer = DotenvConfigurer.getInstance();

        // then
        assertThat(configurer)
            .isNotNull();
    }

    @Test
    void DotenvConfiguration$Builder_build_method_should_return_non_null_object() {
        // given
        final DotenvConfigurer configurer = DotenvConfigurer.getInstance();

        // when
        final Dotenv dotenv = configurer.load();

        // then
        assertThat(dotenv)
            .isNotNull();
    }

    @Test
    void DotenvConfiguration$Builder_includeSystemVariables_method_should_return_new_configurer_instance() {
        // given
        final DotenvConfigurer configurer = DotenvConfigurer.getInstance();

        // when
        final DotenvConfigurer configurerAfterInvocation = configurer.includeSystemVariables(true);

        // then
        assertThat(configurerAfterInvocation)
            .isNotNull()
            .isNotSameAs(configurer);
    }

    @Test
    void DotenvConfiguration$Builder_includeSystemVariables_affect_correct_config_field() {
        // given
        final boolean expectedIncludeSystemVariables = false;

        // when
        final Dotenv dotenv = DotenvConfigurer.getInstance()
            .includeSystemVariables(expectedIncludeSystemVariables)
            .load();

        // then
        assertThat(dotenv)
            .isNotNull()
            .hasFieldOrPropertyWithValue("includeSystemVariables", expectedIncludeSystemVariables);
    }

    @Test
    void DotenvConfiguration$Builder_allowOverrideSystemVariables_method_should_return_new_configurer_instance() {
        // given
        final DotenvConfigurer configurer = DotenvConfigurer.getInstance();

        // when
        final DotenvConfigurer configurerAfterInvocation = configurer.allowOverrideSystemVariables(true);

        // then
        assertThat(configurerAfterInvocation)
            .isNotNull()
            .isNotSameAs(configurer);
    }

    @Test
    void DotenvConfiguration$Builder_allowOverrideSystemVariables_affect_correct_config_field() {
        // given
        final boolean expectedAllowOverrideSystemVariables = false;

        // when
        final Dotenv dotenv = DotenvConfigurer.getInstance()
            .allowOverrideSystemVariables(expectedAllowOverrideSystemVariables)
            .load();

        // then
        assertThat(dotenv)
            .isNotNull()
            .hasFieldOrPropertyWithValue("allowOverrideSystemVariables", expectedAllowOverrideSystemVariables);
    }
}
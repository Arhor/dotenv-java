package io.github.arhor.dotenv;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class DotenvImplTest {

    @Nested
    @DisplayName("Dotenv.get(String)")
    class DotenvGetTests {
        @Test
        @DisplayName("should return an expected value for a key using default config")
        void dotenv_get_default_config_positive_test() {
            // given
            final var dotenv = Dotenv.configure().load();
            final var propertyKey = "test_key";
            final var propertyVal = "test_val";

            // when
            final var result = dotenv.get(propertyKey);

            // then
            assertThat(result)
                .isNotNull()
                .isEqualTo(propertyVal);
        }

        @Test
        @DisplayName("should return an expected value for a key using config with changed location")
        void dotenv_get_changed_location_positive_test() {
            // given
            final var dotenv = Dotenv.configure().location("./directory").load();
            final var propertyKey = "test_key";
            final var propertyVal = "test_val";

            // when
            final var result = dotenv.get(propertyKey);

            // then
            assertThat(result)
                .isNotNull()
                .isEqualTo(propertyVal);
        }

        @Test
        @DisplayName("should return an expected value for a key using config with changed location using file URI")
        void dotenv_get_changed_location_file_positive_test() {
            // given
            final var testResourcesDir = System.getenv().get("TEST_RESOURCES_DIR");
            final var dotenv = Dotenv.configure().location("file:///" + testResourcesDir).load();
            final var propertyKey = "test_key";
            final var propertyVal = "test_val";

            // when
            final var result = dotenv.get(propertyKey);

            // then
            assertThat(result)
                .isNotNull()
                .isEqualTo(propertyVal);
        }

        @Test
        @DisplayName("should throw LoadingException loading loading file by wrong path")
        void dotenv_get_changed_location_negative_test() {
            // given
            final var wrongPath = "surely/non/existing/path";

            // when
            final var result = catchThrowable(() -> Dotenv.configure().location(wrongPath).load());

            // then
            assertThat(result)
                .isInstanceOf(LoadingException.class);
        }

        @Test
        @DisplayName("should throw CyclicReferenceException resolving property with cyclic dependency")
        void dotenv_get_cyclic_dependency_negative_test() {
            // given
            final Dotenv dotenv = Dotenv.configure().load();
            final String propertyName = "a";

            // when
            final var result = catchThrowable(() -> dotenv.get(propertyName));

            // then
            assertThat(result)
                .isInstanceOf(CyclicReferenceException.class)
                .hasMessageContaining("a -> b -> c -> a");
        }

        @Test
        @DisplayName("should throw UnresolvedReferenceException resolving property with missing dependency")
        void dotenv_get_unresolved_dependency_negative_test() {
            // given
            final Dotenv dotenv = Dotenv.configure().strictMode(true).load();
            final String propertyName = "d";

            // when
            final var result = catchThrowable(() -> dotenv.get(propertyName));

            // then
            assertThat(result)
                .isInstanceOf(UnresolvedReferenceException.class)
                .hasMessage("Cannot resolve reference with name 'f', path: d -> e -> f");
        }
    }

    @Nested
    @DisplayName("Dotenv.get(String, String)")
    class DotenvGetWithDefaultTests {
    }

    @Nested
    @DisplayName("Dotenv.getRequired(String)")
    class DotenvGetRequiredTests {
        @Test
        @DisplayName("should throw MissingPropertyException resolving missing property")
        void dotenv_getRequired_missing_property_negative_test() {
            // given
            final Dotenv dotenv = Dotenv.configure().load();
            final String propertyName = "missing_property";

            // when
            final var result = catchThrowable(() -> dotenv.getRequired(propertyName));

            // then
            assertThat(result)
                .isInstanceOf(MissingPropertyException.class)
                .hasMessage("Cannot find property: 'missing_property'");
        }
    }
}

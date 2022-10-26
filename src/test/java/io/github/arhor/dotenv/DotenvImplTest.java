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
        void should_not_fail_1() {
            // given
            final Dotenv dotenv = Dotenv.configure().load();
            final String propertyName = "test_key";
            final String propertyValue = "test_value";

            // when
            final String result = dotenv.get(propertyName);

            // then
            assertThat(result)
                .isNotNull()
                .isEqualTo(propertyValue);
        }

        @Test
        void should_not_fail_2() {
            // given
            final Dotenv dotenv = Dotenv.configure().location("./directory").load();
            final String propertyName = "test_key";
            final String propertyValue = "test_value";

            // when
            final String result = dotenv.get(propertyName);

            // then
            assertThat(result)
                .isNotNull()
                .isEqualTo(propertyValue);
        }

        @Test
        void should_not_fail_3() {
            // given
            final Dotenv dotenv = Dotenv.configure().load();
            final String propertyName = "c";

            // when
            final var result = catchThrowable(() -> dotenv.get(propertyName));

            // then
            assertThat(result)
                .isInstanceOf(CyclicReferenceException.class)
                .hasMessageContaining("a -> b -> c -> a");
        }
    }

    @Nested
    @DisplayName("Dotenv.get(String, String)")
    class DotenvGetWithDefaultTests {
    }

    @Nested
    @DisplayName("Dotenv.getRequired(String)")
    class DotenvGetRequiredTests {
    }
}

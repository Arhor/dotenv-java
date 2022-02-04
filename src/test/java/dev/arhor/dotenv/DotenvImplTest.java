package dev.arhor.dotenv;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DotenvImplTest {

    @Test
    void should_not_fail() {
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
}

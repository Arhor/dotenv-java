package com.github.arhor.dotenv;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DotenvImplTest {

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
    void should_not_fail_3() throws Throwable {
        // given
        final Dotenv dotenv = Dotenv.configure().load();
        final String propertyName = "c";

        // when
        final ThrowingCallable action = () -> dotenv.get(propertyName);

        // then
        assertThatExceptionOfType(CyclicReferenceException.class)
            .isThrownBy(action)
            .withMessageContaining("a -> b -> c -> a");
    }
}

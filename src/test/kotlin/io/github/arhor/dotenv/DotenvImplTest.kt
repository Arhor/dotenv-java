package io.github.arhor.dotenv

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf

class DotenvImplTest : DescribeSpec({

    describe("Dotenv.get(String)") {
        it("should return an expected value for a key using default config") {
            // given
            val dotenv = Dotenv.configure().load()
            val propertyKey = "test_key"
            val propertyVal = "test_val"

            // when
            val result = dotenv.get(propertyKey)

            // then
            result
                .shouldNotBeNull()
                .shouldBe(propertyVal)
        }

        it("should return an expected value for a key when the value computed using several references") {
            // given
            val dotenv = Dotenv.configure().strictMode(true).load()
            val propertyKey = "lorem"
            val propertyVal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."

            // when
            val result = dotenv.get(propertyKey)

            // then
            result
                .shouldNotBeNull()
                .shouldBe(propertyVal)
        }

        it("should return null resolving missing property") {
            // given
            val dotenv = Dotenv.configure().load()
            val propertyName = "missing_property"

            // when
            val result = dotenv.get(propertyName)

            // then
            result.shouldBeNull()
        }

        it("should throw CyclicReferenceException resolving property with cyclic dependency") {
            // given
            val dotenv = Dotenv.configure().load()
            val propertyName = "a"

            // when
            val result = shouldThrowAny { dotenv.get(propertyName) }

            // then
            result
                .shouldNotBeNull()
                .shouldBeInstanceOf<CyclicReferenceException>()
                .shouldHaveMessage("Cyclic references found, path: a -> b -> c -> a")
        }

        it("should throw UnresolvedReferenceException resolving property with missing dependency") {
            // given
            val dotenv = Dotenv.configure().strictMode(true).load()
            val propertyName = "d"

            // when
            val result = shouldThrowAny { dotenv.get(propertyName) }

            // then
            result
                .shouldNotBeNull()
                .shouldBeInstanceOf<UnresolvedReferenceException>()
                .shouldHaveMessage("Cannot resolve reference with name 'f', path: d -> e -> f")
        }
    }

    describe("Dotenv.get(String, String)") {
        it("should return default value for a missing key using default config") {
            // given
            val dotenv = Dotenv.configure().load()
            val propertyKey = "missing_property"
            val propertyVal = "test_val"

            // when
            val result = dotenv.get(propertyKey, propertyVal)

            // then
            result
                .shouldNotBeNull()
                .shouldBe(propertyVal)
        }

        it("should return default null resolving missing property") {
            // given
            val dotenv = Dotenv.configure().load()
            val propertyName = "missing_property"

            // when
            val result = dotenv.get(propertyName, null)

            // then
            result.shouldBeNull()
        }

        it("should return resolved default value for a key using default config") {
            // given
            val dotenv = Dotenv.configure().load()
            val propertyVal = "test_val"

            // when
            val result = dotenv.get("missing_property", "\${test_key}")

            // then
            result
                .shouldNotBeNull()
                .shouldBe(propertyVal)
        }

        it("should throw UnresolvedReferenceException resolving property with missing dependency") {
            // given
            val dotenv = Dotenv.configure().strictMode(true).load()
            val propertyName = "g"

            // when
            val result = shouldThrowAny { dotenv.get(propertyName, "\${h}") }

            // then
            result
                .shouldNotBeNull()
                .shouldBeInstanceOf<UnresolvedReferenceException>()
                .shouldHaveMessage("Cannot resolve reference with name 'h', path: g -> h")
        }
    }

    describe("Dotenv.getRequired(String)") {
        it("should throw MissingPropertyException resolving missing property") {
            // given
            val dotenv = Dotenv.configure().load()
            val propertyName = "missing_property"

            // when
            val result = shouldThrowAny { dotenv.getRequired(propertyName) }

            // then
            result
                .shouldNotBeNull()
                .shouldBeInstanceOf<MissingPropertyException>()
                .shouldHaveMessage("Cannot find property: 'missing_property'")
        }
    }
})

package io.github.arhor.dotenv

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class DotenvConfigurerTest : DescribeSpec({

    describe("DotenvConfigurer.getInstance()") {
        it("should return default configurer which is not null") {
            // when
            val configurer = DotenvConfigurer.getInstance()

            // then
            configurer.shouldNotBeNull()
        }

        it("should return always the same default configurer") {
            // when
            val configurer1 = DotenvConfigurer.getInstance()
            val configurer2 = DotenvConfigurer.getInstance()

            // then
            configurer1.shouldBe(configurer2)
        }
    }

    describe("DotenvConfigurer.load()") {
        it("should load dotenv file with default location and filename") {
            // when
            val result = DotenvConfigurer.getInstance().load()

            // then
            result.shouldNotBeNull()
        }

        it("should load dotenv file with changed relative location and default filename") {
            // given
            val location = "./directory"

            // when
            val result = Dotenv.configure().location(location).load()

            // then
            result.shouldNotBeNull()
        }

        it("should load dotenv file with changed absolute location and default filename") {
            // given
            val location = System.getenv("TEST_RESOURCES_DIR")

            // when
            val result = Dotenv.configure().location(location).load()

            // then
            result.shouldNotBeNull()
        }

        it("should load dotenv file with changed URI location and default filename") {
            // given
            val locationURI = "file:///${System.getenv("TEST_RESOURCES_DIR")}"

            // when
            val result = Dotenv.configure().location(locationURI).load()

            // then
            result.shouldNotBeNull()
        }

        it("should throw LoadingException when loading file by wrong path") {
            // given
            val wrongPath = "surely/non/existing/path"

            // when
            val result = shouldThrowAny { Dotenv.configure().location(wrongPath).load() }

            // then
            result
                .shouldNotBeNull()
                .shouldBeInstanceOf<LoadingException>()
        }
    }

    describe("DotenvConfigurer.equals(Object)") {
        it("should return true for the other configurer with the same state") {
            // given
            val configurer1 =
                DotenvConfigurer.getInstance()
                    .location(".")
                    .filename(".env")
                    .strictMode(true)
                    .includeSystemVariables(true)
                    .replaceSystemVariables(true)
            val configurer2 =
                DotenvConfigurer.getInstance()
                    .location(".")
                    .filename(".env")
                    .strictMode(true)
                    .includeSystemVariables(true)
                    .replaceSystemVariables(true)

            // when
            val result = configurer1 == configurer2

            // then
            result.shouldBeTrue()
        }

        it("should return false for the other configurer with different state") {
            // given
            val configurer1 =
                DotenvConfigurer.getInstance()
                    .location(".")
                    .filename(".env")
                    .strictMode(false)
                    .includeSystemVariables(false)
                    .replaceSystemVariables(false)
            val configurer2 =
                DotenvConfigurer.getInstance()
                    .location("/")
                    .filename(".env.dev")
                    .strictMode(true)
                    .includeSystemVariables(true)
                    .replaceSystemVariables(true)

            // then
            val result = configurer1 == configurer2

            // then
            result.shouldBeFalse()
        }
    }

    describe("DotenvConfigurer.hashCode()") {
        it("should return the same hash code for the equal configurers") {
            // given
            val configurer1 =
                DotenvConfigurer.getInstance()
                    .location(".")
                    .filename(".env")
                    .strictMode(true)
                    .includeSystemVariables(true)
                    .replaceSystemVariables(true)
            val configurer2 =
                DotenvConfigurer.getInstance()
                    .location(".")
                    .filename(".env")
                    .strictMode(true)
                    .includeSystemVariables(true)
                    .replaceSystemVariables(true)

            // when
            val hashCode1 = configurer1.hashCode()
            val hashCode2 = configurer2.hashCode()

            // then
            hashCode1 shouldBeExactly hashCode2
        }
    }

    describe("DotenvConfigurer.toString()") {
        it("should return string representation of the configurer containing information about all its fields") {
            // given
            val configurer = DotenvConfigurer.getInstance()

            // when
            val result = configurer.toString()

            // then
            result
                .shouldContain("location='${configurer.location}'")
                .shouldContain("filename='${configurer.filename}'")
                .shouldContain("strictMode=${configurer.isStrictMode}")
                .shouldContain("includeSystemVariables=${configurer.isIncludeSystemVariables}")
                .shouldContain("replaceSystemVariables=${configurer.isReplaceSystemVariables}")
        }
    }
})

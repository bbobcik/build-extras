package cz.auderis.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Pokus5Test {

    @Nested
    @DisplayName("Deterministic tests")
    class Deterministic {

        @Test
        void shouldSucceed() {
            sleep(100L);
            Assertions.assertTrue(true);
        }

        @Test
        @Disabled("Disabled test in nested class")
        void shouldSucceed2() {
            sleep(100L);
            Assertions.assertTrue(true);
        }

        @Test
        void shouldFail() {
            sleep(500L);
            Assertions.assertTrue(false);
        }

    }

    @Nested
    @DisplayName("Nondeterministic tests")
    class Nondeterministic {

        final Random rng = new Random();

        @Test
        void shouldFailNondeterministic() {
            final long delay = 100L + rng.nextInt(256);
            sleep(delay);
            final int decision = rng.nextInt(5);
            Assertions.assertTrue(decision > 1);
        }

    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

}

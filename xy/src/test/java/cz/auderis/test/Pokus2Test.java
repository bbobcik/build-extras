package cz.auderis.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Pokus2Test {

    @Test
    void shouldSucceed() {
        sleep(500L);
        Assertions.assertTrue(true);
    }

    @Test
    @Disabled("Disabled test")
    void shouldSucceed2() {
        sleep(500L);
        Assertions.assertTrue(true);
    }

    @Test
    void shouldFail() {
        sleep(500L);
        Assertions.assertTrue(false);
    }


    @Test
    void shouldFailNondeterministic() {
        sleep(500L);
        final int decision = new Random().nextInt(5);
        Assertions.assertTrue(decision != 0);
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

}

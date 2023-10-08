package cz.auderis.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

public class Pokus3Test {

    @ParameterizedTest
    @ValueSource(longs = { 700L, 1100L })
    void shouldSucceed(long delay) {
        sleep(delay);
        Assertions.assertTrue(true);
    }

    @ParameterizedTest
    @ValueSource(longs = { 800L, 1200L })
    void shouldFail(long delay) {
        sleep(delay);
        Assertions.assertTrue(false);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "1300 |  4 | 11",
            "2600 | 13 | 17"
    })
    void shouldFailNondeterministic(long delay, int k, int n) {
        sleep(delay);
        final int decision = new Random().nextInt(n);
        Assertions.assertTrue(decision < k, () -> "k=" + k + ", n=" + n + ", decision=" + decision);
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

}

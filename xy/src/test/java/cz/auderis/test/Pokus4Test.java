package cz.auderis.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

public class Pokus4Test {

    @ParameterizedTest
    @ValueSource(longs = { 50L, 100L, 200L })
    void shouldSucceed(long delay) {
        sleep(delay);
        Assertions.assertTrue(true);
    }

    @ParameterizedTest
    @ValueSource(longs = { 50L, 100L, 200L })
    void shouldFail(long delay) {
        sleep(delay);
        Assertions.assertTrue(false);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "150 | 1 | 4",
            "300 | 3 | 6",
            "600 | 7 | 8"
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

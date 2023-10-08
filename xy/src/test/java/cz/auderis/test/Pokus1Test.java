package cz.auderis.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Pokus1Test {

    @Test
    void shouldSucceed() {
        Assertions.assertTrue(true);
    }

    @Test
    void shouldFail() {
        Assertions.assertTrue(false);
    }


    @Test
    void shouldFailNondeterministic() {
        final int decision = new Random().nextInt(5);
        Assertions.assertTrue(decision != 0);
    }

}

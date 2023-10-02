package cz.auderis.test.schedule;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Optional;

public class AdaptiveMethodOrderer implements MethodOrderer {

    @Override
    public Optional<ExecutionMode> getDefaultExecutionMode() {
        return Optional.empty();
    }

    @Override
    public void orderMethods(MethodOrdererContext context) {
    }

}

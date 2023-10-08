package cz.auderis.test.schedule;

import org.junit.jupiter.api.MethodDescriptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;
import java.util.Optional;

public class AdaptiveMethodOrderer implements MethodOrderer {

    @Override
    public Optional<ExecutionMode> getDefaultExecutionMode() {
        return Optional.empty();
    }

    @Override
    public void orderMethods(MethodOrdererContext context) {
        final Class<?> testClass = context.getTestClass();
        final List<? extends MethodDescriptor> testMethods = context.getMethodDescriptors();

        System.err.println("Ordering methods of " + testClass.getCanonicalName() + " (" + testMethods.size() + " methods):");
        for (final MethodDescriptor testMethod : testMethods) {
            System.err.println(" - " + testMethod.getMethod().getName());
        }
    }

}

package cz.auderis.test.schedule;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Arrays;
import java.util.List;

public class AdaptiveClassOrderer implements ClassOrderer {

    public AdaptiveClassOrderer() {
        System.err.println("Initializing class orderer");
    }

    @Override
    public void orderClasses(ClassOrdererContext context) {
        String param = context.getConfigurationParameter("testSchedule.store").orElse("NOT-FOUND");
        param = Arrays.toString(param.toCharArray());

        final List<? extends ClassDescriptor> testClasses = context.getClassDescriptors();
        System.err.printf("Ordering test classes (%d classes, param=%s):%n", testClasses.size(), param);
        for (final ClassDescriptor testClass : testClasses) {
            System.err.println(" - " + testClass.getTestClass().getCanonicalName());
        }
    }

}

package cz.auderis.test.schedule;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class TestSchedulerExtension implements Extension, BeforeAllCallback, BeforeEachCallback, TestWatcher, AfterAllCallback {

    private static final ExtensionContext.Namespace LOCAL_NAMESPACE = ExtensionContext.Namespace.create("cz.auderis.test.schedule");

    public TestSchedulerExtension() {
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        final ExtensionContext.Store contextStore = context.getStore(LOCAL_NAMESPACE);
        final String rootScope = context.getRoot().getUniqueId();
        final String scope = context.getUniqueId();
        contextStore.put("scope", scope);

        final String name = context.getDisplayName();
        System.err.println("### Running test callback BeforeAll for " + name + ", scope " + scope + ", root scope " + rootScope);

    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final ExtensionContext.Store contextStore = context.getStore(LOCAL_NAMESPACE);
        final String scope = contextStore.getOrDefault("scope", String.class, "undefined");
        final String name = context.getDisplayName();
        System.err.println("+++ Running test callback BeforeEach for " + name + ", scope " + scope);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        final ExtensionContext.Store contextStore = context.getStore(LOCAL_NAMESPACE);
        final String scope = contextStore.getOrDefault("scope", String.class, "undefined");
        final String name = context.getDisplayName();
        System.err.printf(">>> Test result: %s for %s (scope %s)%n", "disabled", name, scope);

    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        final ExtensionContext.Store contextStore = context.getStore(LOCAL_NAMESPACE);
        final String scope = contextStore.getOrDefault("scope", String.class, "undefined");
        final String name = context.getDisplayName();
        System.err.printf(">>> Test result: %s for %s (scope %s)%n", "success", name, scope);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        final ExtensionContext.Store contextStore = context.getStore(LOCAL_NAMESPACE);
        final String scope = contextStore.getOrDefault("scope", String.class, "undefined");
        final String name = context.getDisplayName();
        System.err.printf(">>> Test result: %s for %s (scope %s)%n", "abort", name, scope);
        System.err.println("Cause: " + cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        final ExtensionContext.Store contextStore = context.getStore(LOCAL_NAMESPACE);
        final String scope = contextStore.getOrDefault("scope", String.class, "undefined");

        final String name = context.getDisplayName();
        System.err.printf(">>> Test result: %s for %s (scope %s)%n", "fail", name, scope);
        System.err.println("Cause: " + cause.getMessage());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        final ExtensionContext.Store contextStore = context.getStore(LOCAL_NAMESPACE);
        final String scope = contextStore.getOrDefault("scope", String.class, "undefined");

        final String name = context.getDisplayName();
        System.err.println("### Running test callback AfterAll for " + name + ", scope " + scope);


    }

}

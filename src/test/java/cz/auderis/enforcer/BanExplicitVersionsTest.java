package cz.auderis.enforcer;

import org.apache.maven.enforcer.rule.api.EnforcerLevel;
import org.apache.maven.enforcer.rule.api.EnforcerLogger;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static cz.auderis.enforcer.ModelUtils.createDependencies;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class BanExplicitVersionsTest {

    @Mock
    Model projectModel;

    @Mock
    MavenProject project;

    @Mock
    EnforcerLogger logger;

    BanExplicitDependencyVersions rule;

    @BeforeEach
    void initializeRule() {
        doReturn(projectModel)
                .when(project)
                .getOriginalModel();
        rule = new BanExplicitDependencyVersions(project);
        rule.setLog(logger);
    }

    @Nested
    class ThrowingError {

        @Test
        void shouldPassOnNullDependencies() {
            // Given
            doReturn(null)
                    .when(projectModel)
                    .getDependencies();
            // When/then
            assertDoesNotThrow(rule::execute);
        }

        @Test
        void shouldPassOnEmptyDependencies() {
            // Given
            doReturn(Collections.emptyList())
                    .when(projectModel)
                    .getDependencies();
            // When/then
            assertDoesNotThrow(rule::execute);
        }

        @Test
        void shouldPassWhenNoVersionSet() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2",
                    "org.example:artifact3"
            );
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            // When/then
            assertDoesNotThrow(rule::execute);
        }

        @Test
        void shouldFailWhenSomeVersionSet() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2:1.0.0",
                    "org.example:artifact3"
            );
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            // When
            final EnforcerRuleException thrownException = assertThrows(
                    EnforcerRuleException.class,
                    rule::execute
            );
            // Then
            assertThat(thrownException).hasMessageContainingAll(
                    "1.0.0",
                    "artifact2",
                    "main dependencies"
            );
        }

        @Test
        void shouldPassWhenVersionSetInIgnoredScope() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2:1.0.0",
                    "org.example:artifact3"
            );
            dependencies.get(1).setScope("test");
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            rule.setIgnoredScopes(Collections.singletonList("test"));
            // When/then
            assertDoesNotThrow(rule::execute);
        }

        @Test
        void shouldFailWhenVersionSetInUnignoredScope() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2:1.0.0",
                    "org.example:artifact3"
            );
            dependencies.get(1).setScope("provided");
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            rule.setIgnoredScopes(Collections.singletonList("test"));
            // When
            final EnforcerRuleException thrownException = assertThrows(
                    EnforcerRuleException.class,
                    rule::execute
            );
            // Then
            assertThat(thrownException).hasMessageContainingAll(
                    "1.0.0",
                    "artifact2",
                    "main dependencies"
            );
        }

    }


    @Nested
    class ReportingWarning {

        ArgumentCaptor<String> logMessageCaptor = ArgumentCaptor.forClass(String.class);

        @BeforeEach
        void setEnforcementLevel() {
            rule.setLevel(EnforcerLevel.WARN);
        }

        @AfterEach
        void ensureLoggerVerified() {
            verifyNoMoreInteractions(logger);
        }

        @Test
        void shouldPassOnNullDependencies() {
            // Given
            doReturn(null)
                    .when(projectModel)
                    .getDependencies();
            // When
            assertDoesNotThrow(rule::execute);
            // Then
            verify(logger, never()).warn(anyString());
        }

        @Test
        void shouldPassOnEmptyDependencies() {
            // Given
            doReturn(Collections.emptyList())
                    .when(projectModel)
                    .getDependencies();
            // When
            assertDoesNotThrow(rule::execute);
            // Then
            verify(logger, never()).warn(anyString());
        }

        @Test
        void shouldPassWhenNoVersionSet() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2",
                    "org.example:artifact3"
            );
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            // When
            assertDoesNotThrow(rule::execute);
            // Then
            verify(logger, never()).warn(anyString());
        }

        @Test
        void shouldFailWhenSomeVersionSet() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2:1.0.0",
                    "org.example:artifact3"
            );
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            // When
            assertDoesNotThrow(rule::execute);
            // Then
            verify(logger).warn(logMessageCaptor.capture());
            final String logMessage = logMessageCaptor.getValue();
            assertThat(logMessage).containsSubsequence(
                    "1.0.0",
                    "artifact2",
                    "main dependencies"
            );
        }

        @Test
        void shouldPassWhenVersionSetInIgnoredScope() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2:1.0.0",
                    "org.example:artifact3"
            );
            dependencies.get(1).setScope("test");
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            rule.setIgnoredScopes(Collections.singletonList("test"));
            // When
            assertDoesNotThrow(rule::execute);
            // Then
            verify(logger, never()).warn(anyString());
        }

        @Test
        void shouldFailWhenVersionSetInUnignoredScope() {
            // Given
            List<Dependency> dependencies = createDependencies(
                    "org.example:artifact1",
                    "org.example:artifact2:1.0.0",
                    "org.example:artifact3"
            );
            dependencies.get(1).setScope("provided");
            doReturn(dependencies)
                    .when(projectModel)
                    .getDependencies();
            rule.setIgnoredScopes(Collections.singletonList("test"));
            // When
            // When
            assertDoesNotThrow(rule::execute);
            // Then
            verify(logger).warn(logMessageCaptor.capture());
            final String logMessage = logMessageCaptor.getValue();
            assertThat(logMessage).containsSubsequence(
                    "1.0.0",
                    "artifact2",
                    "main dependencies"
            );
        }

    }

}

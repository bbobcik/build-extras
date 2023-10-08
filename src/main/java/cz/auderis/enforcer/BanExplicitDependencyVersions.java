package cz.auderis.enforcer;

import org.apache.maven.enforcer.rule.api.AbstractEnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerLevel;
import org.apache.maven.enforcer.rule.api.EnforcerLogger;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.InputLocation;
import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <b>banExplicitDependencyVersions</b><br>
 * This rule checks whether there are any explicit dependency versions
 * in the project POM, specifically in {@code <dependencies>} block.
 * The goal is to have all versions defined in
 * {@code <dependencyManagement>} section (preferably via imported BOMs)
 * and only use dependency coordinates in the main dependency section.
 *
 * @author <a href="mailto:bbobcik+github@gmail.com">Boleslav Bobčík</a>
 */
@Named("banExplicitDependencyVersions")
public class BanExplicitDependencyVersions extends AbstractEnforcerRule {

    /**
     * Specifies dependency scopes that are ignored by this rule.
     */
    private final Set<String> ignoredScopes;

    /**
     * Specifies whether dependencies in profiles should be checked as well
     * (true by default).
     */
    private boolean checkProfiles;

    private final MavenProject project;
    private EnforcerLevel level;

    @Inject
    public BanExplicitDependencyVersions(MavenProject project) {
        this.project = project;
        //
        ignoredScopes = new HashSet<>(8);
        checkProfiles = true;
        level = EnforcerLevel.ERROR;
    }

    public List<String> getIgnoredScopes() {
        return new ArrayList<>(ignoredScopes);
    }

    public void setIgnoredScopes(List<String> newScopes) {
        ignoredScopes.clear();
        if (null != newScopes) {
            for (String scope : newScopes) {
                scope = (null != scope) ? scope.trim() : "";
                if (!scope.isEmpty()) {
                    ignoredScopes.add(scope);
                }
            }
        }
    }

    public boolean isCheckProfiles() {
        return checkProfiles;
    }

    public void setCheckProfiles(boolean checkProfiles) {
        this.checkProfiles = checkProfiles;
    }

    @Override
    public EnforcerLevel getLevel() {
        return level;
    }

    public void setLevel(EnforcerLevel level) {
        this.level = level;
    }

    @Override
    public void execute() throws EnforcerRuleException {
        final Model model = project.getOriginalModel();
        // Check normal dependencies
        final List<Dependency> projectDependencies = model.getDependencies();
        checkDependencies("main dependencies", projectDependencies);
        // Check dependencies contained in profiles if requested
        final List<Profile> profiles = model.getProfiles();
        if (checkProfiles && null != profiles) {
            for (final Profile profile : profiles) {
                final String profileId = profile.getId();
                final List<Dependency> profileDependencies = profile.getDependencies();
                checkDependencies("profile " + profileId, profileDependencies);
            }
        }
    }

    private void checkDependencies(String checkContext, Iterable<Dependency> dependencies)
            throws EnforcerRuleException {
        if (null == dependencies) {
            return;
        }
        final boolean hasIgnoredScopes = !ignoredScopes.isEmpty();
        for (final Dependency dependency : dependencies) {
            final String version = dependency.getVersion();
            if ((null == version) || version.trim().isEmpty()) {
                continue;
            }
            if (hasIgnoredScopes) {
                final String scope = dependency.getScope();
                if (null != scope && !scope.trim().isEmpty() && ignoredScopes.contains(scope)) {
                    continue;
                }
            }
            reportViolation(checkContext, dependency);
        }
    }

    private void reportViolation(String checkContext, Dependency dependency)
            throws EnforcerRuleException {
        final String version = dependency.getVersion();
        final String groupId = dependency.getGroupId();
        final String artifactId = dependency.getArtifactId();
        final InputLocation location = dependency.getLocation("");
        final String locationString;
        if ((null != location) && location.getLineNumber() > 0) {
            final int lineNumber = location.getLineNumber();
            locationString = String.format("%s, line %d", checkContext, lineNumber);
        } else {
            locationString = checkContext;
        }
        final String message = String.format(
                "Explicit dependency version '%s' in %s:%s (%s)",
                version, groupId, artifactId, locationString
        );
        final EnforcerLevel level = getLevel();
        if (level == EnforcerLevel.ERROR) {
            throw new EnforcerRuleException(message);
        }
        final EnforcerLogger log = getLog();
        log.warn(message);
    }

}

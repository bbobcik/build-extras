package cz.auderis.enforcer;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Profile;

import java.util.ArrayList;
import java.util.List;

class ModelUtils {

    static Dependency createDependency(String coordinates) {
        final Dependency dep = new Dependency();
        final int firstColon = coordinates.indexOf(':');
        if (firstColon < 0) {
            throw new IllegalArgumentException("Invalid dependency coordinates: " + coordinates);
        }
        final String groupId = coordinates.substring(0, firstColon);
        dep.setGroupId(groupId);
        final int secondColon = coordinates.indexOf(':', firstColon + 1);
        if (secondColon < 0) {
            // No version specified
            final String artifactId = coordinates.substring(firstColon + 1);
            dep.setArtifactId(artifactId);
        } else {
            // Version specified
            final String artifactId = coordinates.substring(firstColon + 1, secondColon);
            dep.setArtifactId(artifactId);
            final String version = coordinates.substring(secondColon + 1);
            dep.setVersion(version);
        }
        return dep;
    }

    static List<Dependency> createDependencies(String... coordinates) {
        final List<Dependency> deps = new ArrayList<>(coordinates.length);
        for (String coord : coordinates) {
            final Dependency dep = createDependency(coord);
            deps.add(dep);
        }
        return deps;
    }

    static List<Profile> createProfiles(String... profileIds) {
        final List<Profile> profiles = new ArrayList<>(profileIds.length);
        for (String profileId : profileIds) {
            final Profile profile = new Profile();
            profile.setId(profileId);
            profiles.add(profile);
        }
        return profiles;
    }

}

package cz.auderis.test.schedule.stats;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Created on 6.10.2023.
 */
public class TestStatsManager implements TestStatisticsController {

    private final Path statsDir;

    public TestStatsManager(Path statsDir) {
        this.statsDir = Objects.requireNonNull(statsDir, "Test statistics directory is undefined");
    }




}

package io.quarkiverse.quinoa.test;

import static io.quarkiverse.quinoa.deployment.testing.QuinoaQuarkusUnitTest.getWebUITestDirPath;
import static io.quarkiverse.quinoa.deployment.testing.QuinoaQuarkusUnitTest.systemBinary;
import static io.quarkiverse.quinoa.deployment.testing.QuinoaQuarkusUnitTest.LockFile.YARN;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.quinoa.deployment.testing.QuinoaQuarkusUnitTest;
import io.quarkus.test.QuarkusUnitTest;

public class QuinoaFrozenLockfileConfigTest {

    private static final String NAME = "frozen-lockfile";

    @RegisterExtension
    static final QuarkusUnitTest config = QuinoaQuarkusUnitTest.create(NAME)
            .initialLockfile(YARN)
            .ci(null)
            .toQuarkusUnitTest()
            .overrideConfigKey("quarkus.quinoa.frozen-lockfile", "true")
            .assertLogRecords(l -> assertThat(l)
                    .anyMatch(s -> s.getMessage().equals("Running Quinoa package manager install command: %s") &&
                            s.getParameters()[0].equals(systemBinary("yarn") + " install --frozen-lockfile")));

    @Test
    public void testQuinoa() {
        assertThat(getWebUITestDirPath(NAME).resolve("node_modules/installed")).isRegularFile()
                .hasContent("hello");
    }
}

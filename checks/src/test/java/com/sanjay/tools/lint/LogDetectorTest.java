package com.sanjay.tools.lint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class LogDetectorTest extends LintDetectorTest {
    public void testLog() {
        lint().files(
                java("" +
                        "package test.pkg;\n" +
                        "\n" +
                        "public class TestCase1 {\n" +
                        "\n" +
                        "    public void test() {\n" +
                        "        android.util.Log.e(\"TAG\", \"message\");\n" +
                        "    }\n" +
                        "\n" +
                        "}"
                )
        ).run()
                .expect("src/test/pkg/TestCase1.java:6: Error: Use custom log instead of android.util.Log#e()  [LogUse]\n" +
                        "        android.util.Log.e(\"TAG\", \"message\");\n" +
                        "        ~~~~~~~~~~~~~~~~~~\n" +
                        "1 errors, 0 warnings");
    }

    public void testSystemOut() {
        lint().files(
                java("" +
                        "package test.pkg;\n" +
                        "\n" +
                        "public class TestCase2 {\n" +
                        "\n" +
                        "    public void test() {\n" +
                        "        System.out.print(\"message\");\n" +
                        "    }\n" +
                        "\n" +
                        "}")
        ).run()
                .expect("src/test/pkg/TestCase2.java:6: Error: Use custom log instead of System.out#print()  [LogUse]\n" +
                        "        System.out.print(\"message\");\n" +
                        "        ~~~~~~~~~~~~~~~~\n" +
                        "1 errors, 0 warnings");
    }

    @Override
    protected Detector getDetector() {
        return new LogDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(LogDetector.ISSUE);
    }
}

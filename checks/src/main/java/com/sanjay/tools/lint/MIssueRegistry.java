package com.sanjay.tools.lint;


import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MIssueRegistry extends IssueRegistry {
    @NotNull
    @Override
    public List<Issue> getIssues() {
        return Collections.singletonList(LogDetector.ISSUE);
    }
}

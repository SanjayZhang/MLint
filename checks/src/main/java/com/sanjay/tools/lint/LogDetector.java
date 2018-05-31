package com.sanjay.tools.lint;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LogDetector extends Detector implements SourceCodeScanner {
    public static final Issue ISSUE = Issue.create(
            "LogUse",
            "Log/System.out.print is not allowed",
            "Use Custom Log",
            Category.SECURITY, 5, Severity.ERROR,
            new Implementation(LogDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Nullable
    public List<String> getApplicableMethodNames() {
        List<String> list = Arrays.asList("d", "e", "i", "v", "w", "println", "isLoggable");
        List<String> sysList = Arrays.asList("print", "printf", "println");

        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(list);
        hashSet.addAll(sysList);
        return new ArrayList<>(hashSet);
    }

    @Override
    public void visitMethod(@NonNull JavaContext context, @NonNull UCallExpression call,
                            @NonNull PsiMethod method) {
        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isMemberInClass(method, "android.util.Log")) {
            String message = String.format(
                    "Use `custom log` instead of `android.util.Log#%1$s()` ",
                    method.getName());
            reportCustomLogIssue(context, call, message);
        } else if (evaluator.isMemberInClass(method, "java.io.PrintStream")) {
            String message = String.format(
                    "Use `custom log` instead of `System.out#%1$s()` ",
                    method.getName());
            reportCustomLogIssue(context, call, message);
        }
    }

    private void reportCustomLogIssue(@NonNull JavaContext context, @NonNull UCallExpression call, String message) {
        Location location = context.getCallLocation(call, true, false);
        context.report(ISSUE, call, location, message);
    }

}

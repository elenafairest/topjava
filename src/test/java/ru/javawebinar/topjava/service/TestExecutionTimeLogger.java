package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestExecutionTimeLogger implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(TestExecutionTimeLogger.class);

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long start = System.nanoTime();
                try {
                    statement.evaluate();
                } finally {
                    double runTime = (System.nanoTime() - start) * 0.000001;
                    MealServiceTest.executionTimeLog.append(String.format("Time taken for %s: %s ms%n", description.getDisplayName(), runTime));
                    log.info("Time taken for {}: {} ms",
                            description.getDisplayName(),
                            runTime);
                }
            }
        };
    }
}

package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;

public class TestExecutionTimeLogger implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long start = System.currentTimeMillis();
                try {
                    statement.evaluate();
                } finally {
                    log.info("Time taken for {}: {} milli sec",
                            description.getDisplayName(),
                            System.currentTimeMillis() - start);
                }
            }
        };
    }
}

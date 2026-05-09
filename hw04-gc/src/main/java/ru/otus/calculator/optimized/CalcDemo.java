package ru.otus.calculator.optimized;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class CalcDemo {
    private static final Logger log = LoggerFactory.getLogger(CalcDemo.class);

    public static void main(String[] args) {
        int counter = 500_000_000;
        int logStep = 10_000_000;

        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        var data = new Data(0);
        int logCounter = 0;
        for (var idx = 0; idx < counter; idx++) {
            data.setValue(idx);
            summator.calc(data);

            if (logCounter == logStep) {
                log.info("{} current idx:{}", LocalDateTime.now(), idx);
                logCounter = 0;
            }
            logCounter++;
        }

        long delta = System.currentTimeMillis() - startTime;
        log.info("PrevValue:{}", summator.getPrevValue());
        log.info("PrevPrevValue:{}", summator.getPrevPrevValue());
        log.info("SumLastThreeValues:{}", summator.getSumLastThreeValues());
        log.info("SomeValue:{}", summator.getSomeValue());
        log.info("Sum:{}", summator.getSum());
        log.info("spend msec:{}, sec:{}", delta, (delta / 1000));
    }
}
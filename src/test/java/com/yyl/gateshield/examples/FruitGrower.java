package com.yyl.gateshield.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FruitGrower implements Sales {

    private final Logger logger = LoggerFactory.getLogger(FruitGrower.class);

    @Override
    public void sellFruits() {
//        System.out.println("Successfully sold fruits");
        logger.info("Successfully sold fruits");
    }
}

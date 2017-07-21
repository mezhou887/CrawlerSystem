package com.crawl;


import org.apache.log4j.Logger;


import java.io.IOException;

public class ExceptionTest {
    private static Logger logger = Logger.getLogger(ExceptionTest.class);
    public static void main(String[] args){
        try {
            throw new IOException();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

package com.kurt.gym.helper.logger;

import org.slf4j.Logger;

public final class LoggerUtil {

    public static void printInfoWithDash (Logger logger, String message){
        logger.info("========================= " + message + " =================================");
    }
}

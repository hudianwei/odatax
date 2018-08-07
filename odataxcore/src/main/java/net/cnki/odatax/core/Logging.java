package net.cnki.odatax.core;


import org.apache.log4j.Logger;

/**
 * @author hudianwei
 * @date 2018/8/2 15:27
 */
/*日志*/
public class Logging {
    private final static Logger logger = Logger.getLogger(Logging.class);

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void debug(String message, Throwable e) {
        logger.debug(message, e);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Throwable e) {
        logger.info(message, e);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Throwable e) {
        logger.error(message, e);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void warn(String message, Throwable e) {
        logger.warn(message, e);
    }

    public static void fatal(String message) {
        logger.fatal(message);
    }

    public static void fatal(String message, Throwable e) {
        logger.fatal(message, e);
    }
}

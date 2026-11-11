package com.u.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    public static void logError(Logger log, String methodName, Exception e) {
        log.error("操作异常 [方法名:{}]：{}", methodName, e.getMessage(), e);
    }

    public static void logOperation(Logger log, String operation) {
        log.info("操作审计：{}", operation);
    }
}
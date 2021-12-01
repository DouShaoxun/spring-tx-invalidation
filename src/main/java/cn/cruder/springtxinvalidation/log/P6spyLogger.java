package cn.cruder.springtxinvalidation.log;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: cruder
 * @Date: 2021/11/30/22:50
 */
@Slf4j
public class P6spyLogger extends com.p6spy.engine.spy.appender.StdoutLogger {

    @Override
    public void logText(String text) {
        //  SQL 日志
        log.info(text.replace("\n"," "));
    }
}

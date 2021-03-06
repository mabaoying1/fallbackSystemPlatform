package com.soft.api.util;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;

/**
 * @ClassName MyTimeBasedFileNamingAndTriggeringPolicy
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/18
 * @Version V1.0
 **/
@NoAutoStart
public class MyTimeBasedFileNamingAndTriggeringPolicys<E>
        extends DefaultTimeBasedFileNamingAndTriggeringPolicy<E> {
    //这个用来指定时间间隔
    private Integer multiple = 1;

    @Override
    protected void computeNextCheck() {
        nextCheck = rc.getEndOfNextNthPeriod(dateInCurrentPeriod, multiple).getTime();
    }

    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        if (multiple > 1) {
            this.multiple = multiple;
        }
    }
}

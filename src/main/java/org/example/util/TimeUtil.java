package org.example.util;

import java.time.LocalDateTime;

public class TimeUtil {
    /**
     * 获取即将到来的时间点
     *
     * @param hour   小时
     * @param minute 分钟
     * @param second 秒
     * @return 时间点
     */
    public static LocalDateTime getNextAssignTime(int hour, int minute, int second) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayAssignOClock = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hour, minute, second);
        return now.isAfter(todayAssignOClock) ? todayAssignOClock.plusDays(1) : todayAssignOClock;
    }
}

package com.lchclearnet.calendar;

import java.time.LocalDate;

public enum BusinessDayConvention implements DayMove {
    ModifiedFollowing {
        public LocalDate resolveToDay(LocalDate dt, BusinessCalendar cal) {
            LocalDate dt_temp = DateUtils.offsetBusinessDay(dt, cal, 0, "MF");
            return DateUtils.offsetBusinessDay(dt_temp, cal, 0, "MF");
        }
    }, Following {
        public LocalDate resolveToDay(LocalDate dt, BusinessCalendar cal) {
            LocalDate dt_temp = DateUtils.offsetBusinessDay(dt, cal, 1, "F");
            return DateUtils.offsetBusinessDay(dt_temp, cal, -1, "F");
        }
    }, Proceding {
        public LocalDate resolveToDay(LocalDate dt, BusinessCalendar cal) {
            LocalDate dt_temp = DateUtils.offsetBusinessDay(dt, cal, 1, "P");
            return DateUtils.offsetBusinessDay(dt, cal, -1, "P");
        }
    }, ModifiedProceding {
        public LocalDate resolveToDay(LocalDate dt, BusinessCalendar cal) {
            LocalDate dt_temp = DateUtils.offsetBusinessDay(dt, cal, 1, "MP");
            return DateUtils.offsetBusinessDay(dt, cal, -1, "MP");
        }
    },
}


package com.lchclearnet.fx.jandas;

import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.BusinessCalendarServiceFactory;
import com.lchclearnet.calendar.DateUtils;
import com.lchclearnet.calendar.jandas.DataFrameBusinessCalendarAdapter;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.utils.Tuple;
import java.time.LocalDate;

public class CalendarServiceDemo {

  public static void main(String[] args) {

    Tuple params = DemoHelper.parseArgs(args);

    AppConfig configService = params.item(0);
    BusinessCalendarServiceFactory calendarService = buildBusinessCalendarService(configService);
    SmartRequest all_business_days_request = new SmartRequest();

    BusinessCalendarService response =
        DemoHelper.execute_request(calendarService, all_business_days_request,
            "business days in cache");

    BusinessCalendar bc = response.getBusinessCalendar("ATX");
    DateUtils.stream(LocalDate.of(2018, 12, 24), LocalDate.of(2019, 1, 14))
        .forEach(ld -> System.out.println(
            String.format("%s %s a business day.", ld, bc.isBusinessDay(ld) ? "is" : "is not")));
  }

  private static BusinessCalendarServiceFactory buildBusinessCalendarService(AppConfig config) {

    BusinessCalendarServiceFactory service =
        new BusinessCalendarServiceFactory(new DataFrameBusinessCalendarAdapter(config));
    service.startup();
    return service;
  }

}

package com.lchclearnet.pricer;

import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.DateUtils;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.trade.CashFlowInstrument;
import com.lchclearnet.trade.CashFlowResult;
import com.lchclearnet.trade.CashFlowResults;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;

import java.time.LocalDate;

public class SimpleCashFlowPricer implements
        CashFlowPricer<Iterable<CashFlowInstrument>, CashFlowResults> {

    public static LocalDate getSpotDate(LocalDate valueDate, Currency ccy,
                                        BusinessCalendarService calendarService, LocalDate[] spotDates) {

        LocalDate spotDate = spotDates[ccy.ordinal()];
        if (spotDate == null) {
            BusinessCalendar calendar = calendarService.getBusinessCalendar(ccy);
            spotDate = DateUtils.offsetBusinessDay(valueDate, calendar, ccy.getSpotLag(), "F");
            spotDates[ccy.ordinal()] = spotDate;
        }
        return spotDate;
    }

    @Override
    public CashFlowResults price(LocalDate valueDate, Iterable<CashFlowInstrument> cashFlows,
                                 FxMarket mktServiceProvider, BusinessCalendarService calendarService) {

        LocalDate[] spotDates = new LocalDate[Currency.values().length];

        CashFlowResults results = new CashFlowResults();

        FxSpots fxSpots = mktServiceProvider.getFxSpots();

        LocalDate spotDate;
        YieldCurve yc_pay_dc, yc_ccy_fwd, yc_pay_fwd;

        for (CashFlowInstrument cashFlow : cashFlows) {
            spotDate = getSpotDate(valueDate, cashFlow.currency, calendarService, spotDates);
            yc_pay_dc = mktServiceProvider.getYieldCurve(cashFlow.paymentCurrency.getDiscountCurve());
            yc_ccy_fwd = mktServiceProvider.getYieldCurve(cashFlow.currency.getFxForwardYieldCurve());
            yc_pay_fwd = mktServiceProvider.getYieldCurve(cashFlow.paymentCurrency.getFxForwardYieldCurve());

            double df_ccy_fwd = yc_ccy_fwd.discountFactor(spotDate, cashFlow.settlementDate);
            double df_pay_fwd = yc_pay_fwd.discountFactor(spotDate, cashFlow.settlementDate);
            double df_pay_dc = yc_pay_dc.discountFactor(valueDate, cashFlow.settlementDate);
            double dcf = yc_ccy_fwd.getDayCountFraction(cashFlow.spotDate, cashFlow.settlementDate);
            double dcf_s = yc_ccy_fwd.getDayCountFraction(cashFlow.spotDate);
            double dcf_e = yc_ccy_fwd.getDayCountFraction(cashFlow.settlementDate);

            results.add(new CashFlowResult(cashFlow, df_ccy_fwd, df_pay_dc, df_pay_fwd,
                    fxSpots.getSpot(CurrencyPair.of(cashFlow.currency, cashFlow.paymentCurrency)), dcf, dcf_s,
                    dcf_e));
        }

        return results;
    }
}

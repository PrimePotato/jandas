package com.lchclearnet.pricer;

import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.DateUtils;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.Row;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.column.ColumnFactory;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;

import java.time.LocalDate;

import static com.lchclearnet.trade.TradeService.*;

public class TableCashFlowPricer implements CashFlowPricer<Table, Table> {

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
    public Table price(LocalDate valueDate, Table cashFlows,
                       FxMarket mktServiceProvider, BusinessCalendarService calendarService) {


        Column<Double> df_ccy_fwd = ColumnFactory.createColumn("df_ccy_fwd", ColumnFactory.DOUBLE);
        Column<Double> df_pay_fwd = ColumnFactory.createColumn("df_pay_fwd", ColumnFactory.DOUBLE);
        Column<Double> df_pay_dc = ColumnFactory.createColumn("df_pay_dc", ColumnFactory.DOUBLE);
        Column<Double> spot = ColumnFactory.createColumn("spot", ColumnFactory.DOUBLE);
        Column<Double> dcf = ColumnFactory.createColumn("DCF", ColumnFactory.DOUBLE);
        Column<Double> dcf_s = ColumnFactory.createColumn("DCFSpot", ColumnFactory.DOUBLE);
        Column<Double> dcf_e = ColumnFactory.createColumn("DCFEnd", ColumnFactory.DOUBLE);

        LocalDate[] spotDates = new LocalDate[Currency.values().length];

        FxSpots fxSpots = mktServiceProvider.getFxSpots();

        YieldCurve yc_pay_dc, yc_ccy_fwd, yc_pay_fwd;

        Currency currency, paymentCurrency;
        LocalDate settlementDate, spotDate, ccySpotDate;
        for (Row cashFlow : cashFlows) {

            currency = cashFlow.getObject(NATIVE_CCY);
            paymentCurrency = cashFlow.getObject(PAYEMENT_CCY);
            settlementDate = cashFlow.getDate(PAYEMENT_DATE);
            spotDate = cashFlow.getDate(SPOT_DATE);

            yc_pay_dc = mktServiceProvider.getYieldCurve(paymentCurrency.getDiscountCurve());
            yc_ccy_fwd = mktServiceProvider.getYieldCurve(currency.getFxForwardYieldCurve());
            yc_pay_fwd = mktServiceProvider.getYieldCurve(paymentCurrency.getFxForwardYieldCurve());

            //todo why are we using getSpotDate and not the cash flow spot date ?
            ccySpotDate = getSpotDate(valueDate, currency, calendarService, spotDates);
            df_ccy_fwd.append(yc_ccy_fwd.discountFactor(ccySpotDate, settlementDate));
            df_pay_fwd.append(yc_pay_fwd.discountFactor(ccySpotDate, settlementDate));

            df_pay_dc.append(yc_pay_dc.discountFactor(valueDate, settlementDate));
            dcf.append(yc_ccy_fwd.getDayCountFraction(spotDate, settlementDate));
            dcf_s.append(yc_ccy_fwd.getDayCountFraction(spotDate));
            dcf_e.append(yc_ccy_fwd.getDayCountFraction(settlementDate));

            spot.append(fxSpots.getSpot(CurrencyPair.of(currency, paymentCurrency)));
        }

        return cashFlows.addColumns(df_ccy_fwd, df_pay_dc, df_pay_fwd, spot, dcf, dcf_s, dcf_e);
    }
}

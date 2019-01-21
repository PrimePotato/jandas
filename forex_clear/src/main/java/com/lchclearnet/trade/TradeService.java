package com.lchclearnet.trade;

import com.lchclearnet.market.FxMarket;
import com.lchclearnet.table.Table;
import com.lchclearnet.utils.Currency;

public interface TradeService {

  String MEMBER = "Member";
  String FOREX_CLEAR_REF = "ForexClearRef";
  String TRADE_REF = "TradeRef";
  String TRADE_TYPE = "TradeType";

  String NATIVE_CASHFLOW = "NativeCashFlow";
  String NATIVE_CCY = "NativeCurrency";
  String PAYEMENT_DATE = "PaymentDate";
  String PAYEMENT_CCY = "PaymentCurrency";
  String CCY_PAIR = "CurrencyPair";
  String SPOT_DATE = "SpotDate";

  String CALL_CURRENCY = "CallCurrency";
  String PUT_CURRENCY = "PutCurrency";
  String CALL_AMOUNT = "CallAmount";
  String PUT_AMOUNT = "PutAmount";

  String BUY_SELL = "BuySell";
  String PRICE_STRIKE = "Price/Strike";
  String PREMIUM_CURRENCY = "PremiumCurrency";
  String PREMIUM_AMOUNT = "PremiumAmount";
  String PREMIUM_SETTLEMENT_DATE = "PremiumSettlementDate";
  String SETTLEMENT_DATE = "SettlementDate";
  String EXPIRY_DATE = "ExpiryDate";
  String EXPIRY_TIME = "ExpiryTime";

  Iterable<Currency> getCurrencies();

  <T> T getCashFlows(FxMarket marketService);

  <T> T getOptions(FxMarket marketService);

  <T> T getTrades();
}

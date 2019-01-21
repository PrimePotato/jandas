package com.lchclearnet.utils;

public enum VolNode  {
  PUT_10(.1, "PUT 10", "PUT_10"),
  PUT_25(.25, "PUT 25", "PUT_25"),
  ATM(0.5, "ATM"),
  CALL_25(.75 ,"CALL 25", "CALL_25"),
  CALL_10(.9, "CALL 10", "CALL_10");


  private double delta;
  private String[] value;

  VolNode(double delta, String... value) {
    this.delta = delta;
    this.value = value;
  }

  public double offsetDelta() {
    return delta;
  }

  public String[] getValues() {

    return value;
  }

  @Override
  public String toString() {

    return value[0];
  }

}

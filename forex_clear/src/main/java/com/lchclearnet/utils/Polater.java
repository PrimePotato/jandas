package com.lchclearnet.utils;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.function.DoubleFunction;

public class Polater implements UnivariateInterpolator {

    private final UnivariateInterpolator delegate;
    private final ExtraPolationMethod left;
    private final ExtraPolationMethod right;

    private Polater(UnivariateInterpolator delegate, ExtraPolationMethod left, ExtraPolationMethod right) {
        this.delegate = delegate;
        this.left = left;
        this.right = right;
    }

    public static UnivariateInterpolator decorate(UnivariateInterpolator delegate) {
        return decorate(delegate, ExtraPolationMethod.CONSTANT, ExtraPolationMethod.CONSTANT);
    }

    public static UnivariateInterpolator decorate(UnivariateInterpolator delegate, ExtraPolationMethod left, ExtraPolationMethod right) {
        return new Polater(delegate, left, right);
    }

    public UnivariateFunction interpolate(double xval[], double yval[]) {

        final PolynomialSplineFunction uf = (PolynomialSplineFunction) delegate.interpolate(xval, yval);
        final PolynomialFunction[] pfs = uf.getPolynomials();

        final DoubleFunction<Double> leftFunction;
        switch(left){
            case CONSTANT:
                leftFunction = x -> yval[0];
                break;
            case CONTINUE:
                leftFunction = x -> pfs[0].value(x);
                break;
            default:
                throw new IllegalStateException(String.format("'%s' extrapolation method is not handeled", left));
        }

        final DoubleFunction<Double> rightFunction;
        switch(left){
            case CONSTANT:
                rightFunction = x -> yval[xval.length - 1];
                break;
            case CONTINUE:
                rightFunction = x -> pfs[pfs.length - 1].value(x);
                break;
            default:
                throw new IllegalStateException(String.format("'%s' extrapolation method is not handeled", left));
        }

        return x -> {
            double y;
            if (x < xval[0]) {
                y = leftFunction.apply(x);
            } else if (x > xval[xval.length - 1]) {
                y = rightFunction.apply(x);
            } else {
                y = uf.value(x);
            }
            return y;
        };
    }

}



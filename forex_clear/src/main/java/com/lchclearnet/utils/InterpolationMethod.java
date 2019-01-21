package com.lchclearnet.utils;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

public enum InterpolationMethod implements UnivariateInterpolator {

    LINEAR_CONSTANT(Polater.decorate(new LinearInterpolator(), ExtraPolationMethod.CONSTANT, ExtraPolationMethod.CONSTANT)),
    LINEAR_CONTINUE(Polater.decorate(new LinearInterpolator(), ExtraPolationMethod.CONTINUE, ExtraPolationMethod.CONTINUE)),
    LINEAR_CONSTANT_CONTINUE(Polater.decorate(new LinearInterpolator(), ExtraPolationMethod.CONSTANT, ExtraPolationMethod.CONTINUE)),
    LINEAR_CONTINUE_CONSTANT(Polater.decorate(new LinearInterpolator(), ExtraPolationMethod.CONTINUE, ExtraPolationMethod.CONSTANT)),

    SPLINE_CONSTANT(Polater.decorate(new SplineInterpolator(), ExtraPolationMethod.CONSTANT, ExtraPolationMethod.CONSTANT)),
    SPLINE_CONTINUE(Polater.decorate(new SplineInterpolator(), ExtraPolationMethod.CONTINUE, ExtraPolationMethod.CONTINUE)),
    SPLINE_CONSTANT_CONTINUE(Polater.decorate(new SplineInterpolator(), ExtraPolationMethod.CONSTANT, ExtraPolationMethod.CONTINUE)),
    SPLINE_CONTINUE_CONSTANT(Polater.decorate(new SplineInterpolator(), ExtraPolationMethod.CONTINUE, ExtraPolationMethod.CONSTANT));

    private UnivariateInterpolator interpolator;

    InterpolationMethod(UnivariateInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public static InterpolationMethod getEnum(String value) {
        if (value == null) throw new NullPointerException("Cannot parse a null interpolation method.");
        value = value.toLowerCase().trim();

        if ("linear_constant".equalsIgnoreCase(value)) return LINEAR_CONSTANT;
        if ("linear_continue".equalsIgnoreCase(value)) return LINEAR_CONTINUE;
        if ("linear_constant_continue".equalsIgnoreCase(value)) return LINEAR_CONSTANT_CONTINUE;
        if ("linear_continue_constant".equalsIgnoreCase(value)) return LINEAR_CONTINUE_CONSTANT;

        if ("spline_constant".equalsIgnoreCase(value)) return LINEAR_CONSTANT;
        if ("spline_continue".equalsIgnoreCase(value)) return LINEAR_CONTINUE;
        if ("spline_constant_continue".equalsIgnoreCase(value)) return LINEAR_CONSTANT_CONTINUE;
        if ("spline_continue_constant".equalsIgnoreCase(value)) return LINEAR_CONTINUE_CONSTANT;

        throw new IllegalArgumentException(String.format("'%s' is not a valid interpolation method.", value));
    }

    @Override
    public UnivariateFunction interpolate(double[] xval, double[] yval) throws MathIllegalArgumentException {
        return interpolator.interpolate(xval, yval);
    }
}

package com.lchclearnet.market.vol;

import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.calendar.SingleBusinessCalendar;
import com.lchclearnet.calendar.Tenor;
import com.lchclearnet.market.vol.DeltaVolSlice;
import com.lchclearnet.market.vol.FXVolSurface;
import com.lchclearnet.market.vol.StrikeVolSlice;
import com.lchclearnet.utils.ExtraPolationMethod;
import com.lchclearnet.utils.Polater;
import com.lchclearnet.utils.VolNode;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class FXVolSurfaceTest {

    private Tenor t;
    private LocalDate dt;
    private EnumMap<VolNode, Double> vols;
    private DeltaVolSlice dvs;
    private StrikeVolSlice svs;
    private FXVolSurface fvs;

    @Before
    public void setUp() {

        LocalDate valueDate = LocalDate.of(2018, 12, 12);

        vols = new EnumMap<>(VolNode.class);

        vols.put(VolNode.PUT_10, 0.15);
        vols.put(VolNode.PUT_25, 0.14);
        vols.put(VolNode.ATM, 0.15);
        vols.put(VolNode.CALL_25, 0.17);
        vols.put(VolNode.CALL_10, 0.19);

        dvs = new DeltaVolSlice(Tenor.parse("1M"), LocalDate.of(2019, 2, 15), vols,
                new LinearInterpolator());
        svs = new StrikeVolSlice(dvs, 101, 0.6);

        vols.put(VolNode.PUT_10, 0.15);
        vols.put(VolNode.PUT_25, 0.14);
        vols.put(VolNode.ATM, 0.15);
        vols.put(VolNode.CALL_25, 0.17);
        vols.put(VolNode.CALL_10, 0.19);

        List<LocalDate> ary_exp = Arrays.asList(LocalDate.of(2019, 2, 15),
                LocalDate.of(2019, 3, 15),
                LocalDate.of(2021, 4, 11));

        TreeMap<LocalDate, StrikeVolSlice> slices = new TreeMap<>();
        for (LocalDate expiry : ary_exp) {
            dvs = new DeltaVolSlice(t, expiry, vols, Polater.decorate(new LinearInterpolator(), ExtraPolationMethod.CONTINUE, ExtraPolationMethod.CONTINUE));
            double tau = DayCountFraction.DCF_ACT_365F.calc(valueDate, expiry);
            slices.put(expiry, new StrikeVolSlice(dvs, 101, tau));
        }

        HashSet<DayOfWeek> wkd = new HashSet<>();
        wkd.add(DayOfWeek.SATURDAY);
        wkd.add(DayOfWeek.SUNDAY);

        SingleBusinessCalendar sbc = new SingleBusinessCalendar("test", new HashSet(), wkd);

        fvs = new FXVolSurface(valueDate, slices, sbc);
    }

    @Test
    public void volInterpFromStrike() {
        fvs.volInterpFromStrike(100);
    }

    @Test
    public void volFromStrikeExpiry() {
        System.out.println(fvs.volFromStrikeExpiry(LocalDate.of(2019, 5, 10), 99));
        System.out.println(fvs.volFromStrikeExpiry(LocalDate.of(2020, 1, 1), 99));
        System.out.println(fvs.volFromStrikeExpiry(LocalDate.of(2020, 1, 1), 100));
        System.out.println(fvs.volFromStrikeExpiry(LocalDate.of(2020, 1, 1), 102));
    }

}
package com.techyourchance.unittestingfundamentals.exercise3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp(){
        SUT = new IntervalsAdjacencyDetector();
    }

    // interval1 adjast interval2 at start
    @Test
    public void interval_adjastAtStart_true() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(5, 8);
        boolean res = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(res, is(true));
    }

    // interval1 adjast interval2 at end
    @Test
    public void interval_adjastAtEnd_true() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-5, -1);
        boolean res = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(res, is(true));
    }

    // interval1 contains interval2
    @Test
    public void interval_interval1ContainsInterval2_true() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(0, 4);
        boolean res = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(res, is(false));
    }

    // interval1 is contained in interval2
    @Test
    public void interval_interval1isContainedInterval2_true() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-2, 8);
        boolean res = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(res, is(false));
    }

    //interval1 is before interval2
    @Test
    public void interval_interval1iBeforeInterval2_true() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(0, 8);
        boolean res = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(res, is(false));
    }

    // interval1 is after interval2
    @Test
    public void interval_interval1isAfterInterval2_true() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-5, -2);
        boolean res = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(res, is(false));
    }

    // interval1 is the same interval2
    @Test
    public void interval_interval1isTheSameInterval2_true() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-1, 5);
        boolean res = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(res, is(false));
    }

}
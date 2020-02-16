package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setUp(){
        SUT = new StringDuplicator();
    }

    @Test
    public void test1(){
        String rest = SUT.duplicate("");
        Assert.assertThat(rest, is(""));
    }

    @Test
    public void test2(){
        String rest = SUT.duplicate("a");
        Assert.assertThat(rest, is("aa"));
    }

    @Test
    public void test3(){
        String rest = SUT.duplicate("a a");
        Assert.assertThat(rest, is("a aa a"));
    }

}
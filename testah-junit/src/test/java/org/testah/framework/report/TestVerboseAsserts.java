package org.testah.framework.report;

import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;

import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class TestVerboseAsserts {

    private static final String TEST_MESSAGE = "test message";
    private static final Double doubleObject = Double.valueOf(0.13);
    private static final double doublePrimitive = 17.1;
    private static final int intPrimitive = 111;

    @Test
    public void testAssertThatMatcherWithMessage() {
        TS.asserts().assertThat(TEST_MESSAGE, doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().assertThat(TEST_MESSAGE, doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().assertThat(TEST_MESSAGE, intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testAssertThatMatcher() {
        TS.asserts().assertThat(doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().assertThat(doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().assertThat(intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testThatMatcherWithMessage() {
        TS.asserts().that(TEST_MESSAGE, doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().that(TEST_MESSAGE, doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().that(TEST_MESSAGE, intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testThatMatcher() {
        TS.asserts().that(doubleObject, lessThanOrEqualTo(doubleObject));
        TS.asserts().that(doublePrimitive, lessThanOrEqualTo(doublePrimitive));
        TS.asserts().that(intPrimitive, lessThanOrEqualTo(intPrimitive));
    }

    @Test
    public void testCustomAssertPass() {
        TS.asserts().customAssert(() -> {
            Assert.assertTrue("expecting to pass", true);
        });

        TS.asserts().customAssert("custom assert desc", () -> {
            Assert.assertTrue("expecting to pass", true);
        });
    }

    @Test(expected = AssertionError.class)
    public void testCustomAssertFail() {
        TS.asserts().customAssert(() -> {
            Assert.assertTrue("expecting to fail", false);
        });
    }

    @Test(expected = AssertionError.class)
    public void testCustomAssertFailWithMessage() {
        TS.asserts().customAssert("custom assert desc", () -> {
            Assert.assertTrue("expecting to fail", false);
        });
    }

}
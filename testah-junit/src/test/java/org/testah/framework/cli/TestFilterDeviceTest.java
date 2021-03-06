package org.testah.framework.cli;

import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class TestFilterDeviceTest {

    private static final String TEST_WITH_DEVICE_ONLY = "TestWithDeviceOnly";
    private Params paramsBeforeTest;

    @Before
    public void setUp() {
        paramsBeforeTest = TS.params();
        TS.setParams(new Params());
    }

    @Test
    public void testFilterTestCaseDevice() {
        final TestFilter filter = new TestFilter();
        TestCaseDto meta = new TestCaseDto().setDevices(new ArrayList<String>());
        TS.params().setFilterByTestType(null);
        meta.getDevices().add("TEST_Device");

        TS.params().setFilterByDevice(null);
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_DEVICE_ONLY));

        TS.params().setFilterByDevice("");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_DEVICE_ONLY));

        TS.params().setFilterByDevice("TEST_Device");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_DEVICE_ONLY));

        TS.params().setFilterByDevice("~TEST_Device");
        Assert.assertEquals(false, filter.filterTestCase(meta, TEST_WITH_DEVICE_ONLY));

        TS.params().setFilterByDevice("Test1, test2, TEST_Device");
        Assert.assertEquals(true, filter.filterTestCase(meta, TEST_WITH_DEVICE_ONLY));

        TS.params().setFilterByDevice("Test1, test2,~TEST_Device");
        Assert.assertEquals(false, filter.filterTestCase(meta, TEST_WITH_DEVICE_ONLY));
    }

    @Test
    public void testFilterTestPlanDevice() {

        testFilterMyDevice(1, 1, 0, 1, TestPlanWithDevice.class);

        testFilterMyDevice(1, 1, 0, 1, TestPlanWithManyDevices.class);

        testFilterMyDevice(5, 5, 3, 2, TestPlanWithDevice.class, TestPlanWithManyDevices.class,
                TestPlanWithDeviceDefault.class, TestPlanWithDeviceEmpty.class,
                TestPlanWithDeviceEmptyString.class);

    }

    private void testFilterMyDevice(final int expectedTest1, final int expectedTest2, final int expectedTest3,
                                    final int expectedTest4, final Class<?>... classesToAdd) {
        final TestFilter filter = new TestFilter();
        Set<Class<?>> classes = ImmutableSet.copyOf(Arrays.asList(classesToAdd));

        TS.params().setFilterByDevice(null);
        Assert.assertEquals(expectedTest1, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByDevice("");
        Assert.assertEquals(expectedTest2, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByDevice("~TEST_Device");
        Assert.assertEquals(expectedTest3, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());

        TS.params().setFilterByDevice("TEST_Device");
        Assert.assertEquals(expectedTest4, filter.resetTestClassesMetFilters().filterTestPlansToRun(classes)
                .size());
    }

    @After
    public void tearDown() {
        TS.setParams(paramsBeforeTest);
    }

}
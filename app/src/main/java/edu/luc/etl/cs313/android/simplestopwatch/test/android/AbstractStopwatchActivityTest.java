package edu.luc.etl.cs313.android.simplestopwatch.test.android;

import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Test;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.android.StopwatchAdapter;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.MAX_SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Abstract GUI-level test superclass of several essential stopwatch scenarios.
 *
 * @author laufer
 *
 * TODO move this and the other tests to src/test once Android Studio supports
 * non-instrumentation unit tests properly.
 */
public abstract class AbstractStopwatchActivityTest {

    /**
     * Verifies that the activity under test can be launched.*/
    @Test
    public void testActivityCheckTestCaseSetUpProperly() {
        assertNotNull("activity should be launched successfully", getActivity());
    }

    /**
     * Verifies the following scenario: time is 0.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioInit() throws Throwable {
        getActivity().runOnUiThread(() -> assertEquals(0, getDisplayedValue()));
    }

    @Test
    public void testActivityScenarioRun() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            assertTrue(getStartStopButton().performClick());
            });
        Thread.sleep(1000); // <-- do not run this in the UI thread!
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(1, getDisplayedValue());
            assertTrue(getStartStopButton().performClick());
        });
    }

    @Test
    public void testActivityScenarioInc() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            for (int i = 0; i < 9; i++) {
                assertTrue(getStartStopButton().performClick());
            }
        });
        Thread.sleep(2500);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> assertEquals(9, getDisplayedValue()));
    }

    protected abstract void runUiThreadTasks();

    @Test
    @UiThreadTest
    public void testActivityScenarioIncReset() {
            assertTrue(getStartStopButton().performClick());
        assertEquals(1, getDisplayedValue());
        assertTrue(getStartStopButton().isEnabled());
        assertTrue(getStartStopButton().isEnabled());
        assertTrue(getStartStopButton().performClick());
        assertEquals(2, getDisplayedValue());
        assertTrue(getStartStopButton().isEnabled());
        assertTrue(getStartStopButton().isEnabled());
        assertTrue(getStartStopButton().isEnabled());
        assertTrue(getStartStopButton().performClick());
        assertEquals(3, getDisplayedValue());
        assertTrue(getStartStopButton().isEnabled());
        assertTrue(getStartStopButton().isEnabled());
        assertTrue(getStartStopButton().performClick());
    }

   /* @Test
    @UiThreadTest
    public void testActivityScenarioRotation() {
        assertTrue(getStartStopButton().performClick());
        assertEquals(1, getDisplayedValue());
        assertTrue(getStartStopButton().performClick());
        assertTrue(getStartStopButton().performClick());
        assertTrue(getStartStopButton().performClick());
        assertEquals(4, getDisplayedValue());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        assertEquals(4, getDisplayedValue());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertEquals(4, getDisplayedValue());
        assertTrue(getStartStopButton().performClick());
    } */

    // auxiliary methods for easy access to UI widgets

    protected abstract StopwatchAdapter getActivity();

    protected int tvToInt(final TextView t) {
        return Integer.parseInt(t.getText().toString().trim());
    }

    protected int getDisplayedValue() {
        final TextView ts = (TextView) getActivity().findViewById(R.id.seconds);
        return tvToInt(ts);
    }

    protected Button getStartStopButton() {
        return (Button) getActivity().findViewById(R.id.startStop);
    }

}

package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import android.widget.EditText;

import edu.luc.etl.cs313.android.simplestopwatch.R;

abstract class StoppedState implements StopwatchState {
    /**
     * Switches to the stopped state when the button is clicked
     */

    public StoppedState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;
    private boolean buttonPressed = false;
    @Override
    public boolean onStartStop() {
        sm.actionTypedTime();
        if ((!buttonPressed) && (sm.getTime() != 0)) {
            sm.setEditable(false);
            sm.actionStart();
            sm.actionRingTheAlarm();
            sm.toRunningState();
        }
        else {
            sm.actionStart();  // only start the clock ticking if this is the first button press
            sm.actionInc();    //increment the timer and then return to Increment state
            sm.toIncrementState();
            }
        return false;
    }
    @Override
    public void onTick() {
        throw new UnsupportedOperationException("onTick");
    }


    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.STOPPED;
    }
}

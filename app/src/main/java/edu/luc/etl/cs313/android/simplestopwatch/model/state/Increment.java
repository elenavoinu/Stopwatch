package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.MAX_SECONDS;

class Increment implements StopwatchState {

    public Increment(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;
    private int count=0; //number on the screen
    /**
     * increment time if it's under 100 and counter isn't 0. If counter = 0, it plays sound and
     * switches to the running state. If the time is 99, it goes to the alarming state*/
    @Override
    public boolean onStartStop() {

        if (sm.getTime() < MAX_SECONDS){// MAX_SECONDS = max number of ticks
            count = 0;
            sm.actionInc();
        } else {
            sm.actionStop();
            sm.toRunningState();
        }
        return false;
    }
    @Override
    public void displayValue() {

    }
    /**If the time is greater than zero and three seconds elapse from the most recent time the button
    * was pressed, then the timer beeps once and starts running.*/
        @Override
        public void onTick() {
            count++;
            if(count==3) { //after three seconds, alarm beeps and the timer goes into the running state
                sm.actionRingTheAlarm();
                sm.toRunningState();
            }
        }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.INCREMENT;
    }

}

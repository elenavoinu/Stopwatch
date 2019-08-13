package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class AlarmingState implements StopwatchState {

    public AlarmingState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public boolean onStartStop() { //when the button is clicked in this state, it will stop the action
        // and revert to the initial stopped state
        sm.actionStop();
        sm.actionReset();
        sm.setEditable(true);
        sm.toStoppedState();
        return false;
    }

    @Override public void displayValue() { }

    @Override public void onTick() {
        sm.actionRingTheAlarm();
    } //it will beep every tick

    @Override public void updateView() { }//don't have to do anything for this method

    @Override public int getId() { return R.string.ALARMING; }

}



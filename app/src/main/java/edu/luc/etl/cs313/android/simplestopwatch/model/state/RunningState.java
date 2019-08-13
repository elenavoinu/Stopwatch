package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class RunningState implements StopwatchState {

    public RunningState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public boolean onStartStop() { //if button is clicked during the running state, then timer goes back to StoppedState
        sm.actionStop();
        sm.actionReset();
        sm.setEditable(true);
        sm.toStoppedState();
        return false;
    }

    @Override
    public void displayValue() {

    }

    /**
    * * decrements the timer by one every second while there is time left,
    when out of time, transitions to the AlarmingState*/

    @Override
    public void onTick() {
        if (sm.getTime() == 0){
            sm.toAlarmingState();
        }
        else {
            sm.actionDec();
            sm.toRunningState();
        }
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.RUNNING;
    }

}

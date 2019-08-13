package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIUpdateListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

    public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    private final TimeModel timeModel;

    private final ClockModel clockModel;
    private int typedTime;


    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private StopwatchState state;

    protected void setState(final StopwatchState state) {
        this.state = state;
        uiUpdateListener.updateState(state.getId());
    }
    private StopwatchUIUpdateListener uiUpdateListener;

    @Override
    public void setUIUpdateListener(final StopwatchUIUpdateListener uiUpdateListener) {
        this.uiUpdateListener = uiUpdateListener;
    }

    public void setEditable(boolean b) { uiUpdateListener.setEditable(b);}
    private void setTypedTime() { typedTime = uiUpdateListener.getTypedTime(); }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override public synchronized boolean onStartStop() { state.onStartStop() ; return false;}
    @Override public void displayValue() {state.onStartStop(); }
    @Override public synchronized void onTick()      { state.onTick(); }
    @Override public void updateUIRuntime() { uiUpdateListener.updateTime(timeModel.getRuntime()); }

    // known states
    private final StopwatchState STOPPED     = new StoppedState(this) {
        @Override public void displayValue() { }
    };
    private final StopwatchState RUNNING     = new RunningState(this);
    private final StopwatchState INCREMENT = new Increment(this);
    private final StopwatchState ALARMING = new AlarmingState(this);

    // transitions
    @Override public void toRunningState()    { setState(RUNNING); }
    @Override public void toStoppedState()    { setState(STOPPED); }
    @Override public void toIncrementState()    { setState(INCREMENT); }
    @Override public void toAlarmingState()    { setState(ALARMING); }

    // actions
    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetRuntime(); actionUpdateView(); }
    @Override public void actionStart()      { clockModel.start(); }
    @Override public void actionStop()       { clockModel.stop(); }
    @Override public void actionInc()        { timeModel.incRuntime(); actionUpdateView(); }
    @Override public void actionDec()        {timeModel.decRuntime(); actionUpdateView(); }
    @Override public void actionUpdateView() { state.updateView(); }
    @Override public void actionTypedTime() { setTypedTime(); timeModel.setRunningTime(typedTime); actionUpdateView(); }

    @Override public void setTime(int time) { timeModel.setRunningTime(time);}


    @Override public void actionRingTheAlarm() {uiUpdateListener.playDefaultNotification();}
    @Override public int getTime()           { return timeModel.getRuntime(); }



}



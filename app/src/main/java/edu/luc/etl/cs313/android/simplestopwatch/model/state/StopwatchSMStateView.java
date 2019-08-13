package edu.luc.etl.cs313.android.simplestopwatch.model.state;

/**
 * The restricted view states have of their surrounding state machine.
 * This is a client-specific interface in Peter Coad's terminology.
 *
 * @author laufer
 */
interface StopwatchSMStateView {

    // transitions
    void toRunningState();
    void toStoppedState();
    void toAlarmingState();
    void toIncrementState();


    // actions
    void actionInit();
    void actionReset();
    void actionStart();
    void actionStop();
 //   void actionLap();
    void actionRingTheAlarm();
    void actionInc();
    void actionDec();
    void actionUpdateView();
    void actionTypedTime();


    // state-dependent UI updates
    void updateUIRuntime();
    int getTime();//actionGetRuntime();
    void setEditable(boolean b);
}

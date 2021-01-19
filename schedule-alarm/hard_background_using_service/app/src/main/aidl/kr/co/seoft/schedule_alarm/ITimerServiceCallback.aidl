package kr.co.seoft.schedule_alarm;

interface ITimerServiceCallback {
    void onStatusChanged(int status);
    void onSecondAndIndexChanged(long second, int index);
}
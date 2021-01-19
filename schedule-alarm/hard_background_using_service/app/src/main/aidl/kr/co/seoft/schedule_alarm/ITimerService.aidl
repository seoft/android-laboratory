package kr.co.seoft.schedule_alarm;

import kr.co.seoft.schedule_alarm.ITimerServiceCallback;

interface ITimerService {
    int getStatus();
    int getCurrentIndex();
    long getRemainSecond();

    void setTimes(in int[] times);
    void start();
    void startFromIndex(int index);
    void pause();
    void stop();

    void addCallback(ITimerServiceCallback callback);
    void removeCallback();
}
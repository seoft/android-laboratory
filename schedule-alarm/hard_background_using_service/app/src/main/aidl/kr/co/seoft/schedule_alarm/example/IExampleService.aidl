package kr.co.seoft.schedule_alarm.example;

import kr.co.seoft.schedule_alarm.example.IExampleServiceCallback;

interface IExampleService {
    String getStringValue();
    int getIntValue();
    void requestStringAsync();
    void requestIntAsync();

    void addCallback(IExampleServiceCallback callback);
    void removeCallback();
}
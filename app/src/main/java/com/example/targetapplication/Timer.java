package com.example.targetapplication;

public class Timer {

    long startTime;
    long endTime;
    boolean running;

    public Timer(){
        this.startTime = 0;
        this.endTime = 0;
        running = false;
    }

    public void start(){
        running = true;
        startTime = System.currentTimeMillis();
    }
    public void stop(){
        running = false;
        endTime = System.currentTimeMillis();
    }

    public long getTotalTime(){
        long totalTime = endTime - startTime;
        return totalTime;
    }

    public boolean isRunning(){
        return running;
    }

}

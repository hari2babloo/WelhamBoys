package com.a3x3conect.welhamboys;

public interface OnTaskCompleted{
    void onTaskCompleted(String response);
    void onError(String error);
}
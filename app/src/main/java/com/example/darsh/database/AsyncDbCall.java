package com.example.darsh.database;

import android.content.Context;
import android.os.Process;

import java.lang.reflect.Method;

/**
 * Created by darshan on 19/6/16.
 */
public class AsyncDbCall {
    private Context context;

    public AsyncDbCall() {

    }

    private class DbRunnable implements Runnable {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        }
    }
}

package com.example.gruper.chatgruperexample.util;

import android.os.Binder;

import java.lang.ref.WeakReference;

/**
 * Created by gruper on 09/05/17.
 */

public class LocalBinder<S> extends Binder {
    private final WeakReference<S> mService;

    public LocalBinder(final S service) {
        mService = new WeakReference<S>(service);
    }

    public S getService() {
        return mService.get();
    }

}
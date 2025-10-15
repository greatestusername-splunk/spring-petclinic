/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot;

import com.singularity.ee.agent.appagent.boot.IThreadStateChangeListener;
import java.util.AbstractSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadInterceptorStateChangeNotifier {
    private static volatile ThreadInterceptorStateChangeNotifier instance;
    private final AbstractSet<IThreadStateChangeListener> listenerSet = new CopyOnWriteArraySet<IThreadStateChangeListener>();

    public static ThreadInterceptorStateChangeNotifier getInstance() {
        if (instance == null) {
            ThreadInterceptorStateChangeNotifier.getInstanceSync();
        }
        return instance;
    }

    private static synchronized ThreadInterceptorStateChangeNotifier getInstanceSync() {
        if (instance == null) {
            instance = new ThreadInterceptorStateChangeNotifier();
        }
        return instance;
    }

    private ThreadInterceptorStateChangeNotifier() {
    }

    public void registerNewListener(IThreadStateChangeListener listener) {
        this.listenerSet.add(listener);
    }

    public void deregisterListener(IThreadStateChangeListener listener) {
        this.listenerSet.remove(listener);
    }

    public void setInterceptorRunningOnThread(boolean isRunningInterceptor) {
        for (IThreadStateChangeListener nextListener : this.listenerSet) {
            nextListener.setInterceptorRunningOnThread(isRunningInterceptor);
        }
    }

    public void setTransactionStateForThread(boolean tranRunningOnThread) {
        for (IThreadStateChangeListener nextListener : this.listenerSet) {
            nextListener.setTransactionStateForThread(tranRunningOnThread);
        }
    }
}


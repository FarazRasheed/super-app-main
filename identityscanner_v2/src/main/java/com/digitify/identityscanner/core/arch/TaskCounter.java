package com.digitify.identityscanner.core.arch;

public class TaskCounter {
    public interface OnTaskCountCompletedListener {
        void onTaskCountCompleted();

        OnTaskCountCompletedListener DEFAULT = () -> {

        };
    }

    private int total = 0;

    private int count = 0;
    private OnTaskCountCompletedListener onTaskCountCompletedListener;

    public TaskCounter(int total) {
        this.total = total;
        this.count = total;
    }


    public void reset() {
        count = total;
    }

    public void count() {
        int prevCount = count;
        count--;
        if (count < 0) count = 0;
        if (prevCount != count && count == 0) {
            getOnTaskCountCompletedListener().onTaskCountCompleted();
        }
    }

    public void setOnTaskCountCompletedListener(OnTaskCountCompletedListener onTaskCountCompletedListener) {
        this.onTaskCountCompletedListener = onTaskCountCompletedListener;
    }

    public OnTaskCountCompletedListener getOnTaskCountCompletedListener() {
        if (onTaskCountCompletedListener == null)
            onTaskCountCompletedListener = OnTaskCountCompletedListener.DEFAULT;
        return onTaskCountCompletedListener;
    }
}

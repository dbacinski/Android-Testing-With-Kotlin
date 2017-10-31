package com.example.unittesting;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SchedulersFactory {

    public <T> ObservableTransformer<T, T> createMainThreadSchedulerTransformer() {
        return new SchedulersTransformer<>();
    }

    static class SchedulersTransformer<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.observeOn(AndroidSchedulers.mainThread());
        }
    }
}

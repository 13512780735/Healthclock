package com.healthclock.healthclock.network.helper.rxjavahelper;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * user：lqm
 * desc：自己的Observer，减少实现不必要的回调
 */
public abstract class RxObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
        _onSubscribe(d);
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        _onError(e.getMessage());
    }

    @Override
    public void onComplete() {
        _onComplete();
    }

    public void _onSubscribe(Disposable d) {

    }


    //抽象方法，必须实现
    public abstract void _onNext(T t);

    public abstract void _onComplete();

    public abstract void _onSubscribe(String d);

    public abstract void _onError(String errorMessage);


}
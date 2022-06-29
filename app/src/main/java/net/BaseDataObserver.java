package net;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * 数据观察者
 */
public abstract class BaseDataObserver<M> extends DisposableObserver<BaseRespond<M>> {

    public BaseDataObserver() {

    }

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
            onFailure(msg);
        } else {
            onFailure(throwable.getMessage());
        }
    }

    @Override
    public void onNext(@NonNull BaseRespond<M> baseRespond) {
        if (baseRespond.getCode() != 200) {
            onFailure(baseRespond.getMsg());
        } else {
            onSuccess(baseRespond.getData());
        }
    }

    @Override
    public void onComplete() {
    }
}

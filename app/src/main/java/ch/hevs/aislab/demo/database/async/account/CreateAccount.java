package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class CreateAccount extends AsyncTask<AccountEntity, Void, Long> {

    private Application mApplication;
    private OnAsyncEventListener mCallBack;

    public CreateAccount(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Long doInBackground(AccountEntity... params) {
        return ((BasicApp) mApplication).getAccountRepository().insert(params[0]);
    }

    @Override
    protected void onPostExecute(Long aLong) {
        if (mCallBack != null) {
            mCallBack.onSuccess(aLong);
        }
    }
}

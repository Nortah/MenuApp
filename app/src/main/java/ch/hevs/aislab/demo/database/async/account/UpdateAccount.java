package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class UpdateAccount extends AsyncTask<AccountEntity, Void, Void> {

    private Application mApplication;
    private OnAsyncEventListener mCallBack;

    public UpdateAccount(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(AccountEntity... params) {
        for (AccountEntity account : params)
            ((BasicApp) mApplication).getAccountRepository().update(account);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallBack != null) {
            mCallBack.onSuccess(null);
        }
    }
}

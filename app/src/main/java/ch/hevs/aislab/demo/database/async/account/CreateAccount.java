package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class CreateAccount extends AsyncTask<AccountEntity, Void, Long> {

    private Application mApplication;

    public CreateAccount(Application application) {
        mApplication = application;
    }

    @Override
    protected Long doInBackground(AccountEntity... params) throws SQLiteConstraintException {
        return ((BasicApp) mApplication).getAccountRepository().insert(params[0]);
    }
}

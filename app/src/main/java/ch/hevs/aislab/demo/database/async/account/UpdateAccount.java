package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class UpdateAccount extends AsyncTask<AccountEntity, Void, Void> {

    private Application mApplication;

    public UpdateAccount(Application application) {
        mApplication = application;
    }

    @Override
    protected Void doInBackground(AccountEntity... params) {
        for (AccountEntity account : params)
            ((BasicApp) mApplication).getAccountRepository().update(account);
        return null;
    }
}

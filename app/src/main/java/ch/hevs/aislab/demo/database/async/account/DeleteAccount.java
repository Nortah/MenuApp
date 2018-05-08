package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class DeleteAccount extends AsyncTask<AccountEntity, Void, Void> {

    private Application mApplication;

    public DeleteAccount(Application application) {
        mApplication = application;
    }

    @Override
    protected Void doInBackground(AccountEntity... params) {
        for (AccountEntity account : params)
            ((BasicApp) mApplication).getAccountRepository().delete(account);
        return null;
    }
}

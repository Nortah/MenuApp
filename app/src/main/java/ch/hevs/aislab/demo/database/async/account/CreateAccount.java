package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class CreateAccount extends AsyncTask<AccountEntity, Void, Void> {

    private Application mApplication;

    public CreateAccount(Application application) {
        mApplication = application;
    }

    @Override
    protected Void doInBackground(AccountEntity... params) {
        ((BasicApp) mApplication).getAccountRepository().insert(params[0]);
        return null;
    }
}

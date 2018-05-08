package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Pair;

import java.lang.ref.WeakReference;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.repository.AccountRepository;

public class TransactionAccount extends AsyncTask<Pair<AccountEntity, AccountEntity>, Void, Void> {

    private Application mApplication;

    public TransactionAccount(Application application) {
        mApplication = application;
    }

    @Override
    protected Void doInBackground(Pair<AccountEntity, AccountEntity>[] pairs) {
        for (Pair<AccountEntity, AccountEntity> accounts : pairs)
            ((BasicApp) mApplication).getAccountRepository().transaction(accounts.first, accounts.second);
        return null;
    }
}

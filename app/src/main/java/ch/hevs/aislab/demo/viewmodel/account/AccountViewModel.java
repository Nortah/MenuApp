package ch.hevs.aislab.demo.viewmodel.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.async.account.CreateAccount;
import ch.hevs.aislab.demo.database.async.account.UpdateAccount;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.repository.AccountRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class AccountViewModel  extends AndroidViewModel {

    private static final String TAG = "AccountViewModel";

    private Application mApplication;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<AccountEntity> mObservableAccount;

    public AccountViewModel(@NonNull Application application,
                                   final Long accountId, AccountRepository repository) {
        super(application);

        mApplication = application;

        mObservableAccount = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableAccount.setValue(null);

        LiveData<AccountEntity> account = repository.getAccount(accountId);

        // observe the changes of the account entity from the database and forward them
        mObservableAccount.addSource(account, mObservableAccount::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Long mAccountId;

        private final AccountRepository mRepository;

        public Factory(@NonNull Application application, Long accountId) {
            mApplication = application;
            mAccountId = accountId;
            mRepository = ((BasicApp) application).getAccountRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AccountViewModel(mApplication, mAccountId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<AccountEntity> getAccount() {
        return mObservableAccount;
    }

    public void updateAccount(AccountEntity account) {
        new UpdateAccount(mApplication).execute(account);
    }

    public void createAccount(AccountEntity account) {
        new CreateAccount(mApplication).execute(account);
    }
}
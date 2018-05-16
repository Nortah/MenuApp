package ch.hevs.aislab.demo.viewmodel.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.async.account.CreateAccount;
import ch.hevs.aislab.demo.database.async.account.DeleteAccount;
import ch.hevs.aislab.demo.database.async.account.UpdateAccount;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.repository.AccountRepository;

public class OwnAccountListViewModel extends AndroidViewModel {

    private static final String TAG = "OwnAccountListViewModel";

    private Application mApplication;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<AccountEntity>> mObservableAccounts;

    public OwnAccountListViewModel(@NonNull Application application,
                                   final String ownerId, AccountRepository repository) {
        super(application);

        mApplication = application;

        mObservableAccounts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableAccounts.setValue(null);

        LiveData<List<AccountEntity>> accounts = repository.getByOwner(ownerId);

        // observe the changes of the products from the database and forward them
        mObservableAccounts.addSource(accounts, mObservableAccounts::setValue);
    }

    /**
     * A creator is used to inject the account owner id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mOwnerId;

        private final AccountRepository mRepository;

        public Factory(@NonNull Application application, String accountId) {
            mApplication = application;
            mOwnerId = accountId;
            mRepository = ((BasicApp) application).getAccountRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new OwnAccountListViewModel(mApplication, mOwnerId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntities query so the UI can observe it.
     */
    public LiveData<List<AccountEntity>> getOwnAccounts() {
        return mObservableAccounts;
    }

    public void deleteAccount(AccountEntity account) {
        new DeleteAccount(mApplication).execute(account);
    }
}

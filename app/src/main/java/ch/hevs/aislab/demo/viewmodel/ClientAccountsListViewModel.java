package ch.hevs.aislab.demo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.List;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.async.account.TransactionAccount;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.pojo.ClientAccounts;
import ch.hevs.aislab.demo.database.repository.ClientRepository;

public class ClientAccountsListViewModel extends AndroidViewModel {

    private static final String TAG = "ClientAccountsListViewModel";

    private Application mApplication;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ClientAccounts>> mObservableClientAccounts;

    public ClientAccountsListViewModel(@NonNull Application application,
                                       final String accountId, ClientRepository repository) {
        super(application);

        mApplication = application;

        mObservableClientAccounts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableClientAccounts.setValue(null);

        LiveData<List<ClientAccounts>> clientAccounts = repository.getOtherClientsWithAccounts(accountId);

        // observe the changes of the products from the database and forward them
        mObservableClientAccounts.addSource(clientAccounts, mObservableClientAccounts::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mAccountId;

        private final ClientRepository mRepository;

        public Factory(@NonNull Application application, String accountId) {
            mApplication = application;
            mAccountId = accountId;
            mRepository = ((BasicApp) application).getClientRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ClientAccountsListViewModel(mApplication, mAccountId, mRepository);
        }
    }

    /**
     * Expose the LiveData ClientAccounts query so the UI can observe it.
     */
    public LiveData<List<ClientAccounts>> getClientAccounts() {
        return mObservableClientAccounts;
    }

    public void executeTransaction(final AccountEntity sender, final AccountEntity recipient) {
        //noinspection unchecked
        new TransactionAccount(mApplication).execute(new Pair<>(sender, recipient));
    }
}

package ch.hevs.aislab.demo.viewmodel.client;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.async.client.CreateClient;
import ch.hevs.aislab.demo.database.async.client.DeleteClient;
import ch.hevs.aislab.demo.database.async.client.UpdateClient;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.repository.ClientRepository;

public class ClientViewModel extends AndroidViewModel {

    private static final String TAG = "AccountViewModel";

    private Application mApplication;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ClientEntity> mObservableClient;

    public ClientViewModel(@NonNull Application application,
                            final String clientId, ClientRepository repository) {
        super(application);

        mApplication = application;

        mObservableClient = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableClient.setValue(null);

        LiveData<ClientEntity> account = repository.getClient(clientId);

        // observe the changes of the client entity from the database and forward them
        mObservableClient.addSource(account, mObservableClient::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mClientId;

        private final ClientRepository mRepository;

        public Factory(@NonNull Application application, String clientId) {
            mApplication = application;
            mClientId = clientId;
            mRepository = ((BasicApp) application).getClientRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ClientViewModel(mApplication, mClientId, mRepository);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<ClientEntity> getClient() {
        return mObservableClient;
    }

    public void updateClient(ClientEntity client) {
        new UpdateClient(mApplication).execute(client);
    }

    public void deleteClient(ClientEntity client) {
        new DeleteClient(mApplication).execute(client);
    }
}

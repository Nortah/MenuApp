package ch.hevs.aislab.demo.database.async.client;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class UpdateClient extends AsyncTask<ClientEntity, Void, Void> {

    private Application mApplication;

    public UpdateClient(Application application) {
        mApplication = application;
    }

    @Override
    protected Void doInBackground(ClientEntity... params) {
        for (ClientEntity client : params)
            ((BasicApp) mApplication).getClientRepository().update(client);
        return null;
    }
}

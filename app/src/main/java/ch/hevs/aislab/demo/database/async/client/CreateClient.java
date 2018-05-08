package ch.hevs.aislab.demo.database.async.client;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class CreateClient extends AsyncTask<ClientEntity, Void, Boolean> {

    private Application mApplication;

    public CreateClient(Application application) {
        mApplication = application;
    }

    @Override
    protected Boolean doInBackground(ClientEntity... params) {
        boolean response = true;
        try {
            for (ClientEntity client : params)
                ((BasicApp) mApplication).getClientRepository()
                        .insert(client);
        } catch (SQLiteConstraintException e) {
            response = false;
        }
        return response;
    }
}

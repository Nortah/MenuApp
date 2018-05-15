package ch.hevs.aislab.demo.database.async.client;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import ch.hevs.aislab.demo.BasicApp;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class CreateClient extends AsyncTask<ClientEntity, Void, Void> {

    private static final String TAG = "CreateClient";

    private Application mApplication;
    private OnAsyncEventListener<Boolean> mCallBack;
    private Exception mException;

    public CreateClient(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(ClientEntity... params) {
        try {
            for (ClientEntity client : params)
                ((BasicApp) mApplication).getClientRepository()
                        .insert(client);
        } catch (SQLiteConstraintException e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(null);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}

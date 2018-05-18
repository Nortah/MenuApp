package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientAccounts;

public class ClientRepository {

    private static ClientRepository sInstance;

    private final AppDatabase mDatabase;

    private Executor mDatabaseIO;

    private ClientRepository(final AppDatabase database) {
        mDatabase = database;
        mDatabaseIO = Executors.newSingleThreadExecutor();
    }

    public static ClientRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (AccountRepository.class) {
                if (sInstance == null) {
                    sInstance = new ClientRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ClientEntity> getClient(final String clientId) {
        return mDatabase.clientDao().getById(clientId);
    }

    public LiveData<List<ClientEntity>> getClients() {
        return mDatabase.clientDao().getAll();
    }

    public LiveData<List<ClientAccounts>> getOtherClientsWithAccounts(final String owner) {
        return mDatabase.clientDao().getOtherClientsWithAccounts(owner);
    }

    public void insert(final ClientEntity client) {
        mDatabaseIO.execute(() -> {
            mDatabase.clientDao().insert(client);
        });
    }

    public void update(final ClientEntity client) {
        mDatabaseIO.execute(() -> {
            mDatabase.clientDao().update(client);
        });
    }

    public void delete(final ClientEntity client) {
        mDatabaseIO.execute(() -> {
            mDatabase.clientDao().delete(client);
        });
    }
}

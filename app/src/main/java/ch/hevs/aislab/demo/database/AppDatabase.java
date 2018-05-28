package ch.hevs.aislab.demo.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ch.hevs.aislab.demo.database.dao.AccountDao;
import ch.hevs.aislab.demo.database.dao.ClientDao;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

@Database(entities = {AccountEntity.class, ClientEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    private static AppDatabase sInstance;

    private static final String DATABASE_NAME = "bank-database";

    public abstract AccountDao accountDao();

    public abstract ClientDao clientDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        Log.i(TAG, "Database will be initialized.");
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            // Generate the data for pre-population
                            AppDatabase database = AppDatabase.getInstance(appContext);
                            List<ClientEntity> clients = DataGenerator.generateClients();
                            List<AccountEntity> accounts =
                                    DataGenerator.generateAccountsForClients(clients);

                            insertData(database, clients, accounts);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            Log.i(TAG, "Database initialized.");
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public static void insertData(final AppDatabase database, final List<ClientEntity> clients,
                                   final List<AccountEntity> accounts) {
        database.runInTransaction(() -> {
            Log.i(TAG, "Insert demo data.");
            database.clientDao().insertAll(clients);
            database.accountDao().insertAll(accounts);
        });
    }

    public static void clearData(final AppDatabase database) {
        database.runInTransaction(() -> {
            Log.i(TAG, "Wipe database.");
            database.clientDao().deleteAll();
            database.accountDao().deleteAll();
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}

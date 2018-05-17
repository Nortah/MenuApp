package ch.hevs.aislab.demo.ui.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.adapter.ListAdapter;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientAccounts;
import ch.hevs.aislab.demo.ui.MainActivity;
import ch.hevs.aislab.demo.ui.util.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.account.AccountListViewModel;

public class TransactionActivity extends BaseActivity {

    private final String TAG = "TransactionFragment";

    private AccountEntity mFromAccount;
    private AccountEntity mToAccount;

    private SortedMap<ClientEntity, List<AccountEntity>> mClientEntityMultimap;

    private ListAdapter<AccountEntity> mAdapterFromAccount;
    private ListAdapter<ClientEntity> mAdapterClient;
    private ListAdapter<AccountEntity> mAdapterToAccount;

    private AccountListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_transaction, frameLayout);

        setTitle(getString(R.string.title_activity_transaction));
        navigationView.setCheckedItem(position);

        setupFromAccSpinner();
        setupToAccSpinner();
        setupViewModels();

        final Toast toast = Toast.makeText(this, getString(R.string.transaction_executed), Toast.LENGTH_LONG);
        Button transactionBtn = findViewById(R.id.btn_transaction);
        transactionBtn.setOnClickListener(view -> {
            executeTransaction();
            toast.show();
        });
    }

    private void setupViewModels() {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        String user = settings.getString(MainActivity.PREFS_USER, null);

        AccountListViewModel.Factory factory = new AccountListViewModel.Factory(
                getApplication(), user);
        mViewModel = ViewModelProviders.of(this, factory).get(AccountListViewModel.class);
        mViewModel.getOwnAccounts().observe(this, accountEntities -> {
            if (accountEntities != null) {
                updateFromAccSpinner(accountEntities);
            }
        });

        mViewModel.getClientAccounts().observe(this, clientAccounts -> {
            if (clientAccounts != null) {
                setupMap(clientAccounts);
            }
        });
    }

    /*
    This is reinitializing the data for ToClient and ToAccount every time there are changes
    coming from the ViewModel.
     */
    private void setupMap(List<ClientAccounts> clientAccounts) {
        mClientEntityMultimap = new TreeMap<>();
        for (ClientAccounts cA : clientAccounts) {
            mClientEntityMultimap.put(cA.client, cA.accounts);
        }
        setupToClientSpinner();
    }

    private void setupFromAccSpinner() {
        Spinner spinnerFromAccount = findViewById(R.id.spinner_from);
        mAdapterFromAccount = new ListAdapter<>(this, R.layout.row_client, new ArrayList<>());
        spinnerFromAccount.setAdapter(mAdapterFromAccount);
        spinnerFromAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFromAccount = (AccountEntity) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void setupToClientSpinner() {
        Spinner spinnerToClient = findViewById(R.id.spinner_toClient);
        mAdapterClient = new ListAdapter<>(this, R.layout.row_client,
                new ArrayList<>(mClientEntityMultimap.keySet())
        );
        spinnerToClient.setAdapter(mAdapterClient);
        spinnerToClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateToAccSpinner((ClientEntity) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void setupToAccSpinner() {
        Spinner spinnerToAccount = findViewById(R.id.spinner_toAcc);
        mAdapterToAccount = new ListAdapter<>(this, R.layout.row_client, new ArrayList<>());
        spinnerToAccount.setAdapter(mAdapterToAccount);
        spinnerToAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mToAccount = (AccountEntity) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void updateToAccSpinner(ClientEntity client) {
        mAdapterToAccount.updateData(mClientEntityMultimap.get(client));
    }

    private void updateFromAccSpinner(List<AccountEntity> accounts) {
        mAdapterFromAccount.updateData(new ArrayList<>(accounts));
    }

    private void executeTransaction() {
        EditText amountEditText = findViewById(R.id.transaction_amount);
        Double amount = Double.parseDouble(amountEditText.getText().toString());
        if (amount < 0.0d) {
            amountEditText.setError(getString(R.string.error_transaction_negativ));
            amountEditText.requestFocus();
            return;
        }
        if (mFromAccount.getBalance() - amount < 0.0d) {
            amountEditText.setError(getString(R.string.error_transaction));
            amountEditText.requestFocus();
            return;
        }
        mFromAccount.setBalance(mFromAccount.getBalance() - amount);
        mToAccount.setBalance(mToAccount.getBalance() + amount);
        mViewModel.executeTransaction(mFromAccount, mToAccount);
    }
}

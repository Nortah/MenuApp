package ch.hevs.aislab.demo.ui.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    private String mUser;
    private ClientEntity mLoggedIn;
    private AccountEntity mFromAccount;
    private AccountEntity mToAccount;

    //private List<AccountEntity> mClientAccounts;
    private List<AccountEntity> mOwnAccounts;
    //private List<ClientEntity> mClients;
    private List<ClientAccounts> mClientAccounts;

    private Spinner mSpinnerFromAccount;
    private Spinner mSpinnerToClient;
    private Spinner mSpinnerToAccount;

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

        mClientAccounts = new ArrayList<>();
        mOwnAccounts = new ArrayList<>();

        AccountListViewModel.Factory factory = new AccountListViewModel.Factory(
                getApplication(), user);
        mViewModel = ViewModelProviders.of(this, factory).get(AccountListViewModel.class);
        mViewModel.getOwnAccounts().observe(this, accountEntities -> {
            if (accountEntities != null) {
                mOwnAccounts = accountEntities;
                setupFromAccSpinner();
            }
        });

        mViewModel.getClientAccounts().observe(this, clientAccounts -> {
            if (clientAccounts != null) {
                mClientAccounts = clientAccounts;
                //setupToClientSpinner();
            }
        });
    }

    private void setupFromAccSpinner() {
        mSpinnerFromAccount = findViewById(R.id.spinner_from);
        mAdapterFromAccount = new ListAdapter<>(this, R.layout.row_client, mOwnAccounts);
        mSpinnerFromAccount.setAdapter(mAdapterFromAccount);
        mSpinnerFromAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFromAccount = (AccountEntity) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void setupToClientSpinner(List<ClientEntity> clientEntities) {
        mSpinnerToClient = findViewById(R.id.spinner_toClient);
        mAdapterClient = new ListAdapter<>(this, R.layout.row_client, clientEntities);
        mSpinnerToClient.setAdapter(mAdapterClient);
        mSpinnerToClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //populateToAccount((ClientEntity) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void setupToAccSpinner(List<AccountEntity> accountEntities) {
        mSpinnerToAccount = findViewById(R.id.spinner_toAcc);
        mAdapterToAccount = new ListAdapter<>(this, R.layout.row_client, accountEntities);
        mSpinnerToAccount.setAdapter(mAdapterToAccount);
        mSpinnerToAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mToAccount = (AccountEntity) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void updateToAccSpinner(List<AccountEntity> accountEntities) {
        mAdapterToAccount.updateData(accountEntities);
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

package ch.hevs.aislab.demo.ui.account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.adapter.RecyclerAdapter;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.MainActivity;
import ch.hevs.aislab.demo.util.RecyclerViewItemClickListener;
import ch.hevs.aislab.demo.viewmodel.account.OwnAccountListViewModel;

public class AccountsActivity extends BaseActivity {

    private static final String TAG = "ClientsFragment";

    private List<AccountEntity> mAccounts;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter<AccountEntity> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OwnAccountListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_accounts, frameLayout);

        setTitle(getString(R.string.title_activity_accounts));
        navigationView.setCheckedItem(position);

        mRecyclerView = findViewById(R.id.accountsRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        String user = settings.getString(MainActivity.PREFS_USER, null);

        // specify an adapter (see also next example)
        mAccounts = new ArrayList<>();
        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mAccounts.get(position).getName());

                Intent intent = new Intent(AccountsActivity.this, AccountActivity.class);
                intent.putExtra("accountId", mAccounts.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + mAccounts.get(position).getName());

                createDeleteDialog(position);
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> startActivity(
                new Intent(AccountsActivity.this, EditAccountActivity.class))
        );

        OwnAccountListViewModel.Factory factory = new OwnAccountListViewModel.Factory(
                getApplication(), user);
        mViewModel = ViewModelProviders.of(this, factory).get(OwnAccountListViewModel.class);
        mViewModel.getOwnAccounts().observe(this, accountEntities -> {
            if (accountEntities != null) {
                mAccounts = accountEntities;
                mAdapter.setData(mAccounts);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    private void createDeleteDialog(final int position) {
        final AccountEntity account = mAccounts.get(position);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.row_delete_item, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.title_activity_delete_account));
        alertDialog.setCancelable(false);

        final TextView deleteMessage = view.findViewById(R.id.tv_delete_item);
        deleteMessage.setText(String.format(getString(R.string.account_delete_msg), account.getName()));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept), (dialog, which) -> {
            Toast toast = Toast.makeText(this, getString(R.string.account_deleted), Toast.LENGTH_LONG);
            mViewModel.deleteAccount(account);
            toast.show();
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }
}

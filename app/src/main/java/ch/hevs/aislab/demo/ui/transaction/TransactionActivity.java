package ch.hevs.aislab.demo.ui.transaction;

import android.os.Bundle;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.ui.util.BaseActivity;

public class TransactionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_transaction, frameLayout);

        setTitle(getString(R.string.title_activity_transaction));
        navigationView.setCheckedItem(position);
    }
}

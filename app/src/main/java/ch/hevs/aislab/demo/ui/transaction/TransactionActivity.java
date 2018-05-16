package ch.hevs.aislab.demo.ui.transaction;

import android.os.Bundle;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.ui.BaseActivity;

public class TransactionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_transaction);
        /**
         * Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_transaction, frameLayout);

        /**
         * Setting title and itemChecked
         */
        setTitle(getString(R.string.title_activity_transaction));

        navigationView.setCheckedItem(position);
    }
}

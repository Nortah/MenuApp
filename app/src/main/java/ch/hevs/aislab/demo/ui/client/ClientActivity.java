package ch.hevs.aislab.demo.ui.client;

import android.os.Bundle;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.ui.BaseActivity;

public class ClientActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_client);

        setContentView(R.layout.activity_client);
    }
}

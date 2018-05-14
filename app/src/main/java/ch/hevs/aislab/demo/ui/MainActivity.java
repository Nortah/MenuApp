package ch.hevs.aislab.demo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.ui.mgmt.LoginActivity;
import ch.hevs.aislab.demo.ui.mgmt.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";

    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREFS_USER = "LoggedIn";
    public static final String PREFS_LNG = "Language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_logout));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.logout_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_logout), (dialog, which) -> logout());
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_client) {
            intent = new Intent(this, LoginActivity.class);
        } else if (id == R.id.nav_accounts) {
            intent = new Intent(this, LoginActivity.class);
        } else if (id == R.id.nav_transaction) {
            intent = new Intent(this, LoginActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareDrawerMenu(Menu menu) {
        MenuItem client = menu.findItem(R.id.nav_client);
        MenuItem accounts = menu.findItem(R.id.nav_accounts);
        MenuItem transaction = menu.findItem(R.id.nav_transaction);

        client.setTitle(R.string.client_fragment_title);
        accounts.setTitle(R.string.accounts_fragment_title);
        transaction.setTitle(R.string.fragment_title_transaction);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
        editor.remove(PREFS_USER);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}

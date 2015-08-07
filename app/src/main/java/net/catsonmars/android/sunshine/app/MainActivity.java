package net.catsonmars.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.catsonmars.android.sunshine.app.sync.SunshineSyncAdapter;


public class MainActivity extends ActionBarActivity
        implements ForecastFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation;
    private Boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "In onCreate");

        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;

            // see 5.15_action_bar
            // This will get rid of an unnecessary shadow below the action bar for smaller screen devices like phones.
            // Then the action bar and Today item will appear to be on the same plane (as opposed to two different planes,
            // where one casts a shadow on the other).
            getSupportActionBar().setElevation(0f);
        }

        ForecastFragment forecastFragment = ((ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast));
        forecastFragment.setUseTodayLayout(!mTwoPane);

        SunshineSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "In onDestroy");

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "In onStop");

        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "In onStart");

        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "In onResume");

        super.onResume();

        String currentLocation = Utility.getPreferredLocation(this);

        if (currentLocation != null && !currentLocation.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if (null != ff) {
                ff.onLocationChanged();
            }

            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if (null != df) {
                df.onLocationChanged(currentLocation);
            }

            mLocation = currentLocation;
        }
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "In onPause");

        super.onPause();
    }

    public void onItemSelected(Uri dateUri) {
        if (true == mTwoPane) {
            // on tablet, replace DetailFragment
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, dateUri);

            DetailFragment f = new DetailFragment();
            f.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, f, DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            // on phone, launch DetailActivity
            Intent intent = new Intent(this, DetailActivity.class).setData(dateUri);
            startActivity(intent);
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));

            return true;
        } else if (id == R.id.action_map) {
            openPreferredLocationInMap();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {

        String location = Utility.getPreferredLocation(this);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (intent.resolveActivity(getPackageManager()) != null) {

            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", location)
                    .build();

            intent.setData(geoLocation);

            startActivity(intent);
        } else {
            Log.d(LOG_TAG, String.format("Couldn't call %s, no map app found", location));
        }
    }
}

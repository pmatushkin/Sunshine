package net.catsonmars.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String FORECASTFRAGMENT_TAG = "FORECASTFRAGMENT_TAG";
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "In onCreate");

        mLocation = Utility.getPreferredLocation(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
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

        if (currentLocation != mLocation) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);

            ff.onLocationChanged();
            mLocation = currentLocation;
        }

    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "In onPause");

        super.onPause();
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

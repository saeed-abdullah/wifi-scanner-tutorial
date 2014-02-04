package edu.cornell.cs.pac.ubicomp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        setUpWifi();
    }

    /**
     * Sets up the wifi manager and registers for receiving broadcast
     */
    private void setUpWifi() {
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleWifiScanResult();
            }
        }, intentFilter);
    }

    /**
     * Handler for button click event
     *
     * @param view: The scan button
     */
    public void startScan(View view) {
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Enabling Wifi", Toast.LENGTH_LONG);
            wifiManager.setWifiEnabled(true);
        }

        wifiManager.startScan();
    }

    /**
     * Gets Wifi scanning result.
     * <p/>
     * This method gets called after scanning has finished.
     */
    private void handleWifiScanResult() {
        // Get location label
        String location = ((EditText) findViewById(R.id.edit_label)).getText().toString();

        // Iterates over the access points
        StringBuffer sb = new StringBuffer();
        for (ScanResult e : wifiManager.getScanResults()) {
            sb.append(e.BSSID);
            sb.append(": ");
            sb.append(e.SSID);
            sb.append("\n");
        }

        // Now, populate the text view.
        TextView textView = (TextView) findViewById(R.id.text_scan_result);
        textView.setText(sb.toString());
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


}

package net.ddns.cpsark.tmw;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks{

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private  static final String WEAR_MESSAGE_PATH = "/message";

    private BoxInsetLayout mContainerView;
    private boolean runningStatus = false;
    private TextView bottomText;
    private Spinner dropDown;
    private String[] item = {"item", "item2","item3"};

    private GoogleApiClient mApiClient;
    private ArrayAdapter<String> mAdapter;

    /**TODO: Add control to the dropdown menu such that it can be used to select record type
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        bottomText = (TextView) findViewById(R.id.bottomText);
        bottomText.setText(String.valueOf(runningStatus));
        dropDown = (Spinner) findViewById(R.id.dropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();
        if(mApiClient != null && !(mApiClient.isConnected() || mApiClient.isConnecting())) mApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mApiClient != null && !(mApiClient.isConnected() || mApiClient.isConnecting())) mApiClient.connect();
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
        System.out.println("Stuff");
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH)){
                    bottomText.setText(new String(messageEvent.getData()));
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mApiClient, this);
    }

    @Override
    protected void onStop() {
        if(mApiClient != null){
            Wearable.MessageApi.removeListener(mApiClient,this);
            if(mApiClient.isConnected()) {
                mApiClient.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            mContainerView.setBackground(null);
        }
    }

    /**Todo: add logic to record the type from the dropdown and store that togheter with the timestamp in a xml file*/
    public void actionButton(View view) {
        System.out.println("pressed the button");
        runningStatus = !runningStatus;
        bottomText.setText(String.valueOf(runningStatus));

    }


}

package net.ddns.cpsark.tmw;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private boolean runningStatus = false;
    private TextView bottomText;
    private Spinner dropDown;
    private String[] item = {"item", "item2","item3"};

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
        //dropDown.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
        System.out.println("Stuff");
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

package exercise.find.roots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiverForSuccess = null;
    private BroadcastReceiver broadcastReceiverForFailure = null;
    private ProgressBar progressBar;
    private EditText editTextUserInput;
    private Button buttonCalculateRoots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        editTextUserInput = findViewById(R.id.editTextInputNumber);
        buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);

        // set initial UI:
        progressBar.setVisibility(View.GONE); // hide progress
        editTextUserInput.setText(""); // cleanup text in edit-text
        editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
        buttonCalculateRoots.setEnabled(false); // set button as disabled (user can't click)

        // set listener on the input written by the keyboard to the edit-text
        editTextUserInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                // text did change
                String newText = editTextUserInput.getText().toString();
                buttonCalculateRoots.setEnabled(strToPosLong(newText) > 0);
            }
        });

        // set click-listener to the button
        buttonCalculateRoots.setOnClickListener(v -> {
            String userInputString = editTextUserInput.getText().toString();
            long userInputLong = strToPosLong(userInputString);
            if (userInputLong < 0) {
                Toast.makeText(MainActivity.this, "Positive number expected", Toast.LENGTH_SHORT).show();
                buttonCalculateRoots.setEnabled(false);
                return;
            }
            Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
            intentToOpenService.putExtra("number_for_service", userInputLong);
            startService(intentToOpenService);

            startCalculationChangeStates();
        });

        // register a broadcast-receiver to handle action "found_roots"
        broadcastReceiverForSuccess = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent incomingIntent) {
                if (incomingIntent == null || !incomingIntent.getAction().equals("found_roots"))
                    return;
                // success finding roots!
                endCalculationChangeStates();
                Intent successIntent = new Intent(MainActivity.this, SuccessScreenActivity.class);
                successIntent.putExtra("original_number", incomingIntent.getLongExtra("original_number", -1));
                successIntent.putExtra("root1", incomingIntent.getLongExtra("root1", -1));
                successIntent.putExtra("root2", incomingIntent.getLongExtra("root2", -1));
                successIntent.putExtra("calculation_time_seconds", incomingIntent.getDoubleExtra("calculation_time_seconds", -1));
                startActivity(successIntent);
            }
        };
        registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));


        // register a broadcast-receiver to handle action "stopped_calculations"
        broadcastReceiverForFailure = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent incomingIntent) {
                if (incomingIntent == null || !incomingIntent.getAction().equals("stopped_calculations"))
                    return;
                // failed finding roots!
                endCalculationChangeStates();

                long abortedTime = incomingIntent.getLongExtra("time_until_give_up_seconds", -1);
                Toast.makeText(MainActivity.this,
                        "calculation aborted after " + abortedTime + " seconds", Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(broadcastReceiverForFailure, new IntentFilter("stopped_calculations"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(broadcastReceiverForSuccess);
        this.unregisterReceiver(broadcastReceiverForFailure);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityState oldState = new ActivityState(editTextUserInput.getText().toString(),
                editTextUserInput.isEnabled(),
                buttonCalculateRoots.isEnabled(),
                progressBar.getVisibility());
        outState.putSerializable("old_state", oldState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Serializable prevState = savedInstanceState.getSerializable("old_state");
        if (!(prevState instanceof ActivityState)) {
            return;
        }
        restoreActivityState((ActivityState) prevState);
    }


    /**
     * @param str string to convert to long
     * @return the long value of the string or -1 if the string is not a positive long
     */
    private long strToPosLong(String str) {
        try {
            long n = Long.parseLong(str);
            return n > 0 ? n : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void endCalculationChangeStates() {
        // ToDo: keep old value? or reset the editText?
        progressBar.setVisibility(View.GONE);
        editTextUserInput.setText("");
        editTextUserInput.setEnabled(true);
        buttonCalculateRoots.setEnabled(false);
//        buttonCalculateRoots.setEnabled(strToPosLong(editTextUserInput.getText().toString()) > 0);
    }

    private void startCalculationChangeStates() {
        progressBar.setVisibility(View.VISIBLE);
        editTextUserInput.setEnabled(false);
        buttonCalculateRoots.setEnabled(false);
    }

    private void restoreActivityState(ActivityState oldState) {
        editTextUserInput.setText(oldState.editTextUserInputText);
        editTextUserInput.setEnabled(oldState.editTextUserInputEnableState);
        buttonCalculateRoots.setEnabled(oldState.buttonCalculateRootsEnableState);
        progressBar.setVisibility(oldState.progressBarVisibilityState);
    }


    private static class ActivityState implements Serializable {
        String editTextUserInputText;
        boolean editTextUserInputEnableState;
        boolean buttonCalculateRootsEnableState;
        int progressBarVisibilityState;

        public ActivityState(String editTextUserInputText, boolean editTextUserInputEnableState,
                             boolean buttonCalculateRootsEnableState,
                             int progressBarVisibilityState) {
            this.editTextUserInputText = editTextUserInputText;
            this.editTextUserInputEnableState = editTextUserInputEnableState;
            this.buttonCalculateRootsEnableState = buttonCalculateRootsEnableState;
            this.progressBarVisibilityState = progressBarVisibilityState;
        }
    }
}
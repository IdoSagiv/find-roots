package exercise.find.roots;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

    @Test
    public void when_activityIsLaunching_then_theButtonShouldStartDisabled() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // test: make sure that the "calculate" button is disabled
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        assertFalse(button.isEnabled());
    }

    @Test
    public void when_activityIsLaunching_then_theEditTextShouldStartEmpty() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // test: make sure that the "input" edit-text has no text
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        String input = inputEditText.getText().toString();
        assertTrue(input == null || input.isEmpty());
    }

    @Test
    public void when_userIsEnteringNumberInput_and_noCalculationAlreadyHappened_then_theButtonShouldBeEnabled() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find the edit-text and the button
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

        // test: insert input to the edit text and verify that the button is enabled
        inputEditText.setText("123456");
        assertTrue(button.isEnabled());
    }

    @Test
    public void when_userIsEnteringBadInput_then_theButtonShouldBeDisabled() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find the edit-text and the button
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

        // test: insert input to the edit text and verify that the button is disabled
        inputEditText.setText("13hey");
        assertFalse(button.isEnabled());
        inputEditText.setText("17.3");
        assertFalse(button.isEnabled());
    }

    @Test
    public void when_userIsEnteringGoodInput_and_deleteIt_then_theButtonShouldBeEnabledAndThenDisabled() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find the edit-text and the button
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

        // test
        inputEditText.setText("123456");
        assertTrue(button.isEnabled());
        inputEditText.setText("");
        assertFalse(button.isEnabled());
    }

    @Test
    public void when_activityIsLaunching_then_theProgressBarIsGone() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // test: make sure that the progress bar is gone
        assertEquals(View.GONE, mainActivity.findViewById(R.id.progressBar).getVisibility());
    }

    @Test
    public void when_startingCalculation_then_theProgressBarIsDisplayed() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find relevant views
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);

        // test: make sure that the progress bar is displayed
        inputEditText.setText("1234567");
        button.performClick();
        assertEquals(View.VISIBLE, progressBar.getVisibility());
    }


    @Test
    public void when_startingCalculation_then_theButtonIsDisabled() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find relevant views
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

        // test: make sure that the progress bar is displayed
        inputEditText.setText("1234567");
        button.performClick();
        assertFalse(button.isEnabled());
    }

    @Test
    public void when_startCalculationAndGetStoppedCalculationBroadcast_then_theProgressBarIsGone() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find relevant views
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);

        Intent stopBroadcast = new Intent("stopped_calculations");

        // test: make sure that the progress bar is displayed
        inputEditText.setText("1234567");
        button.performClick();
        RuntimeEnvironment.application.sendBroadcast(stopBroadcast);
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertEquals(View.GONE, progressBar.getVisibility());
    }

    @Test
    public void when_startCalculationAndGetStoppedCalculationBroadcast_then_theButtonIsEnabled() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find relevant views
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

        Intent stopBroadcast = new Intent("stopped_calculations");

        // test: make sure that the progress bar is displayed
        inputEditText.setText("1234567");
        button.performClick();
        RuntimeEnvironment.application.sendBroadcast(stopBroadcast);
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertTrue(button.isEnabled());
    }

    @Test
    public void when_userIsInsertGoodInputAndFlipScreen_then_theStateOfTheViewsStaysTheSame() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find relevant views
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);

        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // test: make sure that the progress bar is displayed
        String oldText = "1234567";
        inputEditText.setText(oldText);
        boolean oldEditTextEnabled = inputEditText.isEnabled();
        boolean oldButtonEnabled = button.isEnabled();
        int oldProgressBarVisibility = progressBar.getVisibility();

        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        assertEquals(oldText, inputEditText.getText().toString());
        assertEquals(oldEditTextEnabled, inputEditText.isEnabled());
        assertEquals(oldButtonEnabled, button.isEnabled());
        assertEquals(oldProgressBarVisibility, progressBar.getVisibility());
    }

    @Test
    public void when_userIsInsertBadInputAndFlipScreen_then_theStateOfTheViewsStaysTheSame() {
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        // find relevant views
        EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
        Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);

        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // test: make sure that the progress bar is displayed
        String oldText = "-1";
        inputEditText.setText(oldText);
        boolean oldEditTextEnabled = inputEditText.isEnabled();
        boolean oldButtonEnabled = button.isEnabled();
        int oldProgressBarVisibility = progressBar.getVisibility();

        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        assertEquals(oldText, inputEditText.getText().toString());
        assertEquals(oldEditTextEnabled, inputEditText.isEnabled());
        assertEquals(oldButtonEnabled, button.isEnabled());
        assertEquals(oldProgressBarVisibility, progressBar.getVisibility());
    }
}
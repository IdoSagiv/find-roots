package exercise.find.roots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class CalculateRootsService extends IntentService {
    private static final long timeToCalcMs = TimeUnit.SECONDS.toMillis(20);

    public CalculateRootsService() {
        super("CalculateRootsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;
        long timeStartMs = System.currentTimeMillis();
        long numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0);
        if (numberToCalculateRootsFor <= 0) {
            Log.e("CalculateRootsService", "can't calculate roots for non-positive input" + numberToCalculateRootsFor);
            return;
        }

        // private case for even numbers
        if (numberToCalculateRootsFor % 2 == 0) {
            sendFoundBroadcast(numberToCalculateRootsFor, 2);
            return;
        }

        // search for roots
        for (int i = 3; i < Math.sqrt(numberToCalculateRootsFor) + 1; i += 2) {
            if (numberToCalculateRootsFor % i == 0) {
                sendFoundBroadcast(numberToCalculateRootsFor, i);
            }
            if (System.currentTimeMillis() - timeStartMs > timeToCalcMs) {
                sendStopCalcBroadcast(numberToCalculateRootsFor);
                return;
            }
        }

        // the original number is prime
        sendFoundBroadcast(numberToCalculateRootsFor, numberToCalculateRootsFor);


    /*
    TODO:
     calculate the roots.
     check the time (using `System.currentTimeMillis()`) and stop calculations if can't find an answer after 20 seconds
     upon success (found a root, or found that the input number is prime):
      send broadcast with action "found_roots" and with extras:
       - "original_number"(long)
       - "root1"(long)
       - "root2"(long)
     upon failure (giving up after 20 seconds without an answer):
      send broadcast with action "stopped_calculations" and with extras:
       - "original_number"(long)
       - "time_until_give_up_seconds"(long) the time we tried calculating
      examples:
       for input "33", roots are (3, 11)
       for input "30", roots can be (3, 10) or (2, 15) or other options
       for input "17", roots are (17, 1)
       for input "829851628752296034247307144300617649465159", after 20 seconds give up
     */
    }


    private void sendFoundBroadcast(long origNum, long root1) {
        Log.e("CalculateRootsService", "calculated roots for " + origNum + " successfully");
        Intent intent = new Intent("found_roots");
        intent.putExtra("original_number", origNum);
        intent.putExtra("root1", root1);
        intent.putExtra("root2", origNum / root1);
        sendBroadcast(intent);
    }

    private void sendStopCalcBroadcast(long origNum) {
        long sec = TimeUnit.MILLISECONDS.toSeconds(timeToCalcMs);
        Log.e("CalculateRootsService", "failed to calculate roots for " + origNum + " in " + sec + " seconds");
        Intent intent = new Intent("stopped_calculations");
        intent.putExtra("original_number", origNum);
        intent.putExtra("time_until_give_up_seconds", sec);
        sendBroadcast(intent);
    }

}
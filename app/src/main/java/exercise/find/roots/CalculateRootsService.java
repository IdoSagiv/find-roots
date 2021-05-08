package exercise.find.roots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.text.DecimalFormat;
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
            sendFoundBroadcast(numberToCalculateRootsFor, 2, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - timeStartMs));
            return;
        }

        // search for roots
        for (int i = 3; i < Math.sqrt(numberToCalculateRootsFor) + 1; i += 2) {
            if (numberToCalculateRootsFor % i == 0) {
                sendFoundBroadcast(numberToCalculateRootsFor, i, milliToRoundedSec(System.currentTimeMillis() - timeStartMs));
                return;
            }
            if (System.currentTimeMillis() - timeStartMs > timeToCalcMs) {
                sendStopCalcBroadcast(numberToCalculateRootsFor);
                return;
            }
        }

        // the original number is prime
        sendFoundBroadcast(numberToCalculateRootsFor, 1, milliToRoundedSec(System.currentTimeMillis() - timeStartMs));
    }


    private void sendFoundBroadcast(long origNum, long root1, double calculationTimeSec) {
        Log.e("CalculateRootsService", "calculated roots for " + origNum + " successfully");
        Intent intent = new Intent("found_roots");
        intent.putExtra("original_number", origNum);
        intent.putExtra("root1", root1);
        intent.putExtra("root2", origNum / root1);
        intent.putExtra("calculation_time_seconds", calculationTimeSec);
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

    private double milliToRoundedSec(long millis) {
        return round3decimals(millis / 1000d);
    }

    private double round3decimals(double n) {
        return Math.round(n * 1000) / 1000d;
    }

}
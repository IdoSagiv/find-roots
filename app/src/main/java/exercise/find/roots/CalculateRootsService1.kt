package exercise.find.roots

import android.app.IntentService
import android.content.Intent
import android.util.Log

class CalculateRootsService1 : IntentService("CalculateRootsService1") {
    private val timeToCalcMs = 20 * 1000L
    public override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val timeStartMs = System.currentTimeMillis()
        val numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0)
        if (numberToCalculateRootsFor <= 0) {
            Log.e("CalculateRootsService", "can't calculate roots for non-positive input $numberToCalculateRootsFor")
            return
        }

        if (numberToCalculateRootsFor % 2 == 0L) {
            sendFoundBroadcast(numberToCalculateRootsFor, 2)
            return
        }

        var i = 3L
        while (i <= Math.sqrt(numberToCalculateRootsFor.toDouble() + 1).toLong()) {
            if (numberToCalculateRootsFor % i == 0L) {
                sendFoundBroadcast(numberToCalculateRootsFor, i)
            }
            if (System.currentTimeMillis() - timeStartMs >= timeToCalcMs) {
                val broadcast = Intent("stopped_calculations")
                broadcast.putExtra("original_number", numberToCalculateRootsFor)
                broadcast.putExtra("time_until_give_up_seconds", timeToCalcMs / 1000)
                sendBroadcast(broadcast)
                return
            }
            i += 2
        }

        sendFoundBroadcast(numberToCalculateRootsFor, numberToCalculateRootsFor)
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

    private fun sendFoundBroadcast(origNum: Long, root: Long) {
        val broadcast = Intent("found_roots")
        broadcast.putExtra("original_number", origNum)
        broadcast.putExtra("root1", root)
        broadcast.putExtra("root2", origNum / root)
        sendBroadcast(broadcast)
    }
}
package exercise.find.roots;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;

public class SuccessScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_screen);
        Intent intent = getIntent();
        if (intent == null) return;

        long originalNumber = intent.getLongExtra("original_number", -1);
        long root1 = intent.getLongExtra("root1", -1);
        long root2 = intent.getLongExtra("root2", -1);
        double calculationTimeSec = intent.getDoubleExtra("calculation_time_seconds", -1);

        TextView successMsgTextView = findViewById(R.id.textViewSuccessMessage);
        String message = MessageFormat.format("The original number - {0}\nThe roots - {0}={1}*{2}\nCalculation took - {3} seconds", originalNumber, root1, root2, calculationTimeSec);
        successMsgTextView.setText(message);
    }
}

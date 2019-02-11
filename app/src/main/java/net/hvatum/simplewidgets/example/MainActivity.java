package net.hvatum.simplewidgets.example;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import net.hvatum.simplewidgets.AnalogGauge;
import net.hvatum.simplewidgets.BarGauge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Thread t;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalogGauge analogGauge1 = MainActivity.this.findViewById(R.id.analoggauge1);
        //analogGauge.setDrawValueText(true);
        //analogGauge.setUnit("pulse");
        List<Pair<Float, Integer>> l = new ArrayList<>();
        l.add(new Pair<>(45.0f, Color.RED));
        l.add(new Pair<>(90.0f, Color.GREEN));
        l.add(new Pair<>(140.0f, Color.YELLOW));
        l.add(new Pair<>(250.0f, Color.RED));
        analogGauge1.setBackgroundColors(l);

        AnalogGauge analogGauge2 = MainActivity.this.findViewById(R.id.analoggauge2);

        BarGauge barGauge = MainActivity.this.findViewById(R.id.bargauge);

        handler = new Handler();
        t = new Thread(() -> {
            Random r = new Random();


            while (true) {
                handler.post(() -> {
                    int value = r.nextInt(250);
                    analogGauge1.setValue(value);
                    analogGauge1.setValueColors(255-value, value, 0);
                    analogGauge2.setValue(value);
                    analogGauge2.setValueColors(255-value, 0, value);
                    barGauge.setValue(value);
                    barGauge.setValueColors(value, 255-value, 0);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        t.start();
    }
}

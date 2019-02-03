package net.hvatum.simplewidgets.example;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import net.hvatum.simplewidgets.AnalogGauge;
import net.hvatum.simplewidgets.BarGauge;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Thread t;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalogGauge analogGauge = MainActivity.this.findViewById(R.id.analoggauge);
        analogGauge.setDrawValueText(true);
        analogGauge.setUnit("pulse");

        BarGauge barGauge = MainActivity.this.findViewById(R.id.bargauge);

        handler = new Handler();
        t = new Thread(() -> {
            Random r = new Random();


            while (true) {
                handler.post(() -> {
                    int value = r.nextInt(100);
                    analogGauge.setValue(value);
                    barGauge.setValue(value);
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

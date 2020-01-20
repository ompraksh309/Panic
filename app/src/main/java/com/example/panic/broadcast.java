package com.example.panic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;


public class broadcast extends BroadcastReceiver {
    Context cntx;
    Vibrator vibe;
    long a,seconds_screenoff,seconds_screenon,OLD_TIME, diffrence, actual_diff;
    boolean OFF_SCREEN, ON_SCREEN, sent_msg;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.v("onReceive", "Power button is pressed.");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            a = System.currentTimeMillis();
            seconds_screenoff = a;
            OLD_TIME = seconds_screenoff;
            OFF_SCREEN = true;

            new CountDownTimer(5000, 200) {

                public void onTick(long millisUntilFinished) {


                    if (ON_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {

                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 4000) {
                                sent_msg = true;
                                if (sent_msg) {

                                    Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_LONG).show();
                                    vibe.vibrate(100);
                                    seconds_screenon = 0;
                                    seconds_screenoff = 0;
                                    sent_msg = false;

                                }
                            } else {
                                seconds_screenon = 0;
                                seconds_screenoff = 0;

                            }
                        }
                    }
                }

                public void onFinish() {

                    seconds_screenoff = 0;
                }
            }.start();



        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            a = System.currentTimeMillis();
            seconds_screenon = a;
            OLD_TIME = seconds_screenoff;

            new CountDownTimer(5000, 200) {

                public void onTick(long millisUntilFinished) {
                    if (OFF_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {
                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 4000) {
                                sent_msg = true;
                                if (sent_msg) {

                                    Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_LONG).show();
                                    vibe.vibrate(100);
                                    seconds_screenon = 0;
                                    seconds_screenoff = 0;
                                    sent_msg = false;

                                }
                            } else {
                                seconds_screenon = 0;
                                seconds_screenoff = 0;

                            }
                        }
                    }

                }

                public void onFinish() {

                    seconds_screenon = 0;
                }
            }.start();



        }
    }

    private long cal_diff(long seconds_screenon2, long seconds_screenoff2) {
        if (seconds_screenon2 >= seconds_screenoff2) {
             diffrence = (seconds_screenon2) - (seconds_screenoff2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        } else {
             diffrence = (seconds_screenoff2) - (seconds_screenon2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        }

        return diffrence;
    }


}

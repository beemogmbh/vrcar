package de.codepotion.videotest;

import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegInputStream;
import com.github.niqdev.mjpeg.MjpegView;

import rx.Observer;

public class MainActivity extends AppCompatActivity {

    private String videoPath ="http://admin:camera@192.168.0.188/mjpeg.cgi";
    MjpegView mjpegView;

    private String USERNAME = "admin";
    private String PASSWORD = "camera";
    private String URL = "http://192.168.188.22/mjpeg.cgi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mjpegView = (MjpegView)findViewById(R.id.mjpegViewDefault);

        Mjpeg.newInstance()
                .credential(USERNAME, PASSWORD)
                .open(URL, 5)
                .subscribe(
                        new Observer<MjpegInputStream>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(getClass().getSimpleName(), "mjpeg error", e);
                            }

                            @Override
                            public void onNext(MjpegInputStream mjpegInputStream) {
                                mjpegView.setSource(mjpegInputStream);
                                mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
                                mjpegView.showFps(true);
                            }
                        }
                );
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

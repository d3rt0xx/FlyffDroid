package app.d3rt0xx.flyffdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    WebView mClientWebView, sClientWebView;
    FrameLayout mClient, sClient;
    LinearLayout linearLayout;
    Boolean exit = false, isOpen = false;
    TinyDB tinyDB;

    String rotationlock = "unlocked";

    Menu optionMenu;

    String url = "https://universe.flyff.com/play";
    String news = "https://universe.flyff.com/news";
    String flyffipedia = "https://flyffipedia.com";
    String flyffulator = "https://flyffulator.com";
    String skillulator = "https://skillulator.com";
    String flyffdroid = "https://github.com/d3rt0xx/FlyffDroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullScreenOff();

        linearLayout = findViewById(R.id.linearLayout);

        mClient = findViewById(R.id.frameLayoutMainClient);

        sClient = findViewById(R.id.frameLayoutSecondClient);

        mClientWebView = new WebView(getApplicationContext());

        sClientWebView = new WebView(getApplicationContext());

        mainClient();

        if (savedInstanceState == null) {
            mClientWebView.loadUrl(url);
            sClientWebView.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            fullScreenOff();
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.removeAllViews();
        sClient.removeAllViews();
        mClientWebView.destroy();
        sClientWebView.destroy();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.secondClient:

                if (sClient.getVisibility() == View.GONE && !isOpen) {

                    sClient.setVisibility(View.VISIBLE);

                    secondClient();

                    optionMenu.findItem(R.id.secondClient).setTitle("Close Second Client");

                    optionMenu.findItem(R.id.minimizeSecondClient).setEnabled(true);

                    optionMenu.findItem(R.id.minimizeMainClient).setEnabled(true);

                    optionMenu.findItem(R.id.reloadSecondClient).setEnabled(true);

                    isOpen = true;
                } else {

                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle("Close Second Client?")
                            .setPositiveButton("Yes", null)
                            .setNegativeButton("No", null)
                            .show();

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    positiveButton.setOnClickListener(v -> {

                        sClient.removeAllViews();

                        sClientWebView.loadUrl("about:blank");

                        sClient.setVisibility(View.GONE);

                        optionMenu.findItem(R.id.secondClient).setTitle("Open Second Client");

                        optionMenu.findItem(R.id.minimizeSecondClient).setTitle("Minimize Second Client");

                        optionMenu.findItem(R.id.minimizeMainClient).setTitle("Minimize Main Client");

                        optionMenu.findItem(R.id.minimizeSecondClient).setEnabled(false);

                        optionMenu.findItem(R.id.minimizeMainClient).setEnabled(false);

                        optionMenu.findItem(R.id.reloadSecondClient).setEnabled(false);

                        if (mClient.getVisibility() == View.GONE) {

                            mClient.setVisibility(View.VISIBLE);
                        }

                        isOpen = false;

                        dialog.dismiss();
                    });
                }

                break;

            case R.id.minimizeSecondClient:

                if (sClient.getVisibility() == View.VISIBLE) {

                    sClient.setVisibility(View.GONE);

                    optionMenu.findItem(R.id.minimizeSecondClient).setTitle("Maximize Second Client");

                    optionMenu.findItem(R.id.minimizeMainClient).setEnabled(false);
                } else {

                    sClient.setVisibility(View.VISIBLE);

                    optionMenu.findItem(R.id.minimizeSecondClient).setTitle("Minimize Second Client");

                    optionMenu.findItem(R.id.minimizeMainClient).setEnabled(true);
                }

                break;

            case R.id.minimizeMainClient:

                if (mClient.getVisibility() == View.VISIBLE) {

                    mClient.setVisibility(View.GONE);

                    optionMenu.findItem(R.id.minimizeMainClient).setTitle("Maximize Main Client");

                    optionMenu.findItem(R.id.minimizeSecondClient).setEnabled(false);
                } else {

                    mClient.setVisibility(View.VISIBLE);

                    optionMenu.findItem(R.id.minimizeMainClient).setTitle("Minimize Main Client");

                    optionMenu.findItem(R.id.minimizeSecondClient).setEnabled(true);
                }

                break;

            case R.id.reloadMainClient:

                mClientWebView.loadUrl(url);

                break;

            case R.id.reloadSecondClient:

                sClientWebView.loadUrl(url);

                break;

            case R.id.fullScreen:

                fullScreenOn();

                break;

            case R.id.rotation:

                lockUnlockRotation();

                break;

            case R.id.news:

                mClientWebView.loadUrl(news);

                break;

            case R.id.flyffipedia:

                mClientWebView.loadUrl(flyffipedia);

                break;

            case R.id.flyffulator:

                mClientWebView.loadUrl(flyffulator);

                break;

            case R.id.skillulator:

                mClientWebView.loadUrl(skillulator);

                break;

            case R.id.flyffdroid:

                mClientWebView.loadUrl(flyffdroid);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        optionMenu = menu;

        loadRotationConfig();

        return super.onCreateOptionsMenu(menu);
    }

    private void fullScreenOn() {

        View decorView = getWindow().getDecorView();

        Objects.requireNonNull(getSupportActionBar()).hide();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void fullScreenOff() {

        View decorView = getWindow().getDecorView();

        Objects.requireNonNull(getSupportActionBar()).show();

        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void mainClient() {

        createWebViewer(mClientWebView, mClient);
    }

    private void secondClient() {

        createWebViewer(sClientWebView, sClient);
    }

    private void lockUnlockRotation() {

        if (rotationlock.equals("unlocked")) {

            rotationlock = "locked";

            Toast.makeText(this, "Locked Auto-Rotation", Toast.LENGTH_SHORT).show();

            optionMenu.findItem(R.id.rotation).setTitle("Unlock Auto-Rotation");

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        } else if (rotationlock.equals("locked")) {

            rotationlock = "unlocked";

            Toast.makeText(this, "Unlocked Auto-Rotation", Toast.LENGTH_SHORT).show();

            optionMenu.findItem(R.id.rotation).setTitle("Lock Auto-Rotation");

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }

        tinyDB.remove("rotation");
        tinyDB.putString("rotation", rotationlock);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void loadRotationConfig() {

        if (!tinyDB.getString("orientation").equals("")) {

            if (tinyDB.getString("orientation").equals("landscape")) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            } else if (tinyDB.getString("orientation").equals("portrait")) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }

        if (!tinyDB.getString("rotation").equals("")) {

            rotationlock = tinyDB.getString("rotation");

            if (rotationlock.equals("locked")) {

                optionMenu.findItem(R.id.rotation).setTitle("Unlock Auto-Rotation");

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

            } else if (rotationlock.equals("unlocked")) {

                optionMenu.findItem(R.id.rotation).setTitle("Lock Auto-Rotation");

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            }
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void createWebViewer(WebView webView, FrameLayout frameLayout) {

        webView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        webView.requestFocus(View.FOCUS_DOWN);
        webView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!v.hasFocus()) {
                        v.requestFocus();
                    }
                    break;
            }
            return false;
        });

        frameLayout.addView(webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUserAgentString("FlyffDroid");

        webView.loadUrl(url);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mClientWebView.saveState(outState);
        sClientWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mClientWebView.restoreState(savedInstanceState);
        sClientWebView.saveState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        tinyDB.remove("orientation");

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            tinyDB.putString("orientation", "landscape");

            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            tinyDB.putString("orientation", "portrait");

            linearLayout.setOrientation(LinearLayout.VERTICAL);
        }

    }
}

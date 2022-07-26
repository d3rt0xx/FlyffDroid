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

import app.d3rt0xx.flyffdroid.constants.Websites;

public class MainActivity extends AppCompatActivity {

    private WebView mClientWebView, sClientWebView;
    private FrameLayout mClient, sClient;
    private LinearLayout linearLayout;
    private Boolean exit = false, isOpen = false;
    private TinyDB tinyDB;

    String rotationlock = "unlocked";

    Menu optionMenu;

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
            String gameStage = Websites.GAME.getUrl();
            mClientWebView.loadUrl(gameStage);
            sClientWebView.loadUrl(gameStage);
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
                toggleSecondClient();
                break;
            case R.id.minimizeSecondClient:
                minimizeWindow(sClient, R.id.minimizeSecondClient, getResources().getString(R.string.maximize_second_client), R.id.minimizeMainClient, getResources().getString(R.string.minimize_second_client));
                break;
            case R.id.minimizeMainClient:
                minimizeWindow(mClient, R.id.minimizeMainClient, getResources().getString(R.string.maximize_main_client), R.id.minimizeSecondClient, getResources().getString(R.string.minimize_main_client));
                break;
            case R.id.reloadMainClient:
                mClientWebView.loadUrl(Websites.GAME.getUrl());
                break;
            case R.id.reloadSecondClient:
                sClientWebView.loadUrl(Websites.GAME.getUrl());
                break;
            case R.id.fullScreen:
                fullScreenOn();
                break;
            case R.id.rotation:
                lockUnlockRotation();
                break;
            case R.id.news:
                loadSecondClientWithUrl(Websites.NEWS);
                break;
            case R.id.marketplace:
                loadSecondClientWithUrl(Websites.MARKETPLACE);
                break;
            case R.id.flyffipedia:
                loadSecondClientWithUrl(Websites.FLYFFIPEDIA);
                break;
            case R.id.flyffulator:
                loadSecondClientWithUrl(Websites.FLYFFULATOR);
                break;
            case R.id.skillulator:
                loadSecondClientWithUrl(Websites.SKILLULATOR);
                break;
            case R.id.madrigalmaps:
                loadSecondClientWithUrl(Websites.MADRIGALMAPS);
                break;
            case R.id.flyffdroid:
                loadSecondClientWithUrl(Websites.FLYFFDROID);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleSecondClient() {
        if (sClient.getVisibility() == View.GONE && !isOpen) {
            changeSecondClientToVisible();
        } else {
            closeSecondClient();
        }
    }

    private void minimizeWindow(FrameLayout mClient, int minimizeItemId, String toggle1Text, int maximizeItemId, String toggle2Text) {
        if (mClient.getVisibility() == View.VISIBLE) {
            changeClientWindowVisibility(mClient, View.GONE, minimizeItemId, toggle1Text, maximizeItemId, false);
        } else {
            changeClientWindowVisibility(mClient, View.VISIBLE, minimizeItemId, toggle2Text, maximizeItemId, true);
        }

    }

    private void loadSecondClientWithUrl(Websites flyffipedia) {
        toggleSecondClient();
        sClientWebView.loadUrl(flyffipedia.getUrl());
    }

    private void closeSecondClient() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getResources().getString(R.string.close_second_client_question))
                .setPositiveButton(getResources().getString(R.string.yes), null)
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> {

            sClient.removeAllViews();

            sClientWebView.loadUrl("about:blank");

            sClient.setVisibility(View.GONE);

            optionMenu.findItem(R.id.secondClient).setTitle(getResources().getString(R.string.open_second_client));

            optionMenu.findItem(R.id.minimizeSecondClient).setTitle(getResources().getString(R.string.minimize_second_client));

            optionMenu.findItem(R.id.minimizeMainClient).setTitle(getResources().getString(R.string.minimize_main_client));

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

    private void changeSecondClientToVisible() {
        sClient.setVisibility(View.VISIBLE);

        secondClient();

        optionMenu.findItem(R.id.secondClient).setTitle(getResources().getString(R.string.close_second_client));

        optionMenu.findItem(R.id.minimizeSecondClient).setEnabled(true);

        optionMenu.findItem(R.id.minimizeMainClient).setEnabled(true);

        optionMenu.findItem(R.id.reloadSecondClient).setEnabled(true);

        isOpen = true;
    }

    private void changeClientWindowVisibility(FrameLayout frame, int visibilty, int titleItem, String titleText, int elementId, boolean isEnabled) {
        frame.setVisibility(visibilty);

        optionMenu.findItem(titleItem).setTitle(titleText);

        optionMenu.findItem(elementId).setEnabled(isEnabled);
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
        String unlocked = "unlocked";
        String locked = "locked";

        if (rotationlock.equals(unlocked)) {
            changeLock(locked, R.string.locked_auto_rotation, R.string.unlock_auto_rotation, ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        } else if (rotationlock.equals(locked)) {

            changeLock(unlocked, R.string.unlocked_auto_rotation, R.string.lock_auto_rotation, ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }

        String rotation = "rotation";
        tinyDB.remove(rotation);
        tinyDB.putString(rotation, rotationlock);
    }

    private void changeLock(String locked, int actionText, int newText, int screenOrientationLocked) {
        rotationlock = locked;

        Toast.makeText(this, getResources().getString(actionText), Toast.LENGTH_SHORT).show();

        optionMenu.findItem(R.id.rotation).setTitle(getResources().getString(newText));

        setRequestedOrientation(screenOrientationLocked);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void loadRotationConfig() {

        String orientation = "orientation";
        if (!tinyDB.getString(orientation).equals("")) {

            if (tinyDB.getString(orientation).equals("landscape")) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            } else if (tinyDB.getString(orientation).equals("portrait")) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }

        String rotation = "rotation";
        if (!tinyDB.getString(rotation).equals("")) {

            rotationlock = tinyDB.getString(rotation);

            if (rotationlock.equals("locked")) {

                optionMenu.findItem(R.id.rotation).setTitle(getResources().getString(R.string.unlock_auto_rotation));

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

            } else if (rotationlock.equals("unlocked")) {

                optionMenu.findItem(R.id.rotation).setTitle(getResources().getString(R.string.lock_auto_rotation));

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
        webSettings.setUserAgentString(getResources().getString(R.string.app_name));

        webView.loadUrl(Websites.GAME.getUrl());
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

        String orientation = "orientation";
        tinyDB.remove(orientation);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            tinyDB.putString(orientation, "landscape");

            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            tinyDB.putString(orientation, "portrait");

            linearLayout.setOrientation(LinearLayout.VERTICAL);
        }

    }
}

package app.d3rt0xx.flyffdroid;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Menu optionMenu;
    private WebView mClientWebView, tClientWebView;
    private FrameLayout mClient, tClient;
    private FloatingActionButton switcher;
    private Boolean exit = false, isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_clean);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        fullScreenOff();

        findViewById(R.id.linearLayout);

        mClient = findViewById(R.id.frameLayoutMainClient);

        tClient = findViewById(R.id.frameLayoutTwinkClient);

        mClientWebView = new WebView(getApplicationContext());

        tClientWebView = new WebView(getApplicationContext());

        switcher = findViewById(R.id.switcher);

        switcher.setOnClickListener(view -> {

            if (tClient.getVisibility() == View.GONE) {

                tClient.setVisibility(View.VISIBLE);

                optionMenu.findItem(R.id.minimizeTwinkClient).setTitle(getResources().getString(R.string.minimize_twink_client));

                optionMenu.findItem(R.id.minimizeTwinkClient).setEnabled(true);

                optionMenu.findItem(R.id.minimizeMainClient).setTitle(getResources().getString(R.string.minimize_main_client));

                optionMenu.findItem(R.id.minimizeMainClient).setEnabled(true);
            } else {

                tClient.setVisibility(View.GONE);

                optionMenu.findItem(R.id.minimizeTwinkClient).setTitle(getResources().getString(R.string.maximize_twink_client));

                optionMenu.findItem(R.id.minimizeTwinkClient).setEnabled(true);

                optionMenu.findItem(R.id.minimizeMainClient).setTitle(getResources().getString(R.string.minimize_main_client));

                optionMenu.findItem(R.id.minimizeMainClient).setEnabled(false);

                mClient.setVisibility(View.VISIBLE);
            }
        });

        mainClient();

        if (savedInstanceState == null) {
            String gameStage = Websites.GAME.getUrl();
            mClientWebView.loadUrl(gameStage);
            tClientWebView.loadUrl(gameStage);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, this.getString(R.string.press_back_again_to_exit),
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
        tClient.removeAllViews();
        mClientWebView.destroy();
        tClientWebView.destroy();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.twinkClient) {
            toggleTwinkClient();
        } else if (itemId == R.id.minimizeTwinkClient) {
            minimizeWindow(tClient, R.id.minimizeTwinkClient, getResources().getString(R.string.maximize_twink_client), R.id.minimizeMainClient, getResources().getString(R.string.minimize_twink_client));
        } else if (itemId == R.id.minimizeMainClient) {
            minimizeWindow(mClient, R.id.minimizeMainClient, getResources().getString(R.string.maximize_main_client), R.id.minimizeTwinkClient, getResources().getString(R.string.minimize_main_client));
        } else if (itemId == R.id.reloadMainClient) {
            mClientWebView.loadUrl(Websites.GAME.getUrl());
        } else if (itemId == R.id.reloadTwinkClient) {
            tClientWebView.loadUrl(Websites.GAME.getUrl());
        } else if (itemId == R.id.fullScreen) {
            fullScreenOn();
            Toast.makeText(this, this.getString(R.string.press_back_once_to_exit_fullscreen),
                    Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.discord) {
            loadTwinkClientWithUrl(Websites.DISCORD);
        } else if (itemId == R.id.exptable) {
            loadTwinkClientWithUrl(Websites.EXPTABLE);
        } else if (itemId == R.id.facebook) {
            loadTwinkClientWithUrl(Websites.FACEBOOK);
        } else if (itemId == R.id.flyffipedia) {
            loadTwinkClientWithUrl(Websites.FLYFFIPEDIA);
        } else if (itemId == R.id.flyffulator) {
            loadTwinkClientWithUrl(Websites.FLYFFULATOR);
        } else if (itemId == R.id.guildulator) {
            loadTwinkClientWithUrl(Websites.GUILDULATOR);
        } else if (itemId == R.id.madrigalmaps) {
            loadTwinkClientWithUrl(Websites.MADRIGALMAPS);
        } else if (itemId == R.id.modelviewer) {
            loadTwinkClientWithUrl(Websites.MODELVIEWER);
        } else if (itemId == R.id.patchnotes) {
            loadTwinkClientWithUrl(Websites.PATCHNOTES);
        } else if (itemId == R.id.support) {
            loadTwinkClientWithUrl(Websites.SUPPORT);
        } else if (itemId == R.id.update) {
            loadTwinkClientWithUrl(Websites.UPDATE);
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTwinkClient() {
        if (tClient.getVisibility() == View.GONE && !isOpen) {
            changeTwinkClientToVisible();
        } else {
            closeTwinkClient();
        }
    }

    private void minimizeWindow(FrameLayout mClient, int minimizeItemId, String toggle1Text, int maximizeItemId, String toggle2Text) {
        if (mClient.getVisibility() == View.VISIBLE) {
            changeClientWindowVisibility(mClient, View.GONE, minimizeItemId, toggle1Text, maximizeItemId, false);
        } else {
            changeClientWindowVisibility(mClient, View.VISIBLE, minimizeItemId, toggle2Text, maximizeItemId, true);
        }

    }

    private void loadTwinkClientWithUrl(Websites flyffdroid) {
        toggleTwinkClient();
        tClientWebView.loadUrl(flyffdroid.getUrl());
    }

    private void closeTwinkClient() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getResources().getString(R.string.close_twink_client_question))
                .setPositiveButton(getResources().getString(R.string.yes), null)
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> {

            tClient.removeAllViews();

            tClientWebView.loadUrl("about:blank");

            tClient.setVisibility(View.GONE);

            optionMenu.findItem(R.id.twinkClient).setTitle(getResources().getString(R.string.open_twink_client));

            optionMenu.findItem(R.id.minimizeTwinkClient).setTitle(getResources().getString(R.string.minimize_twink_client));

            optionMenu.findItem(R.id.minimizeMainClient).setTitle(getResources().getString(R.string.minimize_main_client));

            optionMenu.findItem(R.id.minimizeTwinkClient).setEnabled(false);

            optionMenu.findItem(R.id.minimizeMainClient).setEnabled(false);

            optionMenu.findItem(R.id.reloadTwinkClient).setEnabled(false);

            if (mClient.getVisibility() == View.GONE) {

                mClient.setVisibility(View.VISIBLE);
            }

            switcher.setVisibility(View.GONE);

            isOpen = false;

            dialog.dismiss();
        });
    }

    private void changeTwinkClientToVisible() {
        tClient.setVisibility(View.VISIBLE);

        twinkClient();

        optionMenu.findItem(R.id.twinkClient).setTitle(getResources().getString(R.string.close_twink_client));

        optionMenu.findItem(R.id.minimizeTwinkClient).setEnabled(true);

        optionMenu.findItem(R.id.minimizeMainClient).setEnabled(true);

        optionMenu.findItem(R.id.reloadTwinkClient).setEnabled(true);

        switcher.setVisibility(View.VISIBLE);

        isOpen = true;
    }

    private void changeClientWindowVisibility(FrameLayout frame, int visibility, int titleItem, String titleText, int elementId, boolean isEnabled) {
        frame.setVisibility(visibility);

        optionMenu.findItem(titleItem).setTitle(titleText);

        optionMenu.findItem(elementId).setEnabled(isEnabled);
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        optionMenu = menu;

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

    private void twinkClient() {

        createWebViewer(tClientWebView, tClient);
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility", "SdCardPath"})
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
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setUserAgentString(getResources().getString(R.string.app_name));

        webView.loadUrl(Websites.GAME.getUrl());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mClientWebView.saveState(outState);
        tClientWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mClientWebView.restoreState(savedInstanceState);
        tClientWebView.saveState(savedInstanceState);
    }

}

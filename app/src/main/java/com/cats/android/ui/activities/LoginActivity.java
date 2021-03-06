package com.cats.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cats.android.R;
import com.cats.android.util.WebManager;
import com.cats.android.util.Constants;
import com.google.firebase.crash.FirebaseCrash;

import static com.cats.android.util.Constants.CODE;

/**
 * Created by andrey on 16.02.17.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    WebManager.authorize(v.getContext());
                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
        FirebaseCrash.logcat(Log.INFO, TAG, "Activity created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (WebManager.getAccessToken() == null || WebManager.getAccessToken().isExpired()) {
            if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
                String code = uri.getQueryParameter(CODE);
                if (code != null) {
                    WebManager.getAccessToken(this, new ResultReceiver(new Handler()) {
                        @Override
                        public void onReceiveResult(int resultCode, Bundle resultData) {
                            if (resultCode == RESULT_OK) {
                                goToItemListActivity();
                            }
                        }
                    }, code);
                } else if (uri.getQueryParameter("error") != null) {
                    FirebaseCrash.logcat(Log.ERROR, TAG, uri.getQueryParameter("error"));
                }
            }
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void goToItemListActivity() {
        sendRegistrationToServer(WebManager.getFirebaseToken());
        Intent intent = new Intent(this, ItemListActivity.class);

        startActivity(intent);
        finish();
    }

    private void sendRegistrationToServer(String token) {
        WebManager.sendToken(this, new ResultReceiver(new Handler()) {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Refreshed token were poisoned on the server");
                }
            }
        }, token);
    }
}

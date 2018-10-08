package com.lpalakurthy.installreferrerexample;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

public class MainActivity extends AppCompatActivity {

    static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.editText);
        getReferralDetails();
    }

    private void getReferralDetails() {
        final InstallReferrerClient mReferrerClient;
        mReferrerClient = InstallReferrerClient.newBuilder(this).build();
        mReferrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = mReferrerClient.getInstallReferrer();
                            response.getInstallReferrer();
                            response.getReferrerClickTimestampSeconds();
                            response.getInstallBeginTimestampSeconds();

                            updateTextView(response.getInstallReferrer(),
                                    response.getReferrerClickTimestampSeconds(),
                                    response.getInstallBeginTimestampSeconds());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection could not be established
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void updateTextView(String installReferrer,
                                long referrerClickTimestampSeconds,
                                long installBeginTimestampSeconds) {
        String s = installReferrer + "\n" + referrerClickTimestampSeconds + "\n" + installBeginTimestampSeconds;
        textView.setText(s);
    }

    public static void updateTextView2(String installReferrer) {
        textView.setText(installReferrer);
    }
}

package com.avayahackathon.envisage.Communication;

import android.content.SharedPreferences;
import android.util.Log;

import com.avaya.clientservices.credentials.Challenge;
import com.avaya.clientservices.credentials.CredentialCompletionHandler;
import com.avaya.clientservices.credentials.CredentialProvider;
import com.avaya.clientservices.credentials.UserCredential;

/**
 * Created by yogita on 6/5/17.
 */

public class MessagingCredentialProvider implements CredentialProvider {

    private static final String LOG_TAG = MessagingCredentialProvider.class.getSimpleName();
    private final SharedPreferences settings;

    public MessagingCredentialProvider(SharedPreferences settings) {
        this.settings = settings;
    }
    @Override
    public void onAuthenticationChallenge(Challenge challenge, CredentialCompletionHandler credentialCompletionHandler) {
        Log.d(LOG_TAG, "onAuthenticationChallenge : Challenge = "
                + challenge);

        // Getting login information from settings
        String extension = settings.getString(SDKManager.EXTENSION, "");
        // Note: Although this sample application manages passwords as clear text this application
        // is intended as a learning tool to help users become familiar with the Avaya SDK.
        // Managing passwords as clear text is not illustrative of a secure process to protect
        //String
        // passwords in an enterprise quality application.
        String password = settings.getString(SDKManager.PASSWORD, "Hack#2017");
        String domain = settings.getString(SDKManager.DOMAIN, "svucacloud");
        String userName ="hackathon" + extension + "@" + domain;
        UserCredential userCredential = null;
        if (challenge.getFailureCount() <= 1) {
            userCredential = new UserCredential(userName, password, domain);
        }
        credentialCompletionHandler.onCredentialProvided(userCredential);
    }

    @Override
    public void onCredentialAccepted(Challenge challenge) {

    }

    @Override
    public void onAuthenticationChallengeCancelled(Challenge challenge) {

    }
}

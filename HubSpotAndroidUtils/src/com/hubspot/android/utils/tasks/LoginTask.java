package com.hubspot.android.utils.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.activities.LoginActivity;
import com.hubspot.android.utils.http.HttpUtils;
import com.hubspot.android.utils.hubapi.ApiKeyHelper;
import com.hubspot.android.utils.hubapi.ApiKeyHelperException;

public class LoginTask extends AsyncTask<CharSequence, Void, CharSequence> {
    protected static final String LOG_TAG = "hs.loginTask";

    private LoginActivity applicationContext;

    private CharSequence username;

    private CharSequence errorMessage;

    public LoginTask(final LoginActivity applicationContext) {
        this.applicationContext = applicationContext;
        errorMessage = null;
    }

    @Override
    protected CharSequence doInBackground(CharSequence... params) {
        username = params[0];
        CharSequence password = params[1];
        Long portalId = null;
        if (!Utils.isEmpty(params[2])) {
            portalId = Long.parseLong(params[2].toString());
        }

        final ApiKeyHelper helper = new ApiKeyHelper(new HttpUtils());
        try {
            return helper.requestApiKey(username, password, portalId);
        } catch (ApiKeyHelperException ex) {
            errorMessage = "Error logging in: " + ex.getLocalizedMessage();
            Log.e(LOG_TAG, errorMessage.toString(), ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(CharSequence result) {
        if (!Utils.isEmpty(errorMessage)) {
            final Toast theToast = Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT);
            theToast.show();
            applicationContext.getLoginProgress().dismiss();
        } else {
            applicationContext.saveApiKey(result, username);
        }
    }
}

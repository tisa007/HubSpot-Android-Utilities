package com.hubspot.android.utils.activities;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.hubspot.android.utils.R;
import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.tasks.LoginTask;

public class LoginActivity extends DefaultActivity {
    /**
     * Name of the API key in the returned intent's extras  
     */
    public static final String API_KEY_EXTRA_NAME = "apiKey";
    
    /**
     * Name of the logged in user in the returned intent's extras
     */
    public static final String USERNAME_EXTRA_NAME = "username";

    //Activity Result/Request Constants
    public static final int RESULT_LOGIN_SUCCESS = -1;
    public static final int RESULT_LOGIN_FAIL = 2;
    public static final int REQUEST_AUTHENTICATE_USER = 1;
    
    /** The portal ID to log in to (advanced) */
    private Long portalId;

    private ProgressDialog loginProgress;
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.login);
        findViewById(R.id.signInButton).setOnClickListener(new SignInClickListener(this));
        updatePasswordVisibility();

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final boolean result = super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.loginmenu, menu);
        
        return result;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String passwordToggleText;
        if (showingPassword) {
            passwordToggleText = getString(R.string.hidePassword);
        } else {
            passwordToggleText = getString(R.string.showPassword);
        }
        menu.findItem(R.id.showPassword).setTitle(passwordToggleText);
        
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case R.id.apiKey:
            requestApiKey();
            return true;
        case R.id.portalId:
            requestPortalId();
            return true;
        case R.id.showPassword:
            toggleShowPassword();
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean showingPassword = false;

    private void toggleShowPassword() {
        showingPassword = !showingPassword;
        updatePasswordVisibility();
    }

    private void updatePasswordVisibility() {
        TransformationMethod method;
        if (showingPassword) {
            method = new SingleLineTransformationMethod();
        } else {
            method = new PasswordTransformationMethod();
        }
        ((EditText)findViewById(R.id.password)).setTransformationMethod(method);
    }

    public void saveApiKey(final CharSequence apiKey, final CharSequence username) {
        Intent intent = new Intent();
        intent.putExtra(API_KEY_EXTRA_NAME, apiKey);
        if (!Utils.isEmpty(username)) {
            intent.putExtra(USERNAME_EXTRA_NAME, username);
        }
        setResult(RESULT_OK, intent);
        if (loginProgress != null) {
            loginProgress.dismiss();
        }
        finish();
    }

    /** Show a simple input dialog */
    private void modalInput(final CharSequence title, final CharSequence message, final CharSequence hint,
            final CharSequence okButtonText, final AlertOkClickListener clickListener) {
        final Builder alert = new Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
     
        final EditText input = new EditText(this);
        input.setSingleLine();
        if (!Utils.isEmpty(hint)) {
            input.setHint(hint);
        }
        alert.setView(input);
        clickListener.setInput(input);
        
        alert.setPositiveButton(okButtonText, clickListener);
        alert.show();
    }
    
    /** Show a short toast */
    private void toast(final CharSequence message) {
        final Toast theToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        theToast.show();
    }

    /** Let the user input a portal ID (in addition to their username and password) */
    private void requestPortalId() {
        final AlertOkClickListener listener = new AlertOkClickListener() {
            @Override
            protected void onRespond(final CharSequence response) {
                if (Utils.isEmpty(response)) {
                    return;
                }
                long value = -1;
                try {
                    value = Long.parseLong(response.toString().trim());
                } catch (final NumberFormatException numberFormatException) {
                    toast("Portal IDs must be numbers");
                    return;
                }
                if (value < 0) {
                    toast("Portal IDs must be positive numbers");
                    return;
                }
                
                portalId = value;
                toast("Portal ID saved");
            }
        };
        
        modalInput("Choose a Portal", "If you need to use a specific portal, input its ID", "Portal ID", "Set", listener);
    }

    /** Let the user input an API key, rather than logging in */
    private void requestApiKey() {
        final AlertOkClickListener listener = new AlertOkClickListener() {
            @Override
            protected void onRespond(final CharSequence response) {
                if (Utils.isEmpty(response)) {
                    return;
                }
                
                toast("API key saved");
                saveApiKey(response, null);
            }
        };
        
        modalInput("Set API Key", "You can input a HubSpot API key directly if you'd rather not log in", "API Key", "Set", listener);
    }

    /** Sign in to HubSpot and get the user's API key */
    public void signInClick() {
        final CharSequence username = ((EditText)findViewById(R.id.username)).getText();
        final CharSequence password = ((EditText)findViewById(R.id.password)).getText();

        loginProgress = ProgressDialog.show(LoginActivity.this, "Logging In...", "Logging in to your HubSpot account.");
        CharSequence portalIdSeq = "";
        if (portalId != null) {
            portalIdSeq = portalId.toString();
        }
        new LoginTask(LoginActivity.this).execute(username, password, portalIdSeq);
    }

    public ProgressDialog getLoginProgress() {
        return loginProgress;
    }
}

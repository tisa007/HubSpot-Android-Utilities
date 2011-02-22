package com.hubspot.android.utils.activities;

import android.view.View;

public class SignInClickListener implements View.OnClickListener {
    private final LoginActivity parent;

    public SignInClickListener(final LoginActivity parent) {
        this.parent = parent;
    }

    @Override
    public void onClick(final View self) {
        parent.signInClick();
    }

}

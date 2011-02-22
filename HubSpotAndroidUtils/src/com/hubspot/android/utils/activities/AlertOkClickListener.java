package com.hubspot.android.utils.activities;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

/**
 * Abstract listener to be used with modalInput. Calls onRespond() with the 
 *
 */
public abstract class AlertOkClickListener implements OnClickListener {
    /** On click, passes `input.getText()` to the onRespond() method */
    private EditText input;
    
    public void setInput(final EditText input) {
        this.input = input;
    }
    
    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        onRespond(input.getText());
    }

    protected abstract void onRespond(final CharSequence response);
}

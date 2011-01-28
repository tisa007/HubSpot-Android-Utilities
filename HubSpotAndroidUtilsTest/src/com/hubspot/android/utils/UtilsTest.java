package com.hubspot.android.utils;

import java.util.Collection;
import java.util.Collections;

import android.test.AndroidTestCase;

public class UtilsTest extends AndroidTestCase {

    public void testStringIsEmpty() {
        assertTrue(Utils.isEmpty((String)null));
        assertTrue(Utils.isEmpty(""));
        assertFalse(Utils.isEmpty("asdf"));
        assertFalse(Utils.isEmpty(" "));
    }

    public void testCollectionIsEmpty() {
        assertTrue(Utils.isEmpty((Collection)null));
        assertTrue(Utils.isEmpty(Collections.EMPTY_LIST));
        assertFalse(Utils.isEmpty(Collections.singleton("")));
    }
    
}

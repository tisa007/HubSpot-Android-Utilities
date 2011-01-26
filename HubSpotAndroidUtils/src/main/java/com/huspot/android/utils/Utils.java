package com.hubspot.android.utils;

import java.util.Collection;

import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;

public class Utils {
	public static final String EMPTY_STRING = "";

	/**
	 * Is the given collection null or empty?
	 */
    public static boolean isEmpty(final Collection<?> collection) {
		if (collection == null) {
			return true;
		}

		return (collection.size() == 0);
	}

    /**
     * Is the given CharSequence null or empty?
     */
    public static boolean isEmpty(final CharSequence string) {
        if (string == null) {
            return true;
        }

        return (string.length() == 0);
    }

	/**
	 * Get a string value from a cursor
	 */
    public static String getString(final Cursor cursor, final String columnName) {
		int columnIndex = cursor.getColumnIndexOrThrow(columnName);

		return cursor.getString(columnIndex);
	}

	/**
	 * Get a long value from a cursor
	 */
    public static long getLong(final Cursor cursor, final String columnName) {
		int columnIndex = cursor.getColumnIndexOrThrow(columnName);

		return cursor.getLong(columnIndex);
	}

	/**
	 * Get a long value from a cursor
	 */
    public static int getInt(final Cursor cursor, final String columnName) {
		int columnIndex = cursor.getColumnIndexOrThrow(columnName);

		return cursor.getInt(columnIndex);
	}

    public static String formatPhoneNumber(final String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        String number = phoneNumber.trim();
        if (number.length() == 10) {
            return String.format("%s-%s-%s", number.substring(0, 3), number.substring(3, 6), number.substring(6));
        } else if (number.length() == 7) {
            return String.format("%s-%s", number.substring(0, 3), number.substring(3));
        }

        return number;
    }

    public static String formatTwitterHandle(final String twitterHandle) {
        if (twitterHandle == null) {
            return null;
        }

        String handle = twitterHandle.trim();
        if (handle.startsWith("@")) {
            return handle;
        } else {
            return "@" + handle;
        }
    }

    public static CharSequence unixTimeToRecency(long time) {
        if (time == 0) {
            return Utils.EMPTY_STRING;
        }

        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    public static Uri getTelephoneUri(String number) {
        if (Utils.isEmpty(number)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (char character : number.toCharArray()) {
            switch (character) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                sb.append(character);
                break;
            default:
                break;
            }
        }

        return Uri.fromParts("tel", sb.toString(), null);
    }
}

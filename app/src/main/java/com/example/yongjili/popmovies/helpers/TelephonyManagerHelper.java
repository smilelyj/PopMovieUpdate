package org.swe.android.helpers;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by caseylee on 7/26/15.
 */
public class TelephonyManagerHelper {
    public static String getDeviceId(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephony.getDeviceId();
    }
}

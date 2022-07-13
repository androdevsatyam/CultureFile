package com.devsatya.culturefile;

import static com.devsatya.culturefile.AccessHelper.WORKINGPKG;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


public class FilingService extends AccessibilityService {
    public static boolean start = false;
    public static String TAG = "onAccessibilityEvent";
    String pkg;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (start) {
//            AccessibilityNodeInfo rootInActiveWindow;
            if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
                pkg = event.getPackageName().toString();
                Log.d(TAG, "Open_Package: " + pkg);
                if (pkg.equalsIgnoreCase(WORKINGPKG)) {
                    AccessibilityNodeInfo rootInActiveWindow2 = getRootInActiveWindow();
                    rootInActiveWindow2.getChildCount();
                    Log.d(TAG, "onAccessibilityEvent: " + rootInActiveWindow2.getChildCount());


                    if (rootInActiveWindow2.getChild(0).getChild(0).getText().toString().equalsIgnoreCase(" Welcome VLE")) {
                        rootInActiveWindow2.getChild(0).getChild(3).setText(AccessHelper.dataModels.get(1).getCol1());
                        rootInActiveWindow2.getChild(0).getChild(6).setText(AccessHelper.dataModels.get(1).getCol2() );
                    }
                }
            }
        }
/*
* rootInActiveWindow2.getChild(0).getChild(3
).getText()
username

rootInActiveWindow2.getChild(0).getChild(6
).getText()
password


rootInActiveWindow2.getChild(3).getChild(0)
        button

*/

    }

    @Override
    public void onInterrupt() {

    }
}

package co.yap.yapcore.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class PermissionsManager implements LifecycleObserver {

    private static int PERMISSION_REQUEST = 13;
    private static String ALL_PERMISSIONS = "ALL";
    private static final String[] APP_PERMISSIONS = {
            // Manifest.permission.CAMERA,
            // Manifest.permission.WRITE_EXTERNAL_STORAGE,
            // Manifest.permission.RECORD_AUDIO,
            // Manifest.permission.ACCESS_FINE_LOCATION
    };

    private Activity activity;
    private String requestedPermission = ALL_PERMISSIONS;
    private OnPermissionGrantedListener onPermissionGrantedListener = OnPermissionGrantedListener.DEFAULT;


    public interface OnPermissionGrantedListener {
        void onPermissionGranted(String permission);

        void onPermissionNotGranted(String permission);

        OnPermissionGrantedListener DEFAULT = new OnPermissionGrantedListener() {
            @Override
            public void onPermissionGranted(String permission) {

            }

            @Override
            public void onPermissionNotGranted(String permission) {

            }
        };
    }

    public PermissionsManager(Activity activity, OnPermissionGrantedListener listener) {
        this.activity = activity;
        setOnPermissionGrantedListener(listener);
    }

    public PermissionsManager(Activity activity, OnPermissionGrantedListener listener, LifecycleOwner lifecycleOwner) {
        this(activity, listener);
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    public boolean isAllPermissionsGranted() {
        boolean granted = true;
        for (String p : APP_PERMISSIONS) {
            granted = isPermissionGranted(p);
            if (!granted) break;
        }
        return granted;
    }

    public boolean isPermissionGranted(String manifestPermission) {
        if (manifestPermission.equals(ALL_PERMISSIONS)) {
            return isAllPermissionsGranted();
        }
        return ContextCompat.checkSelfPermission(getContext(), manifestPermission) == PackageManager.PERMISSION_GRANTED;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (isPermissionGranted(requestedPermission)) {
                onPermissionGrantedListener.onPermissionGranted(requestedPermission);
            } else {
                onPermissionGrantedListener.onPermissionNotGranted(requestedPermission);
            }
        }
    }

    public void requestPermission(String manifestPermission) {
        requestedPermission = manifestPermission;
        if (isPermissionGranted(manifestPermission)) {
            onPermissionGrantedListener.onPermissionGranted(manifestPermission);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{manifestPermission}, PERMISSION_REQUEST);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void requestAppPermissions() {
        requestedPermission = ALL_PERMISSIONS;
        if (isAllPermissionsGranted()) {
            onPermissionGrantedListener.onPermissionGranted(ALL_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(getActivity(), APP_PERMISSIONS, PERMISSION_REQUEST);
        }
    }

    /********* Getters and Setters below ***********/
    public Context getContext() {
        return activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setOnPermissionGrantedListener(OnPermissionGrantedListener onPermissionGrantedListener) {
        if (onPermissionGrantedListener == null)
            onPermissionGrantedListener = OnPermissionGrantedListener.DEFAULT;
        this.onPermissionGrantedListener = onPermissionGrantedListener;
    }
}

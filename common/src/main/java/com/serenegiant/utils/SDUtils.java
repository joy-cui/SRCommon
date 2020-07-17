//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriPermission;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class SDUtils {
    private static final String TAG = SDUtils.class.getSimpleName();

    public SDUtils() {
    }

    public static void handleOnResult(Context context, int requestCode, int resultCode, Intent data, handleOnResultDelegater delegater) {
        if (data != null && delegater != null) {
            String action = data.getAction();
            if (resultCode == -1) {
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        delegater.onResult(requestCode, uri, data);
                        return;
                    } catch (Exception var9) {
                        Log.w(TAG, var9);
                    }
                }
            }

            try {
                clearUri(context, getKey(requestCode));
                delegater.onFailed(requestCode, data);
            } catch (Exception var8) {
            }
        }

    }

    private static String getKey(int request_code) {
        return String.format(Locale.US, "SDUtils-%d", request_code);
    }

    private static void saveUri(Context context, String key, Uri uri) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), 0);
        if (pref != null) {
            pref.edit().putString(key, uri.toString()).apply();
        }

    }

    @Nullable
    private static Uri loadUri(Context context, String key) {
        Uri result = null;
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), 0);
        if (pref != null && pref.contains(key)) {
            try {
                result = Uri.parse(pref.getString(key, (String)null));
            } catch (Exception var5) {
                Log.w(TAG, var5);
            }
        }

        return result;
    }

    private static void clearUri(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), 0);
        if (pref != null && pref.contains(key)) {
            try {
                pref.edit().remove(key).apply();
            } catch (Exception var4) {
                Log.w(TAG, var4);
            }
        }

    }

    @TargetApi(19)
    public static void requestOpenDocument(Activity activity, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareOpenDocumentIntent(mime_type), request_code);
        }

    }

    @TargetApi(19)
    public static void requestOpenDocument(FragmentActivity activity, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareOpenDocumentIntent(mime_type), request_code);
        }

    }

    @TargetApi(19)
    public static void requestOpenDocument(Fragment fragment, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareOpenDocumentIntent(mime_type), request_code);
        }

    }

    @TargetApi(19)
    public static void requestOpenDocument(android.support.v4.app.Fragment fragment, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareOpenDocumentIntent(mime_type), request_code);
        }

    }

    @TargetApi(19)
    private static Intent prepareOpenDocumentIntent(String mime_type) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType(mime_type);
        return intent;
    }

    @TargetApi(19)
    public static void requestCreateDocument(Activity activity, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareCreateDocument(mime_type, (String)null), request_code);
        }

    }

    @TargetApi(19)
    public static void requestCreateDocument(Activity activity, String mime_type, String default_name, int request_code) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareCreateDocument(mime_type, default_name), request_code);
        }

    }

    @TargetApi(19)
    public static void requestCreateDocument(FragmentActivity activity, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareCreateDocument(mime_type, (String)null), request_code);
        }

    }

    @TargetApi(19)
    public static void requestCreateDocument(FragmentActivity activity, String mime_type, String default_name, int request_code) {
        if (BuildCheck.isKitKat()) {
            activity.startActivityForResult(prepareCreateDocument(mime_type, default_name), request_code);
        }

    }

    @TargetApi(19)
    public static void requestCreateDocument(Fragment fragment, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(mime_type, (String)null), request_code);
        }

    }

    @TargetApi(19)
    public static void requestCreateDocument(Fragment fragment, String mime_type, String default_name, int request_code) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(mime_type, default_name), request_code);
        }

    }

    @TargetApi(19)
    public static void requestCreateDocument(android.support.v4.app.Fragment fragment, String mime_type, int request_code) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(mime_type, (String)null), request_code);
        }

    }

    @TargetApi(19)
    public static void requestCreateDocument(android.support.v4.app.Fragment fragment, String mime_type, String default_name, int request_code) {
        if (BuildCheck.isKitKat()) {
            fragment.startActivityForResult(prepareCreateDocument(mime_type, default_name), request_code);
        }

    }

    @TargetApi(19)
    private static Intent prepareCreateDocument(String mime_type, String default_name) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType(mime_type);
        if (!TextUtils.isEmpty(default_name)) {
            intent.putExtra("android.intent.extra.TITLE", default_name);
        }

        return intent;
    }

    @TargetApi(19)
    public static boolean requestDeleteDocument(Context context, Uri uri) throws FileNotFoundException {
        return BuildCheck.isKitKat() && DocumentsContract.deleteDocument(context.getContentResolver(), uri);
    }

    public static boolean hasStorageAccess(Context context, int request_code) {
        boolean found = false;
        if (BuildCheck.isLollipop()) {
            Uri uri = loadUri(context, getKey(request_code));
            if (uri != null) {
                List<UriPermission> list = context.getContentResolver().getPersistedUriPermissions();
                Iterator var5 = list.iterator();

                while(var5.hasNext()) {
                    UriPermission item = (UriPermission)var5.next();
                    if (item.getUri().equals(uri)) {
                        found = true;
                        break;
                    }
                }
            }
        }

        return found;
    }

    @TargetApi(21)
    public static Uri requestStorageAccess(Activity activity, int request_code) {
        if (BuildCheck.isLollipop()) {
            Uri uri = getStorageUri(activity, request_code);
            if (uri == null) {
                activity.startActivityForResult(prepareStorageAccessPermission(), request_code);
            }

            return uri;
        } else {
            return null;
        }
    }

    @TargetApi(21)
    public static Uri requestStorageAccess(FragmentActivity activity, int request_code) {
        if (BuildCheck.isLollipop()) {
            Uri uri = getStorageUri(activity, request_code);
            if (uri == null) {
                activity.startActivityForResult(prepareStorageAccessPermission(), request_code);
            }

            return uri;
        } else {
            return null;
        }
    }

    @TargetApi(21)
    public static Uri requestStorageAccess(Fragment fragment, int request_code) {
        Uri uri = getStorageUri(fragment.getActivity(), request_code);
        if (uri == null) {
            fragment.startActivityForResult(prepareStorageAccessPermission(), request_code);
        }

        return uri;
    }

    @TargetApi(21)
    public static Uri requestStorageAccess(android.support.v4.app.Fragment fragment, int request_code) {
        if (BuildCheck.isLollipop()) {
            Uri uri = getStorageUri(fragment.getActivity(), request_code);
            if (uri == null) {
                fragment.startActivityForResult(prepareStorageAccessPermission(), request_code);
            }

            return uri;
        } else {
            return null;
        }
    }

    @TargetApi(21)
    @Nullable
    private static Uri getStorageUri(Context context, int request_code) {
        if (BuildCheck.isLollipop()) {
            Uri uri = loadUri(context, getKey(request_code));
            if (uri != null) {
                boolean found = false;
                List<UriPermission> list = context.getContentResolver().getPersistedUriPermissions();
                Iterator var5 = list.iterator();

                while(var5.hasNext()) {
                    UriPermission item = (UriPermission)var5.next();
                    if (item.getUri().equals(uri)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    return uri;
                }
            }
        }

        return null;
    }

    @TargetApi(21)
    private static Intent prepareStorageAccessPermission() {
        return new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
    }

    @SuppressLint("WrongConstant")
    @TargetApi(21)
    public static Uri requestStorageAccessPermission(Context context, int request_code, Uri tree_uri) {
        if (BuildCheck.isLollipop()) {
            context.getContentResolver().takePersistableUriPermission(tree_uri, 3);
            saveUri(context, getKey(request_code), tree_uri);
            return tree_uri;
        } else {
            return null;
        }
    }

    @SuppressLint("WrongConstant")
    @TargetApi(21)
    public static void releaseStorageAccessPermission(Context context, int request_code) {
        if (BuildCheck.isLollipop()) {
            String key = getKey(request_code);
            Uri uri = loadUri(context, key);
            if (uri != null) {
                context.getContentResolver().releasePersistableUriPermission(uri, 3);
                clearUri(context, key);
            }
        }

    }

    @TargetApi(21)
    public static File createStorageDir(Context context, int tree_id) {
        Log.i(TAG, "createStorageDir:");
        if (BuildCheck.isLollipop()) {
            Uri tree_uri = getStorageUri(context, tree_id);
            if (tree_uri != null) {
                DocumentFile save_tree = DocumentFile.fromTreeUri(context, tree_uri);
                return new File(UriHelper.getPath(context, save_tree.getUri()));
            }
        }

        return null;
    }

    @TargetApi(21)
    public static File createStorageFile(Context context, int tree_id, String mime, String file_name) {
        Log.i(TAG, "createStorageFile:" + file_name);
        return createStorageFile(context, getStorageUri(context, tree_id), mime, file_name);
    }

    @TargetApi(21)
    public static File createStorageFile(Context context, Uri tree_uri, String mime, String file_name) {
        Log.i(TAG, "createStorageFile:" + file_name);
        if (BuildCheck.isLollipop() && context != null && tree_uri != null && !TextUtils.isEmpty(file_name)) {
            DocumentFile save_tree = DocumentFile.fromTreeUri(context, tree_uri);
            DocumentFile target = save_tree.createFile(mime, file_name);
            return new File(UriHelper.getPath(context, target.getUri()));
        } else {
            return null;
        }
    }

    @TargetApi(21)
    public static int createStorageFileFD(Context context, int tree_id, String mime, String file_name) {
        Log.i(TAG, "createStorageFileFD:" + file_name);
        return createStorageFileFD(context, getStorageUri(context, tree_id), mime, file_name);
    }

    @TargetApi(21)
    public static int createStorageFileFD(Context context, Uri tree_uri, String mime, String file_name) {
        Log.i(TAG, "createStorageFileFD:" + file_name);
        if (BuildCheck.isLollipop() && context != null && tree_uri != null && !TextUtils.isEmpty(file_name)) {
            DocumentFile save_tree = DocumentFile.fromTreeUri(context, tree_uri);
            DocumentFile target = save_tree.createFile(mime, file_name);

            try {
                ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(target.getUri(), "rw");
                return fd != null ? fd.getFd() : 0;
            } catch (FileNotFoundException var7) {
                Log.w(TAG, var7);
            }
        }

        return 0;
    }

    public interface handleOnResultDelegater {
        void onResult(int var1, Uri var2, Intent var3);

        void onFailed(int var1, Intent var2);
    }
}

package cbedoy.cblibrary.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;


import java.util.HashMap;
import java.util.Map;

import cbedoy.cblibrary.utils.ImageLoaderService;

/**
 * Created by Carlos Bedoy on 28/12/2014.
 *
 * Mobile App Developer
 * CBLibrary
 *
 * E-mail: carlos.bedoy@gmail.com
 * Facebook: https://www.facebook.com/carlos.bedoy
 * Github: https://github.com/cbedoy
 */
public class ApplicationLoader extends MultiDexApplication
{
    public static volatile Handler mainHandler;
    public static volatile Context mainContext;
    public static volatile LayoutInflater mainLayoutInflater;
    public static volatile String urlProject;
    public static volatile Typeface boldFont;
    public static volatile Typeface regularFont;
    public static volatile Typeface thinFont;
    public static volatile Typeface lightFont;
    public static volatile Typeface mediumFont;
    public static volatile Typeface cardFont;
    public static Integer DISMISS_LOADER;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mainContext = getApplicationContext();
        mainHandler = new Handler(getMainLooper());
        mainLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        boldFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Bold.ttf");
        regularFont                     = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Regular.ttf");
        thinFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Thin.ttf");
        lightFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Light.ttf");
        cardFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/CardType.ttf");
        mediumFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Medium.ttf");



        ImageLoaderService.getInstance();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static String getAppVersion() {
        try {
            PackageInfo packageInfo = mainContext.getPackageManager().getPackageInfo(mainContext.getPackageName(), 0);
            return packageInfo.versionName + "  ("+packageInfo.versionCode+")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void savePreferences(String keyShared, HashMap<String, Object> information){
        SharedPreferences sharedPreferences = mainContext.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for(String key : information.keySet()){
            edit.putString(key, information.get(key).toString());
        }
        edit.commit();
    }

    public static HashMap<String, Object> getSharedFromKey(String key){
        HashMap<String, Object> information = new HashMap<String, Object>();
        SharedPreferences sharedPreferences = mainContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        Map<String, ?> all = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : all.entrySet()){
            information.put(entry.getKey(), entry.getValue());
        }
        return information;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private boolean verifyIfApplicationInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}

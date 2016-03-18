
package cbedoy.cblibrary.services;

import android.content.Context;
import android.location.LocationManager;

import com.flurry.android.FlurryAgent;

import java.util.HashMap;

import cbedoy.cblibrary.interfaces.ICheckPointHandler;

/**
 * Created by Carlos Bedoy on 26/12/14.
 * <p/>
 * Mobile App Developer @ Bills Android
 * <p/>
 * Pademobile
 */

public class FlurryService implements ICheckPointHandler
{
    private static FlurryService instance;
    private boolean activateFlurry;
    private String mKey;

    public void setActivateFlurry(boolean activateFlurry) {
        this.activateFlurry = activateFlurry;
    }

    public static FlurryService getInstance()
    {
        if(instance == null)
            instance = new FlurryService();
        return  instance;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    private FlurryService()
    {
        if(activateFlurry)
        {
            FlurryAgent.setLogEnabled(true);
            FlurryAgent.setLogEvents(true);
            FlurryAgent.setReportLocation(true);
            LocationManager lm = (LocationManager)ApplicationLoader.mainContext.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void init(){
        if(activateFlurry)
        {
            FlurryAgent.init(ApplicationLoader.mainContext, mKey);
        }
    }

    public void startSession(Context context)
    {
        if(activateFlurry)
        {
            FlurryAgent.onStartSession(context, mKey);
        }
    }

    public void endSession(Context context)
    {
        if(activateFlurry)
            FlurryAgent.onEndSession(context);
    }

    @Override
    public void logEvent(String event)
    {
        if(activateFlurry) {
            FlurryAgent.logEvent(event);
        }
    }

    @Override
    public void logEvent(String event, boolean timed)
    {
        if(activateFlurry) {
            FlurryAgent.logEvent(event, timed);
        }
    }

    @Override
    public void logEvent(String event, HashMap<String, String> parameters)
    {
        if(activateFlurry)
        {
            FlurryAgent.logEvent(event, parameters);
        }
    }

    @Override
    public void logEvent(String event, HashMap<String, String> parameters, boolean timed)
    {
        if(activateFlurry)
        {
            FlurryAgent.logEvent(event, parameters, timed);
        }
    }

}

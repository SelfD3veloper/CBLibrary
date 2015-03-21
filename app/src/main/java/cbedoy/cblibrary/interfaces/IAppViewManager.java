package cbedoy.cblibrary.interfaces;

import android.app.Activity;

import cbedoy.cblibrary.viewcontrollers.AbstractViewController;

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
public interface IAppViewManager
{
    public Activity getActivity();
    public void reActivateCurrentView();
    public int getViewControllerWidth();
    public int getViewControllerHeight();
    public void presentLeftMenu();
    public void presentViewForTag(Enum tag);
    public void addViewWithTag(AbstractViewController controller, Enum tag);
}

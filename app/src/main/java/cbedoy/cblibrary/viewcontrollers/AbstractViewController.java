package cbedoy.cblibrary.viewcontrollers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import cbedoy.cblibrary.interfaces.IAppViewManager;
import cbedoy.cblibrary.interfaces.IMessageRepresentationHandler;

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
public abstract class AbstractViewController
{
    protected CONTROLLER tag;
    protected IAppViewManager appViewManager;
    protected View view;
    protected Bitmap backgroundBitmap;
    protected IMessageRepresentationHandler messageRepresentationHandler;
    protected boolean backStatus;

    public enum CONTROLLER
    {
        LOGIN,
        PROFILE,
        ABOUT
    }

    protected abstract View init();
    public abstract void reload();

    public void setTag(CONTROLLER tag) {
        this.tag = tag;
    }

    public void setAppViewManager(IAppViewManager appViewManager) {
        this.appViewManager = appViewManager;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        this.backgroundBitmap = backgroundBitmap;
    }

    public void setMessageRepresentationHandler(IMessageRepresentationHandler messageRepresentationHandler) {
        this.messageRepresentationHandler = messageRepresentationHandler;
    }

    public void setView(View view) {
        this.view = view;
    }



    public View getView() {
        if(view == null)
            view = init();
        return view;
    }

    public void backPressed() {
        this.appViewManager.getActivity().finish();
    }

    public void nextPressed() {

    }

    public void setBackStatus(boolean backStatus) {
        this.backStatus = backStatus;
    }

    public boolean onBackPressed() {
        return true;
    }


}

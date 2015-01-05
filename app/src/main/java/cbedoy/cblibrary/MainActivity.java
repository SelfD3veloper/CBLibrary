package cbedoy.cblibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cbedoy.cblibrary.interfaces.IAppViewManager;
import cbedoy.cblibrary.interfaces.IMementoHandler;
import cbedoy.cblibrary.interfaces.IMessageRepresentationHandler;
import cbedoy.cblibrary.services.ApplicationLoader;
import cbedoy.cblibrary.services.ImageService;
import cbedoy.cblibrary.services.InjectionManager;
import cbedoy.cblibrary.services.MementoHandler;
import cbedoy.cblibrary.services.NotificationCenter;
import cbedoy.cblibrary.utils.Utils;
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

public class MainActivity extends ActionBarActivity implements IAppViewManager, NotificationCenter.NotificationListener{

    private ViewFlipper viewFlipper;
    private SlidingPaneLayout slidingPaneLayout;
    private HashMap<Object, Object> informationService;
    private LinearLayout mainLayout;
    private int view_controller_width;
    private int view_controller_height;
    private HashMap<AbstractViewController.CONTROLLER, AbstractViewController> viewModel;
    private ViewGroup splashView;

    private IMementoHandler mementoHandler;
    private IMessageRepresentationHandler messageRepresentationHandler;
    private LinearLayout leftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationCenter.getInstance().addListener(ApplicationLoader.DISMISS_LOADER, this);
        this.splashView = (LinearLayout) findViewById(R.id.splash_view);
        this.leftMenu = (LinearLayout) findViewById(R.id.main_view_controller_left_menu);
        getSupportActionBar().hide();
        Utils.init(this);
        ImageService.init(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.viewModel = new HashMap<AbstractViewController.CONTROLLER, AbstractViewController>();
        this.view_controller_height = ImageService.getScreenHeight();
        this.view_controller_width = ImageService.getScreenWidth();

        this.viewFlipper = (ViewFlipper) findViewById(R.id.main_view_controller_view_flipper);
        this.slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        this.slidingPaneLayout.setCoveredFadeColor(Color.parseColor("#00000000"));
        this.slidingPaneLayout.setSliderFadeColor(Color.parseColor("#00000000"));


        TranslateAnimation in = new TranslateAnimation(ImageService.getScreenWidth(), 0, 0, 0);
        in.setDuration(3000);
        in.setZAdjustment(Animation.ZORDER_TOP);
        this.viewFlipper.setInAnimation(in);
        TranslateAnimation out = new TranslateAnimation(0, -ImageService.getScreenWidth(), 0, 0);
        out.setDuration(3000);
        out.setZAdjustment(Animation.ZORDER_TOP);
        this.viewFlipper.setOutAnimation(out);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        leftMenu.setVisibility(View.GONE);

        InjectionManager.getInstance();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void setLeftMenuView(AbstractViewController controller) {
        this.leftMenu.addView(controller.getView());
    }

    @Override
    public void finish() {
        long delay = 200;
        final MainActivity self = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                self.terminateInApp();
            }
        }, delay);
    }

    private void terminateInApp(){
        this.overridePendingTransition(R.anim.fadeout, R.anim.fadein);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        boolean allowBack = true;
        int displayed_child = this.viewFlipper.getDisplayedChild();
        View view = this.viewFlipper.getChildAt(displayed_child);

        for(Map.Entry<AbstractViewController.CONTROLLER, AbstractViewController> entry : this.viewModel.entrySet()) {
            AbstractViewController child = entry.getValue();
            if(child.getView() == view) {
                allowBack = child.onBackPressed();
                break;
            }
        }

        if(allowBack)
            super.onBackPressed();
    }

    @Override
    public int getViewControllerWidth() {
        return this.view_controller_width;
    }

    @Override
    public int getViewControllerHeight() {
        return this.view_controller_height;
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public void reActivateCurrentView() {
        final MainActivity self = this;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int displayed_child = self.viewFlipper.getDisplayedChild();
                View view = self.viewFlipper.getChildAt(displayed_child);

                for(Map.Entry<AbstractViewController.CONTROLLER, AbstractViewController> entry : self.viewModel.entrySet()) {
                    AbstractViewController child = entry.getValue();

                    if(child.getView() == view) {
                        child.toogleButtons(true);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void presentViewForTag(AbstractViewController.CONTROLLER tag) {
        final MainActivity self = this;
        final AbstractViewController.CONTROLLER final_tag = tag;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AbstractViewController child = self.viewModel.get(final_tag);
                View view = child.getView();

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

                int child_index = self.viewFlipper.indexOfChild(view);
                if (child_index < 0) {
                    self.viewFlipper.addView(view);
                    child_index = self.viewFlipper.indexOfChild(view);
                }

                int displayed_child = self.viewFlipper.getDisplayedChild();
                if (child_index != displayed_child) {
                    int width = self.view_controller_width;
                    int ltr = child_index > displayed_child ? 1 : -1;

                    {{
                        AlphaAnimation in = new AlphaAnimation(0.0f, 1.0f);
                        in.setDuration(600);
                        in.setZAdjustment(Animation.ZORDER_TOP);
                        self.viewFlipper.setInAnimation(in);

                        AlphaAnimation out = new AlphaAnimation(1.0f, 0.0f);
                        out.setDuration(600);
                        out.setZAdjustment(Animation.ZORDER_TOP);
                        self.viewFlipper.setOutAnimation(out);
                    }}

                    self.viewFlipper.setDisplayedChild(child_index);
                }

                child.reload();
            }
        });
    }

    @Override
    public void addViewWithTag(AbstractViewController controller, AbstractViewController.CONTROLLER tag) {
        this.viewModel.put(tag, controller);
    }

    @Override
    public void presentLeftMenu() {
        if(slidingPaneLayout != null)
        {
            slidingPaneLayout.setVisibility(View.VISIBLE);
            if (slidingPaneLayout.isOpen())
                slidingPaneLayout.closePane();
            else
                slidingPaneLayout.openPane();
        }
    }

    @Override
    public void didReceivedNotification(Integer type, Object... args)
    {
        if(type == ApplicationLoader.DISMISS_LOADER)
        {
            Animation animation = new AlphaAnimation(1.00f, 0.00f);
            animation.setDuration(200);
            animation.setAnimationListener(new Animation.AnimationListener()
            {
                public void onAnimationStart(Animation animation)
                {
                    splashView.setVisibility(View.VISIBLE);
                    leftMenu.setVisibility(View.GONE);
                }
                public void onAnimationRepeat(Animation animation) {}

                public void onAnimationEnd(Animation animation)
                {
                    splashView.setVisibility(View.GONE);
                    leftMenu.setVisibility(View.VISIBLE);
                }
            });
            if(splashView.getVisibility() == View.VISIBLE)
                splashView.startAnimation(animation);

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}

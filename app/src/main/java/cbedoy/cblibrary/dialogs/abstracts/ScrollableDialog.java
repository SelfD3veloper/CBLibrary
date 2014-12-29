package cbedoy.cblibrary.dialogs.abstracts;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cbedoy.cblibrary.R;
import cbedoy.cblibrary.dialogs.artifacts.TutorialAdapter;
import cbedoy.cblibrary.widgets.CircleView;

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
public abstract class ScrollableDialog extends AbstractDialog implements ViewPager.OnPageChangeListener
{
    private ViewPager viewPager;
    private ImageButton actionClose;
    private View indicadorView;
    private View titleContainer;
    protected ArrayList<View> viewModel;
    protected ArrayList<CircleView> circleModel;
    protected TutorialAdapter tutorialAdapter;
    protected TextView titleView;
    protected LinearLayout layoutCircles;

    public ScrollableDialog(Activity activity)
    {
        super(activity);
    }

    @Override
    public View init() {
        view = activity.getLayoutInflater().inflate(R.layout.dialog_flipper, null);
        viewPager = (ViewPager) view.findViewById(R.id.dialog_flipper_view_pager);
        indicadorView = view.findViewById(R.id.dialog_flipper_indicator_view);
        titleView = (TextView) view.findViewById(R.id.dialog_flipper_title);
        actionClose = (ImageButton) view.findViewById(R.id.dialog_flipper_action_close);
        titleContainer = view.findViewById(R.id.dialog_flipper_title_container);
        viewModel = new ArrayList<View>();
        circleModel = new ArrayList<CircleView>();
        tutorialAdapter = new TutorialAdapter(activity.getApplicationContext(), viewModel);
        layoutCircles = (LinearLayout) view.findViewById(R.id.dialog_flipper_circles);
        viewPager.setAdapter(tutorialAdapter);
        viewPager.setOnPageChangeListener(this);
        actionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        view.findViewById(R.id.overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        return view;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int index=0; index<tutorialAdapter.getCount(); index++)
        {
            CircleView circleView = circleModel.get(index);
            circleView.setStatus(position == index);
            circleView.reload();
        }
    }

    @Override
    public void onPageScrollStateChanged(int position)
    {

    }

    public void reload()
    {
        if(layoutCircles.getChildCount()>0)
            layoutCircles.removeAllViews();
        for(CircleView circleView : circleModel) {
            circleView.getView();
            circleView.reload();
            layoutCircles.addView(circleView.getView());
        }
        tutorialAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
    }
}

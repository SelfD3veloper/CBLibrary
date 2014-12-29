package cbedoy.cblibrary.viewcontrollers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.cbedoy.apprende.R;
import com.cbedoy.apprende.artifacts.AbstractViewPager;
import com.cbedoy.apprende.service.MementoHandler;
import com.cbedoy.apprende.widgets.abstracts.AbstractView;

import java.util.ArrayList;
import java.util.List;

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
public class AbstractPagerViewController extends AbstractViewController {

    protected AbstractViewPager abstractViewPager;
    protected ViewPager viewPager;
    protected MementoHandler mementoHandler;
    protected List<AbstractView> viewModel;
    protected List<Object> dataModel;
    protected ImageView background;

    public void setMementoHandler(MementoHandler mementoHandler) {
        this.mementoHandler = mementoHandler;
    }



    @Override
    protected View init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.app_aprende_viewcontroller,  null);
        this.background = (ImageView) view.findViewById(R.id.background_apprende);
        this.viewPager = (ViewPager) view.findViewById(R.id.pager);
        this.dataModel = new ArrayList<Object>();
        this.viewModel = new ArrayList<AbstractView>();
        return view;
    }

    @Override
    public void reload() {

    }
}

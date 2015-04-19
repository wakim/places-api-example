package br.com.wakim.placesexample.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.PlaceLikelihood;

import java.util.List;

import br.com.wakim.placesexample.R;

/**
 * Created by wakim on 19/04/15.
 */
public class PlacesPagerAdater extends PagerAdapter {

    List<PlaceLikelihood> mData;
    LayoutInflater mInflater;

    public PlacesPagerAdater(Activity activity) {
        mInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.pager_list_item, container, false);

        ((TextView) view.findViewById(android.R.id.text1)).setText(mData.get(position).getPlace().getName());
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setData(List<PlaceLikelihood> data) {
        mData = data;
        notifyDataSetChanged();
    }
}

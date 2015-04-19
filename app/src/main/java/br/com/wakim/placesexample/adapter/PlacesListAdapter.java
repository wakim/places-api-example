package br.com.wakim.placesexample.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.util.ArrayList;
import java.util.List;

import br.com.wakim.placesexample.R;

/**
 * Created by wakim on 19/04/15.
 */
public class PlacesListAdapter extends RecyclerView.Adapter<PlacesViewHolder> implements View.OnClickListener {

    LayoutInflater mLayoutInflater;
    List<PlaceLikelihood> mData;

    ListCallback mCallback;

    public PlacesListAdapter(Activity activity) {
        mLayoutInflater = activity.getLayoutInflater();
        mData = new ArrayList<>();
    }

    public PlacesListAdapter setListCallback(ListCallback callback) {
        mCallback = callback;
        return this;
    }

    public void add(PlaceLikelihood place, boolean notify) {
        mData.add(place);
        if(notify) notifyDataSetChanged();
    }

    public void setData(List<PlaceLikelihood> places) {
        mData = places;
        notifyDataSetChanged();
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        view.setOnClickListener(this);
        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder placesViewHolder, int i) {
        Place place = mData.get(i).getPlace();

        placesViewHolder.textView1.setText(place.getName());
        placesViewHolder.textView2.setText(place.getAddress());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if(mCallback != null) {
            mCallback.onItemSelected(v);
        }
    }

    public interface ListCallback {
        void onItemSelected(View item);
    }
}

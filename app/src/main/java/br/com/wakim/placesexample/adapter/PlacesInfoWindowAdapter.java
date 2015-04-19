package br.com.wakim.placesexample.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import br.com.wakim.placesexample.R;

/**
 * Created by wakim on 19/04/15.
 */
public class PlacesInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    List<PlaceLikelihood> mData;
    LayoutInflater mInflater;

    public PlacesInfoWindowAdapter(Activity activity) {
        mInflater = activity.getLayoutInflater();
    }

    public void setData(List<PlaceLikelihood> data) {
        mData = data;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Place place = getPlaceFromMarker(marker).getPlace();
        View v = mInflater.inflate(R.layout.info_marker, null);
        RatingBar rating = (RatingBar) v.findViewById(R.id.info_rating);

        setTextOrHide((TextView) v.findViewById(R.id.info_name), place.getName());
        setTextOrHide((TextView) v.findViewById(R.id.info_tel), place.getPhoneNumber());
        setTextOrHide((TextView) v.findViewById(R.id.info_address), place.getAddress());
        setTextOrHide((TextView) v.findViewById(R.id.info_website), place.getWebsiteUri() == null ? null : place.getWebsiteUri().toString());

        rating.setRating(place.getRating());
        rating.setNumStars(5);
        rating.setIsIndicator(true);

        return v;
    }

    void setTextOrHide(TextView textView, CharSequence text) {
        if(text == null || text.length() == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
    }

    PlaceLikelihood getPlaceFromMarker(Marker marker){
        return mData.get(Integer.parseInt(marker.getSnippet()));
    }
}

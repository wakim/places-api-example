package br.com.wakim.placesexample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wakim on 19/04/15.
 */
public class PlacesViewHolder extends RecyclerView.ViewHolder {

    protected TextView textView1, textView2;

    public PlacesViewHolder(View itemView) {
        super(itemView);

        textView1 = (TextView) itemView.findViewById(android.R.id.text1);
        textView2 = (TextView) itemView.findViewById(android.R.id.text2);
    }
}

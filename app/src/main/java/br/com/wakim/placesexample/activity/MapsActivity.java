package br.com.wakim.placesexample.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.wakim.placesexample.R;
import br.com.wakim.placesexample.adapter.PlacesInfoWindowAdapter;
import br.com.wakim.placesexample.adapter.PlacesListAdapter;
import br.com.wakim.placesexample.adapter.PlacesPagerAdater;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, ViewPager.OnPageChangeListener,
    PlacesListAdapter.ListCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;

    private RecyclerView mRecyclerView;
    private ViewPager mPager;

    private PlacesListAdapter mAdapter;
    private PlacesPagerAdater mPagerAdapter;
    private PlacesInfoWindowAdapter mInfoWindowAdapter;

    private List<PlaceLikelihood> mPlaces;
    private List<Marker> mMarkers;

    private GoogleMap.OnMyLocationChangeListener mListener = new GoogleMap.OnMyLocationChangeListener(){
        @Override
        public void onMyLocationChange(Location location) {
            moveToLocationOneShot(location);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        configureViewPager();
        configureRecyclerView();
        queryForNearbyPlaces();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void configureViewPager() {
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mPagerAdapter = new PlacesPagerAdater(this));

        mPager.setOnPageChangeListener(this);
    }

    private void configureRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new PlacesListAdapter(this).setListCallback(this));
        mRecyclerView.setHasFixedSize(true);
    }

    private void queryForNearbyPlaces() {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                mPlaces = new ArrayList<>(likelyPlaces.getCount());
                mMarkers = new ArrayList<>(likelyPlaces.getCount());
                mInfoWindowAdapter = new PlacesInfoWindowAdapter(MapsActivity.this);

                int i = 0;

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    mPlaces.add(placeLikelihood.freeze());
                    mMarkers.add(mMap.addMarker(buildMarkerForPlace(placeLikelihood.getPlace(), i++)));
                }

                mAdapter.setData(mPlaces);
                mPagerAdapter.setData(mPlaces);
                mInfoWindowAdapter.setData(mPlaces);

                likelyPlaces.release();

                mMap.setInfoWindowAdapter(mInfoWindowAdapter);
            }
        });
    }

    MarkerOptions buildMarkerForPlace(Place place, int position) {
        MarkerOptions mo = new MarkerOptions();

        mo.position(place.getLatLng());
        mo.title(place.getName().toString());
        mo.snippet(Integer.toString(position));

        return mo;
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setOnMyLocationChangeListener(mListener);
        mMap.setInfoWindowAdapter(mInfoWindowAdapter);
        mMap.setOnMarkerClickListener(this);
    }

    private void moveToLocationOneShot(Location location) {
        moveToLocation(location);
        mMap.setOnMyLocationChangeListener(null);
    }

    private void moveToLocation(Location location) {
        moveToLocation(getLatLng(location));
    }

    private void moveToLocation(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16f, 0f, 0f)));
    }

    private LatLng getLatLng(Location loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("TAG", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("TAG", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("TAG", "onConnectionFailed");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        mRecyclerView.smoothScrollToPosition(position);
        showMarker(mMarkers.get(position));
    }

    void showMarker(Marker marker) {
        moveToLocation(marker.getPosition());
        marker.showInfoWindow();
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onItemSelected(View item) {
        int position = mRecyclerView.getChildLayoutPosition(item);

        mPager.setCurrentItem(position, true);
        showMarker(mMarkers.get(position));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int position = mMarkers.indexOf(marker);

        mPager.setCurrentItem(position, true);
        mRecyclerView.smoothScrollToPosition(position);

        return false;
    }
}

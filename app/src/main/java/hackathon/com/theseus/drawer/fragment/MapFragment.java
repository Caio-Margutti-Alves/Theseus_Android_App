package hackathon.com.theseus.drawer.fragment;

import android.app.AlertDialog;
import hackathon.com.theseus.dialog.ArrayAdapterWithIcon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hackathon.com.theseus.R;
import hackathon.com.theseus.drawer.utils.Constant;
import hackathon.com.theseus.gps.Constants;
import hackathon.com.theseus.gps.FetchAddressIntentService;
import trail.Trail;
import trail.TrailList;


/**
 * Created by caioa_000 on 21/02/2015.
 */

public class MapFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "main-activity";

    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected boolean mAddressRequested;
    protected String mAddressOutput;
    private AddressResultReceiver mResultReceiver;
    protected TextView mLocationAddressTextView;

    private MapView mapView;
    private ImageButton imgb_mark;
    private ImageButton imgb_maps_style;
    private ImageButton imgb_trails_search;
    private ProgressBar mProgressBar;
    private LinkedList<Polyline> polylines;

    protected double lat = 0;
    protected double lng = 0;


    ImageButton.OnClickListener lstnMark = new Button.OnClickListener() {
        public void onClick(View view) {
            final String[] options = getResources().getStringArray(R.array.mark_option);
            final String[] descriptions = getResources().getStringArray(R.array.mark_descriptions);

            final Integer[] icons = new Integer[] {R.drawable.map_map_marker, R.drawable.animal_sign,
                    R.drawable.sign_warning, R.drawable.cone, R.drawable.skull, R.drawable.bubble };

            ListAdapter adapter = new ArrayAdapterWithIcon(getActivity(), options, icons);

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle(getResources().getString(R.string.dialog_mark));
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   Marker marker;


                    switch (which) {
                        case Constant.MENU_VIEW_POINT:
                            marker = mapView.getMap().addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(String.valueOf(options[which]))
                                    .snippet(String.valueOf(descriptions[which]))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_map_marker)));
                            break;
                        case Constant.MENU_ANIMAL:
                            marker = mapView.getMap().addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title(String.valueOf(options[which]))
                                .snippet(String.valueOf(descriptions[which]))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.animal_sign)));
                            break;
                        case Constant.MENU_WARNING:
                            marker = mapView.getMap().addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                            .title(String.valueOf(options[which]))
                                            .snippet(String.valueOf(descriptions[which]))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.sign_warning)));
                            break;
                        case Constant.MENU_CLOSURE:
                            marker = mapView.getMap().addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                            .title(String.valueOf(options[which]))
                                            .snippet(String.valueOf(descriptions[which]))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cone)));
                            break;
                        case Constant.MENU_EMERGENCY:
                            marker = mapView.getMap().addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                            .title(String.valueOf(options[which]))
                                            .snippet(String.valueOf(descriptions[which]))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.skull)));
                            break;
                        case Constant.MENU_QUESTION:
                            marker = mapView.getMap().addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                            .title(String.valueOf(options[which]))
                                            .snippet(String.valueOf(descriptions[which]))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bubble)));
                            break;
                    }
                }
            });
            builder.show();
        }
    };

    ImageButton.OnClickListener lstnMapsStyle = new Button.OnClickListener() {
        public void onClick(View view) {
            final String[] options = getResources().getStringArray(R.array.maps_styles);

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle(getResources().getString(R.string.dialog_maps_styles));
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Constant.MENU_NORMAL:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case Constant.MENU_HYBRID:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case Constant.MENU_SATELLITE:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case Constant.MENU_TERRAIN:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                    }
                }
            });
            builder.show();
        }
    };

    ImageButton.OnClickListener lstnTrailSearch = new Button.OnClickListener() {
        public void onClick(View view) {
            final String[] options = getResources().getStringArray(R.array.trails_options);

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle(getResources().getString(R.string.dialog_trails_options));
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Constant.MENU_0:
                            setUpTrail(null,0);
                            break;
                        case Constant.MENU_1:
                            setUpTrail(null,1);
                            break;
                        case Constant.MENU_2:
                            setUpTrail(null,2);
                            break;
                        case Constant.MENU_3:
                            setUpTrail(null,3);
                            break;
                        case Constant.MENU_4:
                            setUpTrail(null,4);
                            break;
                    }
                }
            });
            builder.show();
        }
    };

    public MapFragment newInstance(String text){
        MapFragment mFragment = new MapFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(Constant.TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    // requires list of format (lat, lon, lat, lon, lat, lon....)
    private void setUpTrail(TrailList trails, int index) {
        //Trail trail = new Trail();
        //trail = new Trail(new LinkedList(Arrays.asList(getResources().getStringArray(R.array.trail_jones_valley_corridor))));
        if(polylines.size()!=0) {
            for (Polyline pol : polylines) {
                pol.remove();
            }
        }
        //FIX IT FOR WORKING WITH COLLECTIONS!
        if(index == 0){
            for(int i = 0; i < 4; i++) {
                String[] trail = null;
                if (i == 0)
                    trail = getResources().getStringArray(R.array.trail_jones_valley_corridor);
                else if (i == 1)
                    trail = getResources().getStringArray(R.array.trail_shades_creek_corridor);
                else if (i == 2)
                    trail = getResources().getStringArray(R.array.trail_turkey_creek_corridor);
                else if (i == 3)
                    trail = getResources().getStringArray(R.array.trail_cahaba_river_corridor);
                //for (Trail trail : trails.getAllTrails()) {
                int R = (int) (Math.random() * 256);
                int G = (int) (Math.random() * 256);
                int B = (int) (Math.random() * 256);
                int color = Color.argb(255, R, G, B);

                    /*Random random = new Random();
                    final float hue = random.nextFloat();
                    final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
                    final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
                    Color col = new Color(color);
                    col.getHSBColor(hue, saturation, luminance);*/

                Polyline line = mapView.getMap().addPolyline(new PolylineOptions().width(7).color(color));
                List<LatLng> coordinates = new ArrayList<LatLng>();

                for (String coordinate : trail) {
                    String[] coord = coordinate.split(" ");
                    coordinates.add(new LatLng(Double.parseDouble(coord[0]), Double.parseDouble(coord[1])));
                }
                line.setPoints(coordinates);
                polylines.add(line);
                // }
            }
        }else{
            String[] trail = null;
            if (index == 1)
                trail = getResources().getStringArray(R.array.trail_jones_valley_corridor);
            else if (index == 2)
                trail = getResources().getStringArray(R.array.trail_shades_creek_corridor);
            else if (index == 3)
                trail = getResources().getStringArray(R.array.trail_turkey_creek_corridor);
            else if (index == 4)
                trail = getResources().getStringArray(R.array.trail_cahaba_river_corridor);
            int R = (int) (Math.random() * 256);
            int G = (int) (Math.random() * 256);
            int B = (int) (Math.random() * 256);
            int color = Color.argb(255, R, G, B);

            Polyline line = mapView.getMap().addPolyline(new PolylineOptions().width(7).color(color));
            List<LatLng> coordinates = new ArrayList<LatLng>();

            for (String coordinate : trail) {
                String[] coord = coordinate.split(" ");
                coordinates.add(new LatLng(Double.parseDouble(coord[0]), Double.parseDouble(coord[1])));
            }
            line.setPoints(coordinates);
            polylines.add(line);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapview, container, false);

        polylines = new LinkedList<Polyline>();

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mapView.onCreate(savedInstanceState);
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMap().getUiSettings().setCompassEnabled(true);

        imgb_mark = (ImageButton) view.findViewById(R.id.imgb_mark);
        imgb_mark.setOnClickListener(lstnMark);

        imgb_maps_style = (ImageButton) view.findViewById(R.id.imgb_maps_style);
        imgb_maps_style.setOnClickListener(lstnMapsStyle);

        imgb_trails_search = (ImageButton) view.findViewById(R.id.imgb_trail_search);
        imgb_trails_search.setOnClickListener(lstnTrailSearch);

        mResultReceiver = new AddressResultReceiver(new Handler());

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;
        mAddressOutput = "";
        updateValuesFromBundle(savedInstanceState);

        updateUIWidgets();
        buildGoogleApiClient();

        mapView.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        MapsInitializer.initialize(getActivity());

        TrailList trails = new TrailList();
        trails.startTrailList(getActivity());
        setUpTrail(trails, 0);

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    public void fetchAddressButtonHandler(View view) {
        // We only start the service to fetch the address if GoogleApiClient is connected.
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        // If GoogleApiClient isn't connected, we process the user's request by setting
        // mAddressRequested to true. Later, when GoogleApiClient connects, we launch the service to
        // fetch the address. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
        updateUIWidgets();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /*
     * Called when the Activity is going into the background. Parts of the UI
     * may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            if (!Geocoder.isPresent()) {
                Toast.makeText(getActivity(), R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
                return;
            }
            // It is possible that the user presses the button to get the address before the
            // GoogleApiClient object successfully connects. In such a case, mAddressRequested
            // is set to true, but no attempt is made to fetch the address (see
            // fetchAddressButtonHandler()) . Instead, we start the intent service here if the
            // user has requested an address, since we now have a connection to GoogleApiClient.
            if (mAddressRequested) {
                startIntentService();
            }
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        mLocationAddressTextView.setText(mAddressOutput);
    }

    /**
     * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
     */
    private void updateUIWidgets() {
        if (mAddressRequested) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            imgb_mark.setEnabled(false);
        } else {
            mProgressBar.setVisibility(ProgressBar.GONE);
            imgb_mark.setEnabled(true);
        }
    }

    /**
     * Shows a toast with the given text.
     */
    protected void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            updateUIWidgets();
        }
    }
}

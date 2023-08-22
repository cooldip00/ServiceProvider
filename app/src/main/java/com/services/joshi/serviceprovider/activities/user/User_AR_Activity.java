package com.services.joshi.serviceprovider.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.annotation.Nullable;
import android.Manifest;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.services.joshi.serviceprovider.LocationProvider;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.MainActivity;
import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class User_AR_Activity extends AppCompatActivity implements LocationListener {

    private final PermissionManager permissionManager = ArchitectView.getPermissionManager();

    /** If the POIs were already generated and sent to JavaScript. */
    private boolean injectedPois = false;

    /**
     * Very basic location provider to enable location updates.
     * Please note that this approach is very minimal and we recommend to implement a more
     * advanced location provider for your app. (see https://developer.android.com/training/location/index.html)
     */
    private LocationProvider locationProvider;

    private ArchitectView architectView;

    /**
     * Error callback of the LocationProvider, noProvidersEnabled is called when neither location over GPS nor
     * location over the network are enabled by the device.
     */
    private final LocationProvider.ErrorCallback errorCallback = new LocationProvider.ErrorCallback() {
        @Override
        public void noProvidersEnabled() {
            Toast.makeText(User_AR_Activity.this, R.string.no_location_provider, Toast.LENGTH_LONG).show();
        }
    };

    /**
     * The ArchitectView.SensorAccuracyChangeListener notifies of changes in the accuracy of the compass.
     * This can be used to notify the user that the sensors need to be recalibrated.
     *
     * This listener has to be registered after onCreate and unregistered before onDestroy in the ArchitectView.
     */
    private final ArchitectView.SensorAccuracyChangeListener sensorAccuracyChangeListener = new ArchitectView.SensorAccuracyChangeListener() {
        @Override
        public void onCompassAccuracyChanged(int accuracy) {
            if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) { // UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3
                Toast.makeText(User_AR_Activity.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__ar_);

        locationProvider = new LocationProvider(this, this, errorCallback);
        // Used to enabled remote debugging of the ArExperience with google chrome https://developers.google.com/web/tools/chrome-devtools/remote-debugging
        WebView.setWebContentsDebuggingEnabled(true);

        // camera permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA , Manifest.permission.ACCESS_FINE_LOCATION},
                10);

        /*
         * The ArchitectStartupConfiguration is required to call architectView.onCreate.
         * It controls the startup of the ArchitectView which includes camera settings,
         * the required device features to run the ArchitectView and the LicenseKey which
         * has to be set to enable an AR-Experience.
         */
        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey("EqzsdD3gFBcXa+TXxoL5bSkyJ4eN7IRRKYomnUOahpUQZToxcO7zaHzqK+OizloWcBgA0Yq2c4JohspxrD0AQMeIoGd5Eb112gFirP3D4QJ8FsS93tqzXLuMX9kGPiT9hj8DBne3W9U0xrcp6CFh3EUY0D9ymRfOqPKYCh3hwKhTYWx0ZWRfXz/ma4UdloFBrW2UU4RWbstXKpCoUA0p9QTbdChVqwZmkMn1GtcJEH67nlzStWNCzD9EsOWL0AAcshmWn7Uh/cGOpBAVen/R+70IdL9serAx5YmABC2lwL49xTEjUY4vddLv9Wwv8PbmAlH54QBDbw+/CcGhWzZ7Wtn4TpfA9jczYsc/w+hdvtpB343Vreo7Eay5B7T2HtsCts9og3oEMd0xN7M8wdOnvSUiy5rYvEH7agZK3rN+TjEJNmVsUYnDd6hnMkjUX1N47kqPeDEMFDYHGoCkgUeSS2a3pB5bXq4jIgER5wZgJiN1LSpDHZ3gi9SLyTw76dy6a89pvBHflUcA56qExfGRjP7TJS9qToj4krvi1fFJGO0oTB3mT+GHatodlMz7vSIBZHt0UlwoejQxP9C5x6+zncFM46AYCsykJup3SgBTnp79+nGWyKonr8dwfAcA/+boLxXIvkc0gTmLM/QmaFCaQz27Di1Te/JSvpF6Rxv/ZPdX25S9bif9MRve843szq4cJBcVv+d+k2fUlNF6hchVFtFyLOEzrZ01JLFLn38WEma+MfUfXwXdkqdUzfGmQfSS");
        this.architectView.onCreate( config );
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

        }*/

        architectView.onPostCreate();

        try {
            /*
             * Loads the AR-Experience, it may be a relative path from assets,
             * an absolute path (file://) or a server url.
             *
             * To get notified once the AR-Experience is fully loaded,
             * an ArchitectWorldLoadedListener can be registered.
             */
            this.architectView.load( "file:///android_asset/demo1/index.html" );
            //this.architectView.load( "https://joshibrothers.000webhostapp.com/WikitudeTest/index.html" );
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_loading_ar_experience), Toast.LENGTH_SHORT).show();
            //Log.e(TAG, "Exception while loading  " +  + ".", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationProvider.onResume();

        architectView.onResume();// Mandatory ArchitectView lifecycle call
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*
         * Deletes all cached files of this instance of the ArchitectView.
         * This guarantees that internal storage for this instance of the ArchitectView
         * is cleaned and app-memory does not grow each session.
         *
         * This should be called before architectView.onDestroy
         */
        architectView.clearCache();
        architectView.onDestroy();// Mandatory ArchitectView lifecycle call
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationProvider.onPause();
        architectView.onPause();// Mandatory ArchitectView lifecycle call
    }

    /**
     * The ArchitectView has to be notified when the location of the device
     * changed in order to accurately display the Augmentations for Geo AR.
     *
     * The ArchitectView has two methods which can be used to pass the Location,
     * it should be chosen by whether an altitude is available or not.
     */
    @Override
    public void onLocationChanged(Location location) {
        float accuracy = location.hasAccuracy() ? location.getAccuracy() : 1000;
        if (location.hasAltitude()) {
            architectView.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), accuracy);
        } else {
            architectView.setLocation(location.getLatitude(), location.getLongitude(), accuracy);
        }

        if (!injectedPois) {
            final JSONArray jsonArray = generatePoiInformation(location);
            architectView.callJavascript("World.loadPoisFromJsonData(" + jsonArray.toString() + ")"); // Triggers the loadPoisFromJsonData function
            injectedPois = true; // don't load pois again
        }
    }

    /**
     * The very basic LocationProvider setup of this sample app does not handle the following callbacks
     * to keep the sample app as small as possible. They should be used to handle changes in a production app.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private static JSONArray generatePoiInformation(final Location userLocation) {

        final JSONArray pois = new JSONArray();

        // ensure these attributes are also used in JavaScript when extracting POI data
        final String ATTR_ID = "id";
        final String ATTR_NAME = "name";
        final String ATTR_DESCRIPTION = "description";
        final String ATTR_LATITUDE = "latitude";
        final String ATTR_LONGITUDE = "longitude";
        final String ATTR_ALTITUDE = "altitude";

        String name[] = {"Plumber","Electrician","Carpenter","Mechanic","Technician",
                "Plumber","Electrician","Carpenter","Mechanic","Technician",
                "Plumber","Electrician","Carpenter","Mechanic","Technician",
                "Plumber","Electrician","Carpenter","Mechanic","Technician",
                "Plumber","Electrician","Carpenter","Mechanic","Technician"};

        String Description[] = {
                "Plumber service is available nearby you","Electrician service is available nearby you","Carpenter service is available nearby you","Mechanic service is available nearby you","Technician service is available nearby you",
                "Plumber service is available nearby you","Electrician service is available nearby you","Carpenter service is available nearby you","Mechanic service is available nearby you","Technician service is available nearby you",
                "Plumber service is available nearby you","Electrician service is available nearby you","Carpenter service is available nearby you","Mechanic service is available nearby you","Technician service is available nearby you",
                "Plumber service is available nearby you","Electrician service is available nearby you","Carpenter service is available nearby you","Mechanic service is available nearby you","Technician service is available nearby you",
                "Plumber service is available nearby you","Electrician service is available nearby you","Carpenter service is available nearby you","Mechanic service is available nearby you","Technician service is available nearby you"};
        // generates 20 POIs
        for (int i = 1; i <= 20; i++) {
            final HashMap<String, String> poiInformation = new HashMap<String, String>();
            poiInformation.put(ATTR_ID, String.valueOf(i));
            poiInformation.put(ATTR_NAME, name[i]);
            poiInformation.put(ATTR_DESCRIPTION, Description[i]);
            double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
            poiInformation.put(ATTR_LATITUDE, String.valueOf(poiLocationLatLon[0]));
            poiInformation.put(ATTR_LONGITUDE, String.valueOf(poiLocationLatLon[1]));
            final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
            // Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
            poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
            pois.put(new JSONObject(poiInformation));
        }

        return pois;
    }

    private static double[] getRandomLatLonNearby(final double lat, final double lon) {
        return new double[]{lat + Math.random() / 5 - 0.1, lon + Math.random() / 5 - 0.1};
    }
}

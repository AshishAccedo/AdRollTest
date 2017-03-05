package com.dfpplayer.ima;

import android.text.format.DateUtils;
import android.util.Log;

import com.brightcove.ima.GoogleIMAComponent;
import com.brightcove.ima.GoogleIMAEventType;
import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.mediacontroller.BrightcoveMediaController;
import com.brightcove.player.mediacontroller.BrightcoveSeekBar;
import com.brightcove.player.view.BaseVideoView;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to set up IMA (ads) for the player.
 */
public final class ImaManager {

    private static final String TAG = ImaManager.class.getSimpleName();

    private ImaManager() {
    }

    /**
     * Setup the ima for the player.
     * A video view is sent in which to render to, and the rules url for the ads determines what, when and how the ads should be presented.
     *
     */
    public static void setupIma(final BaseVideoView videoView, final String[] adRulesUrl) {
        BrightcoveMediaController mediaController = new BrightcoveMediaController(videoView);
        videoView.setMediaController(mediaController);

        // Add "Ad Markers" where the Ads Manager says ads will appear.
        AdsManagerLoadedEventListener adsManagerLoadedEventListener = new AdsManagerLoadedEventListener(mediaController);
        mediaController.addListener(GoogleIMAEventType.ADS_MANAGER_LOADED, adsManagerLoadedEventListener);

        // Use a procedural abstraction to setup the Google IMA SDK via the plugin.
        // Establish the Google IMA SDK factory instance.
        ImaSdkFactory sdkFactory = ImaSdkFactory.getInstance();
        EventEmitter eventEmitter = videoView.getEventEmitter();

        // Enable logging up ad start.
        eventEmitter.on(EventType.AD_STARTED, new EventListener() {
            @Override
            public void processEvent(Event event) {
                Log.v(TAG, "################" + event.getType());
            }
        });

        // Enable logging any failed attempts to play an ad.
        eventEmitter.on(GoogleIMAEventType.DID_FAIL_TO_PLAY_AD, new EventListener() {
            @Override
            public void processEvent(Event event) {
                Log.v(TAG, "################" + event.getType());
            }
        });

        // Enable Logging upon ad completion.
        eventEmitter.on(EventType.AD_COMPLETED, new EventListener() {
            @Override
            public void processEvent(Event event) {
                Log.v(TAG, "################" + event.getType());
            }
        });

        // Create the Brightcove IMA Plugin and register the event emitter so that the plugin
        // can deal with video events.
        GoogleIMAComponent googleIMAComponent = new GoogleIMAComponent(videoView, eventEmitter, true);

        AdsRequestForVideoEventListener adsRequestForVideoEventListener = new AdsRequestForVideoEventListener(sdkFactory, googleIMAComponent, videoView, adRulesUrl, eventEmitter);
        eventEmitter.on(GoogleIMAEventType.ADS_REQUEST_FOR_VIDEO, adsRequestForVideoEventListener);
    }

    private static class AdsManagerLoadedEventListener implements EventListener {
        private BrightcoveMediaController mMediaController;

        public AdsManagerLoadedEventListener(BrightcoveMediaController mediaController) {
            mMediaController = mediaController;
        }

        @Override
        public void processEvent(Event event) {
            AdsManager manager = (AdsManager) event.properties.get("adsManager");
            List<Float> cuePoints = manager.getAdCuePoints();
            BrightcoveSeekBar brightcoveSeekBar = mMediaController.getBrightcoveSeekBar();

            for (Float cuePoint : cuePoints) {
                brightcoveSeekBar.addMarker((int) (cuePoint * DateUtils.SECOND_IN_MILLIS));
            }
        }
    }

    private static class AdsRequestForVideoEventListener implements EventListener {
        private ImaSdkFactory mSdkFactory;
        private GoogleIMAComponent mGoogleIMAComponent;
        private BaseVideoView mVideoView;
        private String[] mAdRulesUrl;
        private EventEmitter mEventEmitter;

        public AdsRequestForVideoEventListener(ImaSdkFactory sdkFactory, GoogleIMAComponent googleIMAComponent, BaseVideoView videoView, String[] adRulesUrl, EventEmitter eventEmitter) {
            mSdkFactory = sdkFactory;
            mGoogleIMAComponent = googleIMAComponent;
            mVideoView = videoView;
            mAdRulesUrl = adRulesUrl;
            mEventEmitter = eventEmitter;
        }

        @Override
        public void processEvent(Event event) {

            // Create a container object for the ads to be presented.
            AdDisplayContainer container = mSdkFactory.createAdDisplayContainer();
            container.setPlayer(mGoogleIMAComponent.getVideoAdPlayer());
            container.setAdContainer(mVideoView);

            // Build the list of ad request objects, one per ad, and point each to the ad
            // display container created above.
            ArrayList<AdsRequest> adsRequests = new ArrayList<AdsRequest>(mAdRulesUrl.length);
            for (String adURL : mAdRulesUrl) {
                AdsRequest adsRequest = mSdkFactory.createAdsRequest();
                adsRequest.setAdTagUrl(adURL);
                adsRequest.setAdDisplayContainer(container);
                adsRequests.add(adsRequest);
            }

            // Respond to the event with the new ad requests.
            event.properties.put(GoogleIMAComponent.ADS_REQUESTS, adsRequests);
            mEventEmitter.respond(event);
        }
    }
}
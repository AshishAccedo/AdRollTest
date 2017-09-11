package com.dfpplayer;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightcove.ima.GoogleIMAComponent;
import com.brightcove.ima.GoogleIMAEventType;
import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.media.VideoFields;
import com.brightcove.player.mediacontroller.BrightcoveMediaController;
import com.brightcove.player.model.CuePoint;
import com.brightcove.player.model.Source;
import com.brightcove.player.model.Video;
import com.brightcove.player.util.StringUtil;
import com.brightcove.player.view.BrightcovePlayerFragment;
import com.brightcove.player.view.BrightcoveVideoView;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdsRenderingSettings;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ashish (Min2) on 12/07/17.
 */

public class DfpAdPlayerFragment extends BrightcovePlayerFragment implements EventListener {

    private final String TAG = this.getClass().getSimpleName();

    private EventEmitter eventEmitter;
    private GoogleIMAComponent googleIMAComponent;
    private BrightcoveMediaController mediaController;

    /**
     * Provide a sample illustrative ad.
     */
    private String[] googleAds = {
            //Sony Release Ad
//            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/423477888/SonyLiv/SonyLiv_Pre-Roll_Android&impl=s&gdfp_req=1&ad_rule=1&env=vp&output=vmap&unviewed_position_start=1&url=[referrer_url]&description_url=[description_url]&correlator=1234&cmsid=248"
                //"https://pubads.g.doubleclick.net/gampad/live/ads?sz=640x480&iu=/423477888/SonyLiv_Test/SonyLiv_Pre-Roll_Test&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&url=[referrer_url]&description_url=[description_url]&correlator=[timestamp]&cmsid=248"
//              "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/423477888/SonyLiv/SonyLiv_Pre-Roll_Android&ciu_szs=1x1&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1"
            // Google sample Linear ad
//            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator="
            // google sample pre-mid-post
//            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpost&cmsid=496&vid=short_onecue&correlator="
            // sony VAST URL
//                "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/423477888/SonyLiv/SonyLiv_Pre-Roll_Android&ciu_szs=1x1&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1"
            // Honda Pilot
//            "http://pubads.g.doubleclick.net/gampad/ads?sz=400x300&iu=%2F6062%2Fhanna_MA_group%2Fvideo_comp_app&ciu_szs=&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&m_ast=vast&url=[referrer_url]&correlator=[timestamp]"

            // Google sample Skippable Ad
//                "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dskippablelinear&correlator="

//            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/423477888/SonyLiv_Test/SonyLiv_Pre-Roll_Test&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&url=[referrer_url]&description_url=[description_url]&correlator=[timestamp]"

            "http://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=%2F15018773%2Feverything2&ciu_szs=300x250%2C468x60%2C728x90&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&url=dummy&correlator=[timestamp]&cmsid=133&vid=10XWSh7W4so&ad_rule=1"

            // vast URL(pre, mid and post)
//            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpostpod&cmsid=496&vid=short_onecue&correlator="
            //SonyExternal Profile Ad tag URL
//            "https://pubads.g.doubleclick.net/gampad/live/ads?sz=640x480&iu=/423477888/SonyLiv_Test/SonyLiv_Pre-Roll_Test&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&url=[referrer_url]&description_url=[description_url]&correlator=[timestamp]"
    };
    private ImaSdkFactory sdkFactory;


    public static DfpAdPlayerFragment newInstance() {
        DfpAdPlayerFragment fragment = new DfpAdPlayerFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dfp_player, container, false);
        brightcoveVideoView = (BrightcoveVideoView) view.findViewById(R.id.brightcove_video_view);
        brightcoveVideoView.setClosedCaptioningEnabled(false);

        super.onCreateView(inflater, container, savedInstanceState);

        mediaController = new BrightcoveMediaController(brightcoveVideoView);
        brightcoveVideoView.setMediaController(mediaController);
        eventEmitter = brightcoveVideoView.getEventEmitter();
        setEventEmitters();
        setupGoogleIMA();

        // Remove the HLS_URL field from the catalog request to allow
        // midrolls to work.  Midrolls don't work with HLS due to
        // seeking bugs in the Android OS.
        Map<String, String> options = new HashMap<String, String>();
        List<String> values = new ArrayList<String>(Arrays.asList(VideoFields.DEFAULT_FIELDS));
        values.remove(VideoFields.HLS_URL);
        options.put("video_fields", StringUtil.join(values, ","));

//        Catalog catalog = new Catalog(eventEmitter, accountId, policyId);

        Catalog catalog = new Catalog(eventEmitter, /*"3303963094001"*/getString(R.string.account), /*"BCpkADawqM3zXLtsEM0nAyA_3o3TmZnG6bZTXFmjZ8X_rmFMqlpB78l0aiRELs7MWACf4mYN92qMOLMxfZN6Xr3cQ_0R3G2qBiho3X3Nc2yTv7DH4APQ-EimMJQ3crX0zc0mJMy9CtSqkmli"*/getString(R.string.policy));
        catalog.findVideoByID(getString(R.string.videoId), new VideoListener() {

            // Add the video found to the queue with add().
            // Start playback of the video with start().
            @Override
            public void onVideo(Video video) {
                Log.v(TAG, "onVideo: video = " + video);
                brightcoveVideoView.add(video);
                brightcoveVideoView.start();
            }
        });

        return view;
    }

    private void setEventEmitters() {
        eventEmitter.on(EventType.DID_PLAY, this);
        eventEmitter.on(EventType.PLAY, this);
        eventEmitter.on(EventType.PAUSE, this);
        eventEmitter.on(EventType.DID_PAUSE, this);
        eventEmitter.on(EventType.DID_SEEK_TO, this);
        eventEmitter.on(EventType.CUE_POINT, this);
        eventEmitter.on(EventType.SEEK_TO, this);
        eventEmitter.on(EventType.COMPLETED, this);
        eventEmitter.on(EventType.BUFFERING_STARTED, this);
        eventEmitter.on(EventType.BUFFERING_COMPLETED, this);
        eventEmitter.on(EventType.WILL_INTERRUPT_CONTENT, this);
        eventEmitter.on(EventType.WILL_RESUME_CONTENT, this);
        eventEmitter.on(EventType.ERROR, this);
        eventEmitter.on(EventType.STOP, this);
        eventEmitter.on(EventType.DID_STOP, this);
        eventEmitter.on(EventType.READY_TO_PLAY, this);
        eventEmitter.on(EventType.AD_STARTED, this);
        eventEmitter.on(EventType.AD_COMPLETED, this);
        eventEmitter.on(EventType.SOURCE_NOT_FOUND, this);
        eventEmitter.on(EventType.SOURCE_NOT_PLAYABLE, this);
        eventEmitter.on(EventType.PROGRESS, this);
        eventEmitter.on(EventType.BUFFERED_UPDATE, this);
        eventEmitter.on(EventType.ENTER_FULL_SCREEN, this);
        eventEmitter.on(EventType.EXIT_FULL_SCREEN, this);
        eventEmitter.on(EventType.CAPTION, this);
        eventEmitter.on(EventType.CAPTIONS_AVAILABLE, this);

        eventEmitter.on(EventType.DID_SET_VIDEO, this);
        eventEmitter.on(EventType.DID_SET_SOURCE, this);
        eventEmitter.on(EventType.AD_BREAK_STARTED, this);
        eventEmitter.on(GoogleIMAEventType.ADS_MANAGER_LOADED, this);
        eventEmitter.on(GoogleIMAEventType.ADS_REQUEST_FOR_VIDEO, this);
        eventEmitter.on(GoogleIMAEventType.DID_FAIL_TO_PLAY_AD, this);
        eventEmitter.on(EventType.VIDEO_DURATION_CHANGED, this);
    }


    /**
     * Specify where the ad should interrupt the main video.  This code provides a procedural
     * abastraction for the Google IMA Plugin setup code.
     */
    private void setupCuePoints(Source source) {
//        String cuePointType = "ad";
//        Map<String, Object> properties = new HashMap<String, Object>();
//        Map<String, Object> details = new HashMap<String, Object>();
//
//        // preroll
//        CuePoint cuePoint = new CuePoint(CuePoint.PositionType.BEFORE, cuePointType, properties);
//        details.put(Event.CUE_POINT, cuePoint);
//        eventEmitter.emit(EventType.SET_CUE_POINT, details);

        // midroll at 10 seconds.
        // Due HLS bugs in the Android MediaPlayer, midrolls are not supported.
//        if (!source.getDeliveryType().equals(DeliveryType.HLS)) {
//            int cuepointTime = 10 * (int) DateUtils.SECOND_IN_MILLIS;
//            cuePoint = new CuePoint(cuepointTime, cuePointType, properties);
//            details.put(Event.CUE_POINT, cuePoint);
//            eventEmitter.emit(EventType.SET_CUE_POINT, details);
//            // Add a marker where the ad will be.
//            mediaController.getBrightcoveSeekBar().addMarker(cuepointTime);
//        }

        // postroll
//        cuePoint = new CuePoint(CuePoint.PositionType.AFTER, cuePointType, properties);
//        details.put(Event.CUE_POINT, cuePoint);
//        eventEmitter.emit(EventType.SET_CUE_POINT, details);
    }

    /**
     * Setup the Brightcove IMA Plugin: add some cue points; establish a factory object to
     * obtain the Google IMA SDK instance.
     */
    private void setupGoogleIMA() {

        sdkFactory = ImaSdkFactory.getInstance();

//        // Defer adding cue points until the set video event is triggered.
//        eventEmitter.on(EventType.DID_SET_SOURCE, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                setupCuePoints((Source) event.properties.get(Event.SOURCE));
//            }
//        });
//
//        // Establish the Google IMA SDK factory instance.
//        final ImaSdkFactory sdkFactory = ImaSdkFactory.getInstance();
//
//        // Enable logging of ad starts
//        eventEmitter.on(EventType.AD_STARTED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.v(TAG, event.getType());
//            }
//        });
//
//        // Enable logging of any failed attempts to play an ad.
//        eventEmitter.on(GoogleIMAEventType.DID_FAIL_TO_PLAY_AD, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.v(TAG, event.getType());
//            }
//        });
//
//        // Enable logging of ad completions.
//        eventEmitter.on(EventType.AD_COMPLETED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.v(TAG, event.getType());
//            }
//        });
//
//        // Set up a listener for initializing AdsRequests. The Google IMA plugin emits an ad
//        // request event in response to each cue point event.  The event processor (handler)
//        // illustrates how to play ads back to back.
//        eventEmitter.on(GoogleIMAEventType.ADS_REQUEST_FOR_VIDEO, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                // Create a container object for the ads to be presented.
//                AdDisplayContainer container = sdkFactory.createAdDisplayContainer();
//                container.setPlayer(googleIMAComponent.getVideoAdPlayer());
//                container.setAdContainer(brightcoveVideoView);
//
////                // Populate the container with the companion ad slots.
////                ArrayList<CompanionAdSlot> companionAdSlots = new ArrayList<CompanionAdSlot>();
////                CompanionAdSlot companionAdSlot = sdkFactory.createCompanionAdSlot();
////                ViewGroup adFrame = (ViewGroup) findViewById(R.id.ad_frame);
////                companionAdSlot.setContainer(adFrame);
////                companionAdSlot.setSize(adFrame.getWidth(), adFrame.getHeight());
////                companionAdSlots.add(companionAdSlot);
////                container.setCompanionSlots(companionAdSlots);
//
//                // Build the list of ads request objects, one per ad
//                // URL, and point each to the ad display container
//                // created above.
//                ArrayList<AdsRequest> adsRequests = new ArrayList<AdsRequest>(googleAds.length);
//                for (String adURL : googleAds) {
//                    AdsRequest adsRequest = sdkFactory.createAdsRequest();
//                    adsRequest.setAdTagUrl(adURL);
//                    adsRequest.setAdDisplayContainer(container);
//                    adsRequests.add(adsRequest);
//                }
//
//                // Respond to the event with the new ad requests.
//                event.properties.put(GoogleIMAComponent.ADS_REQUESTS, adsRequests);
//                eventEmitter.respond(event);
//            }
//        });

        final AdsRenderingSettings adsRenderingSettings = sdkFactory.createAdsRenderingSettings();
        adsRenderingSettings.setEnablePreloading(true);

        googleIMAComponent = new GoogleIMAComponent(brightcoveVideoView, eventEmitter, true, adsRenderingSettings);
    }

    @Override
    public void processEvent(Event event) {
        Log.e(TAG, "##############" + event.getType() + " position : "+ brightcoveVideoView.getCurrentPosition());


        if (event.getType().equals(EventType.DID_SET_SOURCE)) {

            setupCuePoints((Source) event.properties.get(Event.SOURCE));

        }else if(event.getType().equals(EventType.AD_STARTED)){
            Log.v(TAG, event.getType());

        }else if(event.getType().equals(GoogleIMAEventType.ADS_REQUEST_FOR_VIDEO)){
            // Create a container object for the ads to be presented.
            AdDisplayContainer container = sdkFactory.createAdDisplayContainer();
            container.setPlayer(googleIMAComponent.getVideoAdPlayer());
            container.setAdContainer(brightcoveVideoView);

//                // Populate the container with the companion ad slots.
//                ArrayList<CompanionAdSlot> companionAdSlots = new ArrayList<CompanionAdSlot>();
//                CompanionAdSlot companionAdSlot = sdkFactory.createCompanionAdSlot();
//                ViewGroup adFrame = (ViewGroup) findViewById(R.id.ad_frame);
//                companionAdSlot.setContainer(adFrame);
//                companionAdSlot.setSize(adFrame.getWidth(), adFrame.getHeight());
//                companionAdSlots.add(companionAdSlot);
//                container.setCompanionSlots(companionAdSlots);

            // Build the list of ads request objects, one per ad
            // URL, and point each to the ad display container
            // created above.
            ArrayList<AdsRequest> adsRequests = new ArrayList<AdsRequest>(googleAds.length);
            for (String adURL : googleAds) {
                AdsRequest adsRequest = sdkFactory.createAdsRequest();
                adsRequest.setAdTagUrl(adURL);
                adsRequest.setAdDisplayContainer(container);
                adsRequests.add(adsRequest);
            }

            // Respond to the event with the new ad requests.
            event.properties.put(GoogleIMAComponent.ADS_REQUESTS, adsRequests);
            eventEmitter.respond(event);
        }
    }
}

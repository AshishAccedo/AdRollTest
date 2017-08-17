package com.dfpplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightcove.ima.GoogleIMAEventType;
import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcovePlayerFragment;
import com.brightcove.player.view.BrightcoveVideoView;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ashish (Min2) on 05/03/17.
 */

public class DfpPlayerFragment extends BrightcovePlayerFragment implements EventListener {

    public static final String TAG = DfpPlayerFragment.class.getSimpleName();

    private Map<String, String> options = new HashMap<String, String>();

//    private String adRulesUrl = "http://pubads.g.doubleclick.net/gampad/ads?sz=640x360&iu=/6062/iab_vast_samples/skippable&ciu_szs=300x250,728x90&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&url=[referrer_url]&correlator=[timestamp]";

//    private String adRulesUrl = "http://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=%2F15018773%2Feverything2&ciu_szs=300x250%2C468x60%2C728x90&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&url=dummy&correlator=[timestamp]&cmsid=133&vid=10XWSh7W4so&ad_rule=1";

    /**
     * Provide a sample illustrative ad.
     */
    private String[] googleAds = {
            // Honda Pilot
//            ""
            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480|1x1&iu=/423477888/SonyLiv/SonyLiv_Pre-Roll_Android&ciu_szs=1x1&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&url=%25%25REFERRER_URL_UNESC%25%25&%25%DESCRIPTION_URL_UNESC%25%25&correlator=%25%CACHEBUSTER%25%25"
//            "http://pubads.g.doubleclick.net/gampad/ads?sz=400x300&iu=%2F6062%2Fhanna_MA_group%2Fvideo_comp_app&ciu_szs=&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&m_ast=vast&url=[referrer_url]&correlator=[timestamp]"
    };
    private EventEmitter eventEmitter;

    public static DfpPlayerFragment newInstance() {
        DfpPlayerFragment fragment = new DfpPlayerFragment();
        return fragment;
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
//        eventEmitter.on(EventType.PROGRESS, this);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dfp_player, container, false);
        brightcoveVideoView = (BrightcoveVideoView) view.findViewById(R.id.brightcove_video_view);
        brightcoveVideoView.setClosedCaptioningEnabled(false);

        super.onCreateView(inflater, container, savedInstanceState);

        eventEmitter = brightcoveVideoView.getEventEmitter();
        setEventEmitters();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupPlayer();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupPlayer() {

        Video video = createVideoFromUrl("https://sonyhdslive-lh.akamaihd.net/i/LIVELINEARCHANNELWAHSD_2@324771/index_800_av-p.m3u8?sd=10&rebase=on&id=AgC2SfL6uBqiV2CYbVmNRn7SaT2yIm38%2fuCq9Hofl2TZ+CiIvJEpeALeOPV8oaOmOIksLbD192S8GQ%3d%3d&hdntl=exp=1500441056~acl=%2fi%2f*~data=hdntl~hmac=39759903dcb4886bd1dd6449d507c579b8af08bc770e65cc27e93d7f6cdb7111");
        brightcoveVideoView.add(video);
        brightcoveVideoView.start();
//        ImaManager.setupIma(brightcoveVideoView, googleAds);

    }

    private Video createVideoFromUrl(final String videoUrl) {
        return Video.createVideo(videoUrl);
    }

    @Override
    public void processEvent(Event event) {
        Log.e(TAG, "####" + event.getType() + " : Seek Pos : " + brightcoveVideoView.getCurrentPosition() + " Live Edge : " + brightcoveVideoView.getVideoDisplay().getLiveEdge());

        if (event.getType().equalsIgnoreCase(EventType.DID_SEEK_TO)) {
            if (event.getIntegerProperty(event.SEEK_POSITION) == 0) {
                brightcoveVideoView.seekTo(1000);
                Log.e(TAG, "#### New Live Edge : " + brightcoveVideoView.getCurrentPosition());
            }
        } else if (event.getType().equalsIgnoreCase(EventType.SEEK_TO)) {
//            if (event.getIntegerProperty(event.SEEK_POSITION) == 0) {
//                brightcoveVideoView.seekTo(1000);
//                Log.e(TAG, "#### New Live Edge : "+brightcoveVideoView.getCurrentPosition());
//            }
        } else if (event.getType().equalsIgnoreCase(EventType.ERROR)) {

//            if ((event.toString().contains("BehindLiveWindowException")) ||
//                    (event.toString().contains("Response code: 403")) ||
//                    (event.toString().contains("Response code: 404")) ||
//                    (event.toString().contains("Response code: 498"))) {
                Log.e(TAG, "#### Exception Handling : " + brightcoveVideoView.getCurrentPosition());
                brightcoveVideoView.seekToLive();
//            }

        } else if (event.getType().equals(EventType.VIDEO_DURATION_CHANGED)) {
            int minPos = event.getIntegerProperty(event.MIN_POSITION);
//            int minPos = (int) event.properties.get("minPosition") + 1000;
            Log.e(TAG, "#### New Min position : " + minPos);
        }
    }
}

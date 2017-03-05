package com.dfpplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcovePlayerFragment;
import com.brightcove.player.view.BrightcoveVideoView;
import com.dfpplayer.ima.ImaManager;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ashish (Min2) on 05/03/17.
 */

public class DfpPlayerFragment extends BrightcovePlayerFragment {

    public static final String TAG = DfpPlayerFragment.class.getSimpleName();

    private Map<String, String> options = new HashMap<String, String>();

//    private String adRulesUrl = "http://pubads.g.doubleclick.net/gampad/ads?sz=640x360&iu=/6062/iab_vast_samples/skippable&ciu_szs=300x250,728x90&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&url=[referrer_url]&correlator=[timestamp]";

//    private String adRulesUrl = "http://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=%2F15018773%2Feverything2&ciu_szs=300x250%2C468x60%2C728x90&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&url=dummy&correlator=[timestamp]&cmsid=133&vid=10XWSh7W4so&ad_rule=1";

    /**
     * Provide a sample illustrative ad.
     */
    private String[] googleAds = {
            // Honda Pilot
            "http://pubads.g.doubleclick.net/gampad/ads?sz=400x300&iu=%2F6062%2Fhanna_MA_group%2Fvideo_comp_app&ciu_szs=&impl=s&gdfp_req=1&env=vp&output=xml_vast2&unviewed_position_start=1&m_ast=vast&url=[referrer_url]&correlator=[timestamp]"
    };

    public static DfpPlayerFragment newInstance() {
        DfpPlayerFragment fragment = new DfpPlayerFragment();
        return fragment;
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


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        brightcoveVideoView = (BrightcoveVideoView) view.findViewById(R.id.brightcove_video_view);

        setupPlayer();

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupPlayer() {


//        final Video video = createVideoFromUrl(videoUrl);

//        Catalog catalog = new Catalog("ErQk9zUeDVLIp8Dc7aiHKq8hDMgkv5BFU7WGshTc-hpziB3BuYh28A..");

        Catalog catalog = new Catalog(brightcoveVideoView.getEventEmitter(), "3303963094001"/*getString(R.string.account)*/, "BCpkADawqM3zXLtsEM0nAyA_3o3TmZnG6bZTXFmjZ8X_rmFMqlpB78l0aiRELs7MWACf4mYN92qMOLMxfZN6Xr3cQ_0R3G2qBiho3X3Nc2yTv7DH4APQ-EimMJQ3crX0zc0mJMy9CtSqkmli"/*getString(R.string.policy)*/);

        catalog.findVideoByID("4283173439001"/*getString(R.string.videoId)*/, new VideoListener() {

            // Add the video found to the queue with add().
            // Start playback of the video with start().
            @Override
            public void onVideo(Video video) {
                Log.v(TAG, "onVideo: video = " + video);
                brightcoveVideoView.add(video);
                brightcoveVideoView.start();
            }
        });
        ImaManager.setupIma(brightcoveVideoView, googleAds);

    }

    private Video createVideoFromUrl(final String videoUrl) {
        return Video.createVideo(videoUrl);
    }

}

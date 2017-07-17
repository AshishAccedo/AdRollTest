package com.dfpplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DfpAdPlayerFragment mDfpPLayerFragment;
    private static final String FRAGMENT_TAG = "DfpPlayer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDfpPlayerFragment();
    }


    private void initDfpPlayerFragment() {
        if (mDfpPLayerFragment != null) {
            return;
        }
        mDfpPLayerFragment = DfpAdPlayerFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.dfp_player_frag_container, mDfpPLayerFragment, FRAGMENT_TAG)
                .commit();
    }
}

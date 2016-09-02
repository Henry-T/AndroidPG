package com.lolofinil.tbnrg.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Henry on 3/31/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}

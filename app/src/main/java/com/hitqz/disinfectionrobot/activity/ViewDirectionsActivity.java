package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.ActivityViewDirectionsBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.fragment.MapFragment;

/**
 * 查看路线
 */
@SuppressLint("CheckResult")
public class ViewDirectionsActivity extends BaseActivity {

    ActivityViewDirectionsBinding mBinding;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewDirectionsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mMapFragment);
        } else {
            fragmentTransaction.show(mMapFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        CommonDialog dialog = new CommonDialog();
        dialog.setOnClickListener(new CommonDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), dialog.getTag());
    }
}

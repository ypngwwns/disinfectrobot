package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hitqz.disinfectionrobot.DisinfectRobotApplication;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.constant.Constants;
import com.hitqz.disinfectionrobot.data.RobotStatus;
import com.hitqz.disinfectionrobot.databinding.ActivityViewDirectionsBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.fragment.MapFragment;
import com.sonicers.commonlib.singleton.GsonUtil;

/**
 * 查看路线
 */
@SuppressLint("CheckResult")
public class ViewDirectionsActivity extends BaseActivity {
    public static final String TAG = ViewDirectionsActivity.class.getSimpleName();

    ActivityViewDirectionsBinding mBinding;
    private MapFragment mMapFragment;
    private WebSocketMessageReceiver mWebSocketMessageReceiver;

    private static class WebSocketMessageReceiver extends BroadcastReceiver {
        private ViewDirectionsActivity mActivity;

        public WebSocketMessageReceiver(ViewDirectionsActivity activity) {
            mActivity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d(TAG, "收到：" + message);
            RobotStatus websocketBean = GsonUtil.getInstance().fromJson(message, RobotStatus.class);
            if (websocketBean == null || websocketBean.getLaserData() == null) {
                return;
            }

            if(mActivity.mMapFragment!= null){
                mActivity.mMapFragment.refresh(websocketBean);
            }
        }
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        mWebSocketMessageReceiver = new WebSocketMessageReceiver(this);
        IntentFilter filter = new IntentFilter(Constants.WEB_SOCKET_ACTION);
        registerReceiver(mWebSocketMessageReceiver, filter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewDirectionsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        DisinfectRobotApplication.instance.jWebSClientService.sendMsg("{\"topic\": \"ROBOT_STATUS_GET\"}");

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
        doRegisterReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWebSocketMessageReceiver);
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

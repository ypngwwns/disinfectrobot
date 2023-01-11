package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hitqz.disinfectionrobot.DisinfectRobotApplication;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.RobotStatus;
import com.hitqz.disinfectionrobot.databinding.ActivityViewDirectionsBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.fragment.MapFragment;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;
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

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        mWebSocketMessageReceiver = new WebSocketMessageReceiver(this);
        DisinfectRobotApplication.instance.addWebSocketCallback(mWebSocketMessageReceiver);
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
        DisinfectRobotApplication.instance.removeWebSocketCallback(mWebSocketMessageReceiver);
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

    private static class WebSocketMessageReceiver implements JWebSocketClientService.WebSocketCallback {
        private ViewDirectionsActivity mActivity;

        public WebSocketMessageReceiver(ViewDirectionsActivity activity) {
            mActivity = activity;
        }

        @Override
        public void onMessage(String message) {
            RobotStatus websocketBean = GsonUtil.getInstance().fromJson(message, RobotStatus.class);
            if (websocketBean == null || websocketBean.getLaserData() == null) {
                return;
            }

            if (mActivity.mMapFragment != null) {
                mActivity.mMapFragment.refresh(websocketBean);
            }
        }

        @Override
        public void onConnectSuccess(String s) {

        }
    }
}

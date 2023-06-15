package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.data.SaveUtil;
import com.hitqz.disinfectionrobot.databinding.ActivityEditMapBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.util.AssetBitmapLoader;
import com.hitqz.disinfectionrobot.widget.EditMapView;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressLint("CheckResult")
public class EditMapActivity extends BaseActivity {
    public static final String TAG = EditMapActivity.class.getSimpleName();
    private ActivityEditMapBinding mBinding;
    private byte[] mCurrMapData;
    private String mCurrMapName = "5GCleaner01_map5f0522.png";
//    private MapData mMapData;

    private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bytesArray;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEditMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setListener();
        //默认缩放模式
        touchMode(true);

        if (!TextUtils.isEmpty(mCurrMapName)) {
            mBinding.mapnameShowTv.setText(mCurrMapName);
        }
        Bitmap bitmap = AssetBitmapLoader.loadBitmapFromAsset(this, mCurrMapName);
        mBinding.editMapView.post(new Runnable() {
            @Override
            public void run() {
                mBinding.editMapView.setMap(bitmap);
            }
        });
        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initMap(String mapCode) throws IOException {
//        mCurrMapData = mNavconnectorImp.getMap2D(mapCode);
//        if (mCurrMapData != null) {
//            Log.d(TAG, "当前地图数据:" + mCurrMapData.length);
//            String savePath = PathUtil.getMapCompressFilePath(getApplicationContext(), mapCode);
//            boolean result = FileUtil.save2Path(mCurrMapData, savePath);
//            Log.d(TAG, "保存结果:" + result);
//            TARGZ.deCompress(savePath, PathUtil.getMapDeCompressFile(getApplicationContext(), mapCode));
//            mMapData = new MapData(PathUtil.getMapPGMFile(getApplicationContext(), mapCode),
//                    PathUtil.getMapYmlFile(getApplicationContext(), mapCode));
//        }
    }

    private void setListener() {
        mBinding.editMapMenu.editMapTouchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchMode(true);
            }
        });

        mBinding.editMapMenu.editMapEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchMode(false);
            }
        });
        mBinding.editMapView.setLineType(EditMapView.PaintType.LINE);
        mBinding.editMapMenu.editMapLineRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBinding.editMapView.setStroke(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.editMapMenu.editMapUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.editMapView.undo();
            }
        });
        mBinding.editMapMenu.editMapRedoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.editMapView.redo();
            }
        });
        mBinding.editMapMenu.editMapSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBinding.editMapView.operated()) {
                    return;
                }
                CommonDialog dialog = new CommonDialog();
                dialog.setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        saveAndUpload();
                    }
                });
                dialog.show(getSupportFragmentManager(), dialog.TAG);
            }
        });
    }

    private void createFileWithByte(byte[] bytes, File file) {
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private byte[] createPGMFile(byte[] pixs, int width, int heigth) {
        StringBuilder builder = new StringBuilder();

        // get pgm header
        builder.append("P5\n");
        builder.append("# CREATE BY hitqz\n");
        builder.append(String.valueOf(width));
        builder.append(" ");
        builder.append(String.valueOf(heigth));
        builder.append("\n");
        builder.append("255\n");
        byte[] headerBytes = builder.toString().getBytes();

        // get colors
        byte[] data = new byte[pixs.length / 4];
        int step = 4;
        int j = 0;
        for (int i = 0; i < pixs.length - step; i += step) {
            data[j] = pixs[i];
            j++;
        }

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        BufferedOutputStream buffered = new BufferedOutputStream(byteOut);
        try {
            buffered.write(headerBytes);
            buffered.write(data);
            buffered.flush();
        } catch (IOException e) {
            Log.w(TAG, "生成PGM文件出错", e);
        }

        return byteOut.toByteArray();
    }

    private void touchMode(boolean isTouchMode) {
        if (isTouchMode) {
            mBinding.editMapView.setMode(false);
            mBinding.editMapMenu.editMapTouchMode.setEnabled(false);
            mBinding.editMapMenu.editMapEditMode.setEnabled(true);
            setEditButtonEnable(false);
        } else {
            mBinding.editMapView.setMode(true);
            mBinding.editMapMenu.editMapTouchMode.setEnabled(true);
            mBinding.editMapMenu.editMapEditMode.setEnabled(false);
            setEditButtonEnable(true);
            mBinding.editMapView.setLineColor(Color.BLACK);
            mBinding.editMapView.setDrawType(EditMapView.DrawType.DRAW);
        }
    }

    private void setEditButtonEnable(boolean enable) {
        mBinding.editMapMenu.editMapLineRadius.setEnabled(enable);
        mBinding.editMapMenu.editMapRedoButton.setEnabled(enable);
        mBinding.editMapMenu.editMapUndoButton.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.editMapView.operated()) {
            CommonDialog dialog = new CommonDialog();
            dialog.setOnClickListener(new CommonDialog.OnClickListener() {
                @Override
                public void onConfirm() {
                    finish();
                }
            });
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        } else {
            super.onBackPressed();
        }
    }

    private void saveAndUpload() {
        Observable
                .fromCallable(() -> SaveUtil.saveBitmapToStorage(this, mBinding.editMapView.getMap(), "map"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String filePath) {
                        if (TextUtils.isEmpty(filePath)) {
                            ToastUtils.showShort("保存失败");
                        } else {
                            File file = new File(filePath);
                            // 创建RequestBody并指定文件类型
                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                            // 创建MultipartBody.Part以便添加到请求
                            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                            mISkyNet.updateRobotMap(filePart)
                                    .compose(RxSchedulers.io_main())
                                    .subscribeWith(new BaseDataObserver<Object>() {
                                        @Override
                                        public void onSuccess(Object model) {
                                            mChassisManager.mMapDataResponse = null;
                                            dismissDialog();
                                            ToastUtils.showShort("上传成功");
                                        }

                                        @Override
                                        public void onFailure(String msg) {
                                            ToastUtils.showShort("上传失败");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.databinding.ActivityEditMapBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.SaveMapDialog;
import com.hitqz.disinfectionrobot.util.AssetBitmapLoader;
import com.hitqz.disinfectionrobot.widget.EditMapView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import me.jessyan.autosize.AutoSize;

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
        AutoSize.autoConvertDensity(this, 960f, true);
        mBinding = ActivityEditMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setListener();
        //默认缩放模式
        touchMode(true);

        if (!TextUtils.isEmpty(mCurrMapName)) {
            mBinding.mapnameShowTv.setText(mCurrMapName);
        }
        Bitmap bitmap = AssetBitmapLoader.loadBitmapFromAsset(this, mCurrMapName);
        mBinding.editMapView.setMap(bitmap);
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
        mBinding.editMapMenu.editMapLoadButton.setOnClickListener((v) -> {
//            showDialog();
//            Observable
//                    .fromCallable(() -> mNavconnectorImp.getMapNameList())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe((result) -> {
//                        if (result != null) {//成功
//                            if (result.size() == 0) {
//                                ToastUtils.showShort("获取地图列表为空");
//                            } else {
//                                MapListDialog dialog = new MapListDialog();
//                                dialog.setList(result);
//                                dialog.setOnClickListener(new MapListDialog.OnClickListener() {
//                                    @Override
//                                    public void onConfirm(String text) {
//                                        showDialog();
//                                        Observable.fromCallable(() -> {
//                                                    mCurrMapName = text;
//                                                    initMap(mCurrMapName);
//                                                    return true;
//                                                })
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe((result) -> {
//                                                    if (!TextUtils.isEmpty(mCurrMapName)) {
//                                                        mBinding.mapnameShowTv.setText(mCurrMapName);
//                                                    }
//                                                    mBinding.editMapView.setMap(mMapData.bitmap);
//                                                    dismissDialog();
//                                                });
//                                    }
//                                });
//                                dialog.show(getSupportFragmentManager(), dialog.getTag());
//                            }
//                        } else {
//                            ToastUtils.showShort("获取地图列表失败");
//                        }
//
//                        dismissDialog();
//                    });
        });
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

        mBinding.editMapMenu.editMapDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog();
//                Observable
//                        .fromCallable(() -> mNavconnectorImp.getMapNameList())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe((result) -> {
//                            if (result != null) {//成功
//                                if (result.size() == 0) {
//                                    ToastUtils.showShort("获取地图列表为空");
//                                } else {
//                                    MapListDialog dialog = new MapListDialog();
//                                    dialog.setList(result);
//                                    dialog.setOnClickListener(new MapListDialog.OnClickListener() {
//                                        @Override
//                                        public void onConfirm(String text) {
//                                            showDialog();
//                                            Observable
//                                                    .fromCallable(() -> {
//                                                        List<String> list = new ArrayList<>();
//                                                        list.add(text);
//                                                        return mNavconnectorImp.deleteMap2D(list);
//                                                    })
//                                                    .subscribeOn(Schedulers.io())
//                                                    .observeOn(AndroidSchedulers.mainThread())
//                                                    .subscribe((result) -> {
//                                                        if (result == 0) {//成功
//                                                            ToastUtils.showShort("删除地图成功");
//                                                        } else {
//                                                            ToastUtils.showShort("删除地图失败");
//                                                        }
//                                                        dismissDialog();
//                                                    });
//                                        }
//                                    });
//                                    dialog.show(getSupportFragmentManager(), dialog.getTag());
//                                }
//                            } else {
//                                ToastUtils.showShort("获取地图列表失败");
//                            }
//
//                            dismissDialog();
//                        });
            }
        });
        mBinding.editMapMenu.editMapColorBlackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.editMapMenu.editMapColorBlackButton.setEnabled(false);
                mBinding.editMapMenu.editMapColorWhiteButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorGrayButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorEraseButton.setEnabled(true);
                mBinding.editMapView.setLineColor(Color.BLACK);
                mBinding.editMapView.setDrawType(EditMapView.DrawType.DRAW);
            }
        });
        mBinding.editMapMenu.editMapColorGrayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.editMapMenu.editMapColorGrayButton.setEnabled(false);
                mBinding.editMapMenu.editMapColorBlackButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorWhiteButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorEraseButton.setEnabled(true);
                mBinding.editMapView.setLineColor(Color.rgb(205, 205, 205));
                mBinding.editMapView.setDrawType(EditMapView.DrawType.DRAW);
            }
        });
        mBinding.editMapMenu.editMapColorWhiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.editMapMenu.editMapColorWhiteButton.setEnabled(false);
                mBinding.editMapMenu.editMapColorGrayButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorBlackButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorEraseButton.setEnabled(true);
                mBinding.editMapView.setLineColor(Color.WHITE);
                mBinding.editMapView.setDrawType(EditMapView.DrawType.WHITE);
            }
        });
        mBinding.editMapMenu.editMapColorEraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.editMapMenu.editMapColorEraseButton.setEnabled(false);
                mBinding.editMapMenu.editMapColorBlackButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorGrayButton.setEnabled(true);
                mBinding.editMapMenu.editMapColorWhiteButton.setEnabled(true);

                mBinding.editMapView.setLineColor(Color.WHITE);
                mBinding.editMapView.setDrawType(EditMapView.DrawType.ERASE);
            }
        });
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
        mBinding.editMapMenu.editMapRecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recover();
            }
        });
        mBinding.editMapMenu.editMapSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMapDialog dialog = new SaveMapDialog();
                dialog.setOnClickListener(text -> {
//                    showDialog();
//                    Observable
//                            .fromCallable(() -> {
//                                //创建新地图文件夹
//                                File newFileDir = PathUtil.getMapDeCompressFile(getApplicationContext(), text);
//
//                                if (newFileDir.exists()) {
//                                    String yamlFilePath = PathUtil.getMapYmlFile(getApplicationContext(), mCurrMapName);
//                                    Map<String, Object> map = YmlUtil.parseYaml(yamlFilePath);
//                                    String rawPgmPath = (String) map.get("image");
//                                    //map.put("image", "/home/nav/protocol/devel/lib/navconnector/.data/map2d/" + text + ".pgm");
//                                    int index = rawPgmPath.indexOf("map2d/");
//                                    String newRawPgmPath = rawPgmPath.substring(0, index) + "map2d/" + text + ".pgm";
//                                    map.put("image", newRawPgmPath);
//                                    String newYamlFilePath = PathUtil.getMapYmlFile(getApplicationContext(), text);
//                                    Yaml yml = new Yaml();
//                                    FileWriter writer = new FileWriter(newYamlFilePath, true);
//                                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
//                                    bufferedWriter.newLine();
//                                    yml.dump(map, bufferedWriter);
//                                    bufferedWriter.close();
//                                    writer.close();
//
//                                    File newYamlFile = new File(newYamlFilePath);
//                                    if (newYamlFile.exists()) {
//                                        Bitmap whiteBgBitmap = mBinding.editMapView.getMap();//获取新图
//                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                        whiteBgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                                        byte[] mapBytes = baos.toByteArray();
//                                        BitmapFactory.Options options = new BitmapFactory.Options();
//                                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                                        Bitmap bitmap = BitmapFactory.decodeByteArray(mapBytes, 0, mapBytes.length, options);
//                                        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
//                                        bitmap.copyPixelsToBuffer(buffer);
//                                        byte[] bytes = buffer.array();
//                                        byte[] pgmBytes = createPGMFile(bytes, bitmap.getWidth(), bitmap.getHeight());
////                                        byte[] pgm = CompressUtil.compress(pgmBytes);
//
//                                        File newPgmFile = new File(PathUtil.getMapPGMFile(getApplicationContext(), text));
//                                        createFileWithByte(pgmBytes, newPgmFile);
//
//                                        TARGZ.compress(PathUtil.getMapCompressFilePath(getApplicationContext(), text), newYamlFile, newPgmFile);
//
//                                        byte[] targzBytes = readBytesFromFile(PathUtil.getMapCompressFilePath(getApplicationContext(), text));
//                                        return mNavconnectorImp.setMap2D(targzBytes);
//                                    }
//                                }
//
//                                return -1;
//                            })
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe((result) -> {
//                                if (result == 0) {//成功
//                                    ToastUtils.showShort("保存地图成功");
//                                } else {
//                                    ToastUtils.showShort("保存地图失败");
//                                }
//
//                                dismissDialog();
//                            });
                });
                dialog.show(getSupportFragmentManager(), dialog.getTag());
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
            mBinding.editMapMenu.editMapColorBlackButton.setEnabled(false);
        }
    }

    private void setEditButtonEnable(boolean enable) {
        mBinding.editMapMenu.editMapColorBlackButton.setEnabled(enable);
        mBinding.editMapMenu.editMapColorGrayButton.setEnabled(enable);
        mBinding.editMapMenu.editMapColorWhiteButton.setEnabled(enable);
        mBinding.editMapMenu.editMapColorEraseButton.setEnabled(enable);
        mBinding.editMapMenu.editMapLineRadius.setEnabled(enable);
        mBinding.editMapMenu.editMapRecoverButton.setEnabled(enable);
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
}

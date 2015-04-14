package com.example.smyconan.megamind;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.smyconan.megamind.R;
import com.qq.wx.img.imgsearcher.ImgListener;
import com.qq.wx.img.imgsearcher.ImgResult;
import com.qq.wx.img.imgsearcher.ImgSearcher;
import com.qq.wx.img.imgsearcher.ImgSearcherState;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends Activity implements ImgListener {

    final static String TAG = "MainActivity";
    //主页的组件变量
    private static final String screKey = "248b63f1ceca9158ca88516bcb338e82a482ecd802cbca12";
    int mInitSucc = 0;
    //以下是首页面的组件变量
    private Button mCameraBtn;
    private Button mAlbumBtn;
    //结果页
    private Button mCancelBtn;
    private TextView mTextView;
    //参数变量
    final int TAKE_PICTURE = 1;
    final int FROM_ALBUM = 2;
    private Bitmap bm = null;
    private String imgFileName = null;
    //关于结果
    private String mResUrl;
    private String mResMD5;
    private String mResPicDesc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle savedInstanceState是前一个Activity传递来的消息；
        if (null != savedInstanceState) {
            //不等于null，表示有消息传递过来，这个消息中包含元组（属性名称(String类型),属性值(File路径类型)）；
            imgFileName = savedInstanceState.getString("imgFileName");
        }
        initMainUI();
        preInitImg();
    }
    //**********************************************************************************************
    private void preInitImg() {
        ImgSearcher.shareInstance().setListener(this);
        mInitSucc = ImgSearcher.shareInstance().init(this, screKey);
        if (mInitSucc != 0) {
            Toast.makeText(this, "初始化失败 ", Toast.LENGTH_SHORT).show();
        }
    }
    //******************程序首页UI布局函数：initMainUI()***********************************************
    private void initMainUI() {
        /*关联首页的布局文件：*/setContentView(R.layout.img_demo);
        /*关联Camera启动按钮：*/mCameraBtn = (Button) findViewById(R.id.camera_start);
        /*关联Album打开按钮：*/mAlbumBtn = (Button) findViewById(R.id.album);

        //_________________________________________________________________________________
        /*Camera按钮的Click操作：*/mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*照相机拍摄照片所存储的目录:*/String filepath = null;
                /*查找手机SD卡是否存在，将结果保存到sdCardExist中：*/boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                /*SD卡不存在，写手机内部存储；*/if (!sdCardExist) {
                                                    @SuppressWarnings("deprecation")
                                                    File mediaFilesDir = getDir("mediaFiles", Context.MODE_WORLD_READABLE);
                                                    filepath = mediaFilesDir.getPath();
                                            }
                /*SD卡存在，将照片写入SD卡；*/ else{
                                                    filepath = Environment.getExternalStorageDirectory().getPath() + "/Megamind/mm";//在SD卡上新建的一个目录；
                                                    File outputpath = new File(filepath);
                                                    if (!outputpath.exists()) { outputpath.mkdirs(); }//若该目录不存在，则创建该目录；
                                            }

                //*******以当时时间来命名jpg文件****************
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String dateStr = dateFormat.format(date);
                String imgType = ".jpg";
                imgFileName = filepath + "/" + dateStr + imgType;
                File imgFile = new File(imgFileName);
                //*******************************************

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = Uri.fromFile(imgFile);
                //指定照片保存路径（SD卡），workupload.jpg为一个临时文件，每次拍照后这个图片都会被替换
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraIntent.putExtra("imgFilename", imgFileName);
                /*启动相机，请求一张照片的返回*/startActivityForResult(cameraIntent, TAKE_PICTURE);
            }
        });

        //__________________________________________________________________________________
        /*Album按钮的Click操作：*/mAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, FROM_ALBUM);
            }
        });
        mTextView = (TextView) findViewById(R.id.start_searching);
    }

    @SuppressWarnings("deprecation")
    private void initResultUI() {
        setContentView(R.layout.search_demo);

        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.staff);

        Drawable mDrawable = new BitmapDrawable(getResources(), bm);
        mLinearLayout.setBackgroundDrawable(mDrawable);
        mCancelBtn = (Button) findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int ret = ImgSearcher.shareInstance().cancel();
                if (0 != ret) {
                    initMainUI();
                }
            }
        });

        byte [] imgByte = getJpg(bm);
        int ret = startImgSearching(imgByte);
        if (0 != ret) {
            initMainUI();
        }
    }
    private int startImgSearching(byte[] img) {
        if (mInitSucc != 0) {
            mInitSucc = ImgSearcher.shareInstance().init(this, screKey);
        }
        if (mInitSucc != 0) {
            Toast.makeText(this, "初始化失败lala!!!",
                    Toast.LENGTH_SHORT).show();
            return -1;
        }

        int ret = ImgSearcher.shareInstance().start(img);
        if (0 == ret) {
            return 0;
        }
        else {
            Toast.makeText(this, "ErrorCode = " + ret, Toast.LENGTH_LONG).show();;
            return -1;
        }
    }
    @Override
    public void onGetError(int errorCode) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "ErrorCode = " + errorCode, Toast.LENGTH_LONG).show();
        initMainUI();
    }
    @Override
    public void onGetResult(ImgResult result) {
        // TODO Auto-generated method stub
        boolean ret = true;
        if (result != null) {
            if (1 == result.ret && result.res != null) {
                int resSize = result.res.size();
                for (int i = 0; i < resSize; ++i) {
                    ImgResult.Result res = (ImgResult.Result) result.res.get(i);
                    if (res != null) {
                        mResMD5 = res.md5;
                        mResUrl = res.url;
                        mResPicDesc = res.picDesc;
                    }
                }
                ret = true;
            }
            else {
                ret = false;
            }
        }
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        turnToResultActivity(ret);
    }
    public void turnToResultActivity(boolean isFound) {

        Intent it = new Intent(this, ResultActivity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("ret", isFound);
        bundle.putString("url", mResUrl);
        bundle.putString("md5", mResMD5);
        bundle.putString("picDesc", mResPicDesc);
        it.putExtras(bundle);       // it.putExtra(“test”, "shuju”);
        startActivity(it);
        /*
        Intent it = new Intent(MainActivity.this, WebActivity.class);
        startActivity(it);
        */
        finish();
    }
    @Override
    public void onGetState(ImgSearcherState state) {
        if (ImgSearcherState.Canceling == state) {
            mTextView.setText("正在取消…");
        }
        else if (ImgSearcherState.Canceled == state) {
            initMainUI();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("imgFileName", imgFileName);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){//result is not correct
            return;
        }else{

            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
            if (TAKE_PICTURE == requestCode) {
                bm = PicShrink.compress(imgFileName);
                File file = new File(imgFileName);
                file.delete();
            }
            else {
                Uri uri = data.getData();
                if (null != uri) {
                    bm = PicShrink.compress(this, uri);
                    if (null == bm) {
                        return;
                    }
                }
                else {
                    return;
                }
            }
            initResultUI();
        }
    }
    public void savePic(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File filepath = new File(root + "/Megamind/mm");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = dateFormat.format(date);
        String imgType = ".jpg";
        String fileName = dateStr + imgType;

        File file = new File(filepath, fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public byte[] getJpg(Bitmap bitmap) {
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outs);

        return outs.toByteArray();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
            // 监控返回键
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != bm && !bm.isRecycled()) {
            bm.recycle();
        }
        ImgSearcher.shareInstance().destroy();
    }
}

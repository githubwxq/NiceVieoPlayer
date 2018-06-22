package com.xiao.nicevieoplayer.example;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxMusicPlayerController;
import com.xiao.nicevideoplayer.TxVideoPlayerController;
import com.xiao.nicevieoplayer.R;
import com.xiao.nicevieoplayer.example.util.VideoInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TinyWindowPlayActivity extends AppCompatActivity {

    private NiceVideoPlayer mNiceVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiny_window_play);
        init();
    }

    private void init() {
        mNiceVideoPlayer = (NiceVideoPlayer) findViewById(R.id.nice_video_player);
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_NATIVE); // IjkPlayer or MediaPlayer
//        String videoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
//        String videoUrl = "http://up.mcyt.net/?down/47870.mp3";

//        String uri = "android.resource://" + getPackageName() + "/" + R.raw.video;
//        String videoUrl=Environment.getExternalStorageDirectory().getAbsolutePath()+"/test/test_shu.mp4";
//        String picpath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/test/test.jpg";


        String videoUrl = "android.resource://" + getPackageName() + "/" + R.raw.test_shu;
//        String videoUrl= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test_heng).toString()   ;
//        String videoUrl= "file:///android_asset/test_shu.mp4";;
        String picpath="android.resource://" + getPackageName() + "/" + R.raw.test2;;


        AssetManager assetManager = getAssets();

        InputStream is = null;
        try {
            is = getAssets().open("area2.txt");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        String rel = baos.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }


        initVideoData();

//        videoUrl=vList.get(0).getFilePath();


//        String path2 = "file:///sdcard/../文件.*";

//        videoUrl = Environment.getExternalStorageDirectory().getPath().concat("/办公室小野.mp4");
        mNiceVideoPlayer.setUp(videoUrl, null);
        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        controller.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？");
        controller.setLenght(98000);
        Glide.with(this)
                .load(picpath)
                .placeholder(R.drawable.img_default)
                .crossFade()
                .into(controller.imageView());
        mNiceVideoPlayer.setController(controller);
    }
    ArrayList<VideoInfo> vList;
    private void initVideoData() {

        vList = new ArrayList<VideoInfo>();
        String[] mediaColumns = new String[]{MediaStore.MediaColumns.DATA,
                BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DURATION, MediaStore.MediaColumns.SIZE};
        Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                VideoInfo info = new VideoInfo();
                info.setFilePath(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                info.setMimeType(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)));
                info.setTitle(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
//                info.setTime(CommTools.LongToHms(cursor.getInt(cursor
//                        .getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))));
//                info.setSize(CommTools.LongToPoint(cursor
//                        .getLong(cursor
//                                .getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))));
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(BaseColumns._ID));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                info.setB(MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id,
//                        MediaStore.Images.Thumbnails.MICRO_KIND, options));
                vList.add(info);

            } while (cursor.moveToNext());
        }

    }

    public void enterTinyWindow(View view) {
        if (mNiceVideoPlayer.isIdle()) {
            Toast.makeText(this, "要点击播放后才能进入小窗口", Toast.LENGTH_SHORT).show();
        } else {
            mNiceVideoPlayer.enterTinyWindow();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}

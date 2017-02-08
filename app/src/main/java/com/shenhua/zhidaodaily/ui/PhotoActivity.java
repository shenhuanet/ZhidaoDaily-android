package com.shenhua.zhidaodaily.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.utils.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

import static com.bumptech.glide.Glide.with;

/**
 * Created by shenhua on 12/16/2016.
 * Email shenhuanet@126.com
 */
public class PhotoActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.photoView)
    PhotoView mPhotoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        setupActionBar(toolbar, "", true);
        final String url = getIntent().getStringExtra("imgUrl");
        with(this).load(url).into(mPhotoView);
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                savePic(url);
                return true;
            }
        });
    }

    private void savePic(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(PhotoActivity.this).load(url).asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    if (bitmap != null) {
                        AppUtils.saveBitmapToSDCard(PhotoActivity.this, bitmap, AppUtils.getFileName(url), "zhidao_daily", true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PhotoActivity.this, "图片已保存到手机", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PhotoActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}

package com.healthclock.healthclock.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.other.VideoModel;
import com.healthclock.healthclock.util.ImageLoaderUtils;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

public class ViedeoAdapter extends BaseQuickAdapter<VideoModel.ListBean,BaseViewHolder>{
    private StandardGSYVideoPlayer detailPlayer;
    private OrientationUtils orientationUtils;
    private boolean isPlay;


    public ViedeoAdapter(int layoutResId, @Nullable List<VideoModel.ListBean> data) {
        super(R.layout.video_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoModel.ListBean item) {
        detailPlayer =  helper.getView(R.id.detail_player);
        helper.setText(R.id.tv_title,"谁能吃我一箭，穿心的，疼不疼，太搞笑了！");
        helper.setText(R.id.tv_time,"时长：01:04"+item.getDuration());
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
        orientationUtils = new OrientationUtils((Activity) mContext, detailPlayer);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        Glide.with(mContext).load(data.getParams().getPoster())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(R.mipmap.default_pic)
//                .error(R.mipmap.default_pic)
//                .centerCrop().override(1090, 1090*3/4)
//                .crossFade().into(imageView);
        String poster="https://timgmb01.bdimg.com/timg?searchbox_feed&quality=100&wh_rate=0&size=b800_1000&ref=http%3A%2F%2Fwww.baidu.com&sec=1557636359&di=25fde91bf1ef19a5a124953bf31aa0c3&src=http%3A%2F%2Fpic.rmb.bdstatic.com%2Fa7d8b4f8dcc4fe67444c0f0395bb73c5.jpeg";
        ImageLoaderUtils imageLoaderUtils=ImageLoaderUtils.getInstance(mContext);
        imageLoaderUtils.displayImage(poster,imageView);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
       String url="https://vd2.bdstatic.com/mda-jcsqcu8urm3v3rnh/sc/mda-jcsqcu8urm3v3rnh.mp4?auth_key=1557637135-0-0-4ac065b29d20eeb24fdc7b058481e232&amp;bcevod_channel=searchbox_feed&amp;pd=unknown&amp;abtest=all";
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle("")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                }).setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        }).build(detailPlayer);

        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(mContext, true, true);
            }
        });
    }
}

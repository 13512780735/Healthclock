package com.healthclock.healthclock.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.healthclock.healthclock.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * @author: qndroid
 * @function: 初始化UniverImageLoader, 并用来加载网络图片
 * @date: 16/6/27
 */
public class ImageLoaderUtils {
    private static final int THREAD_COUNT = 3;
    private static final int PRIORITY = 1;
    private static final int MEMORY_CACHE_SIZE = 2 * 1024 * 1024;
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int CONNECTION_TIME_OUT = 5 * 1000;
    private static final int READ_TIME_OUT = 30 * 1000;

    private static ImageLoaderUtils mInstance = null;
    private static ImageLoader mLoader = null;


    public static ImageLoaderUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 私有构造方法完成初始化工作
     *
     * @param context
     */
    private ImageLoaderUtils(Context context) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(THREAD_COUNT)
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE))
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(DISK_CACHE_SIZE)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(getDefaultOptions())
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
        mLoader = ImageLoader.getInstance();
    }

    //load the image
    public void displayImage(String path, ImageView imageView,
                             ImageLoadingListener listener, DisplayImageOptions options) {
        if (mLoader != null) {
            mLoader.displayImage(path, imageView, options, listener);
        }
    }

    //load the image
    public void displayImage(String path, ImageView imageView, ImageLoadingListener listener) {
        if (mLoader != null) {
            mLoader.displayImage(path, imageView, listener);
        }
    }

    public void displayImage(String path, ImageView imageView) {
        displayImage(path, imageView, null);
    }

    /**
     * 默认的图片显示Options,可设置图片的缓存策略，编解码方式等，非常重要
     *
     * @return
     */
    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new
                DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.default_pic)
                .showImageOnFail(R.mipmap.default_pic)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();
        return options;
    }

    public DisplayImageOptions getOptionsWithNoCache() {

        DisplayImageOptions options = new
                DisplayImageOptions.Builder()
                //.cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                //.cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new FadeInBitmapDisplayer(400))
                .build();
        return options;
    }
}

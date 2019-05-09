package com.healthclock.healthclock.clock.fragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.clock.common.WeacConstants;
import com.healthclock.healthclock.clock.fragment.LocalMusicFragment;
import com.healthclock.healthclock.clock.fragment.RingSelectFragment;
import com.healthclock.healthclock.clock.model.RingSelectItem;
import com.healthclock.healthclock.ui.adapter.RingSelectAdapter;
import com.healthclock.healthclock.clock.util.AudioPlayer;
import com.healthclock.healthclock.clock.util.MyUtil;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SystemRingFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * 保存铃声信息的Adapter
     */
    RingSelectAdapter mSystemRingAdapter;

    /**
     * loader Id
     */
    private static final int LOADER_ID = 1;

    /**
     * 铃声选择位置
     */
    private int mPosition = 0;
    private ListView mListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_system_ring,
                container, false);
        ButterKnife.bind(view);
        mListView = view.findViewById(R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Map<String, String> map = mSystemRingAdapter.getItem(position);
                // 取得铃声名
                String ringName = map.get(WeacConstants.RING_NAME);
                // 取得播放地址
                String ringUrl = map.get(WeacConstants.RING_URL);
                // 更新当前铃声选中的位置
                mSystemRingAdapter.updateSelection(ringName);
                // 更新适配器刷新铃声列表显示
                mSystemRingAdapter.notifyDataSetChanged();
                // 设置最后一次选中的铃声选择界面位置为系统铃声界面
                RingSelectItem.getInstance().setRingPager(0);

                // 播放音频文件
                switch (ringUrl) {
                    case WeacConstants.DEFAULT_RING_URL:
                        // 当为默认铃声时
                        AudioPlayer.getInstance(getActivity()).playRaw(
                                R.raw.ring_weac_alarm_clock_default, false, false);
                        // 无铃声
                        break;
                    case WeacConstants.NO_RING_URL:
                        AudioPlayer.getInstance(getActivity()).stop();
                        break;
                    default:
                        AudioPlayer.getInstance(getActivity()).play(ringUrl, false, false);
                        break;
                }

                ViewPager pager = (ViewPager) getActivity().findViewById(R.id.fragment_ring_select_sort);
                PagerAdapter f = pager.getAdapter();
                LocalMusicFragment localMusicFragment = (LocalMusicFragment) f.instantiateItem(pager, 1);
                //RecorderFragment recorderFragment = (RecorderFragment) f.instantiateItem(pager, 2);
                // 取消本地音乐选中标记
                if (localMusicFragment.mLocalMusicAdapter != null) {
                    localMusicFragment.mLocalMusicAdapter.updateSelection("");
                    localMusicFragment.mLocalMusicAdapter.notifyDataSetChanged();
                }
            }
        });
        // 铃声选择界面
        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 管理cursor
        LoaderManager loaderManager = getLoaderManager();
        // 注册Loader
        loaderManager.initLoader(LOADER_ID, null, this);
        // initAdapter();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // 查询内部存储音频文件
        return new CursorLoader(getActivity(),
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, new String[]{
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA}, null, null,
                MediaStore.Audio.Media.DISPLAY_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                // 当为编辑闹钟状态时，铃声名为闹钟单例铃声名
                String ringName1;
                if (RingSelectFragment.sRingName != null) {
                    ringName1 = RingSelectFragment.sRingName;
                } else {
                    SharedPreferences share = getActivity().getSharedPreferences(
                            WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                    // 当为新建闹钟状态时，铃声名为最近一次选择保存的铃声名,没有的话为默认铃声
                    ringName1 = share.getString(WeacConstants.RING_NAME,
                            getString(R.string.default_ring));
                }

                // 过滤重复音频文件的Set
                HashSet<String> set = new HashSet<>();

                //  保存铃声信息的List
                List<Map<String, String>> list = new ArrayList<>();
                // 添加默认铃声
                Map<String, String> defaultRing = new HashMap<>();
                defaultRing.put(WeacConstants.RING_NAME, getString(R.string.default_ring));
                defaultRing.put(WeacConstants.RING_URL, WeacConstants.DEFAULT_RING_URL);
                list.add(defaultRing);
                set.add(getString(R.string.default_ring));

                // 保存的铃声名为默认铃声，设置该列表的显示位置
                if (getString(R.string.default_ring).equals(ringName1)) {
                    mPosition = 0;
                    RingSelectItem.getInstance().setRingPager(0);
                }

                // 添加无铃声
                Map<String, String> noRing = new HashMap<>();
                noRing.put(WeacConstants.RING_NAME, getString(R.string.no_ring));
                noRing.put(WeacConstants.RING_URL, WeacConstants.NO_RING_URL);
                list.add(noRing);
                set.add(getString(R.string.no_ring));

                // 当列表中存在与保存的铃声名一致时，设置该列表的显示位置
                if (getString(R.string.no_ring).equals(ringName1)) {
                    mPosition = list.size() - 1;
                    RingSelectItem.getInstance().setRingPager(0);
                }

                if (cursor != null) {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                            .moveToNext()) {
                        // 音频文件名
                        String ringName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        if (ringName != null) {
                            // 当过滤集合里不存在此音频文件
                            if (!set.contains(ringName)) {
                                // 添加音频文件到列表过滤同名文件
                                set.add(ringName);
                                // 去掉音频文件的扩展名
                                ringName = MyUtil.removeEx(ringName);
                                // 取得音频文件的地址
                                String ringUrl = cursor.getString(cursor
                                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                                Map<String, String> map = new HashMap<>();
                                map.put(WeacConstants.RING_NAME, ringName);
                                map.put(WeacConstants.RING_URL, ringUrl);
                                list.add(map);
                                // 当列表中存在与保存的铃声名一致时，设置该列表的显示位置
                                if (ringName.equals(ringName1)) {
                                    mPosition = list.size() - 1;
                                    RingSelectItem.getInstance().setRingPager(0);
                                }
                            }
                        }
                    }
                }

                mSystemRingAdapter = new RingSelectAdapter(getActivity(), list, ringName1);
                mListView.setAdapter(mSystemRingAdapter);
                mListView.setSelection(mPosition);
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }
}

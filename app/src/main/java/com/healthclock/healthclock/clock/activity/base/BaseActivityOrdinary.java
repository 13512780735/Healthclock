/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.healthclock.healthclock.clock.activity.base;

import android.os.Bundle;

import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.util.L;
import com.squareup.leakcanary.RefWatcher;

import me.imid.swipebacklayout.lib.app.SwipeBackOrdinaryActivity;

/**
 * Activity普通管理类(继承FragmentActivity)
 *
 * @author 咖枯
 * @version 1.0 2016/3/16
 */
public class BaseActivityOrdinary extends SwipeBackOrdinaryActivity {

    private static final String LOG_TAG = "BaseActivityOrdinary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        L.i(LOG_TAG, getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = App.getRefWatcher(this);
//        refWatcher.watch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟session的统计
      //  MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // MobclickAgent.onPause(this);
    }
}

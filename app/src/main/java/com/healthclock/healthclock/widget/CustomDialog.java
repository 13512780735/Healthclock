package com.healthclock.healthclock.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthclock.healthclock.R;


/**
 * Created by Administrator on 2017/12/26.
 */

public class CustomDialog {
    private final Display display;
    private Context context;
    private TextView mTvTitle;
    private TextView mTvSubTitle;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvOK;
    private ImageView mIvIcon;
    private Dialog dialog;
    private Window dialogWindow;
    private LinearLayout mLinearBottom;
    private RelativeLayout mRelativeBottom;
    private LinearLayout mLinearBottom01;

    public CustomDialog(Context context) {
        this.context = context;
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        dialog = new Dialog(context, R.style.Custom_Dialog_Style);
        dialogWindow = dialog.getWindow();

    }

    /**
     * 构建
     *
     * @return
     */
    public CustomDialog builder() {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_ensure_layout, null, false);
        LinearLayout mLinearDialog = ((LinearLayout) view.findViewById(R.id.linear_dialog));
        mLinearBottom = ((LinearLayout) view.findViewById(R.id.linear_bottom));//含取消确定
        mLinearBottom01 = ((LinearLayout) view.findViewById(R.id.linear_bottom01));//含单一确定
        mRelativeBottom = ((RelativeLayout) view.findViewById(R.id.rl_bottom));//底部按钮

        mIvIcon = ((ImageView) view.findViewById(R.id.iv_icon));
        mTvTitle = ((TextView) view.findViewById(R.id.tv_title));
        mTvSubTitle = ((TextView) view.findViewById(R.id.tv_sub_title));
        mTvSure = ((TextView) view.findViewById(R.id.tv_sure));
        mTvOK = ((TextView) view.findViewById(R.id.tv_ok));
        mTvCancel = ((TextView) view.findViewById(R.id.tv_cancel));
        dialog.setContentView(view);
        mLinearDialog.setLayoutParams(new FrameLayout.LayoutParams(((int) (display.getWidth() * 0.80)), LinearLayout.LayoutParams.WRAP_CONTENT));
        dialogWindow.setGravity(Gravity.CENTER);
        return this;
    }

    /**
     * setting dialog position
     *
     * @param gravity
     * @return
     */
    public CustomDialog setGravity(int gravity) {
        dialogWindow.setGravity(gravity);
        return this;
    }

    /**
     * setting dialog icon
     *
     * @param drawable
     * @return
     */
    public CustomDialog setIncon(int drawable) {
        mIvIcon.setVisibility(View.VISIBLE);
        mIvIcon.setImageResource(drawable);
        return this;
    }

    /**
     * setting dialog title
     *
     * @param title
     * @return
     */
    public CustomDialog setTitle(String title) {
        if ("".equals(title)) {
            mTvTitle.setText("标题");
        } else {
            mTvTitle.setText(title);
        }
        return this;
    }

    /**
     * 无按钮的标题
     *
     * @param title
     * @return
     */
    public CustomDialog setTitle01(String title, int color) {
        if ("".equals(title)) {
            mTvTitle.setText("标题");
        } else {
            mTvTitle.setText(title);
            mRelativeBottom.setVisibility(View.GONE);

        }
        mTvTitle.setTextColor(color);
        return this;
    }

    /**
     * @param title
     * @param color
     * @return
     */
    public CustomDialog setTitle(String title, int color) {
        if ("".equals(title)) {
            mTvTitle.setText("标题");
        } else {
            mTvTitle.setText(title);
        }
        mTvTitle.setTextColor(color);
        return this;
    }

    /**
     * setting dialog title
     *
     * @param title
     * @return
     */
    public CustomDialog setSubTitle(String title) {

        if ("".equals(title)) {
            mTvSubTitle.setVisibility(View.GONE);
        } else {
            mTvSubTitle.setVisibility(View.VISIBLE);
//            mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mTvTitle.setGravity(Gravity.BOTTOM);

            mTvSubTitle.setText(title);
        }
        return this;
    }

    /**
     * @param title
     * @param color
     * @return
     */
    public CustomDialog setSubTitle(String title, int color) {
        if ("".equals(title)) {
            mTvSubTitle.setVisibility(View.GONE);
            mTvSubTitle.setText("标题");
        } else {
            mTvSubTitle.setVisibility(View.VISIBLE);
            mTvTitle.setGravity(Gravity.BOTTOM);
            mTvSubTitle.setText(title);
        }
        mTvSubTitle.setTextColor(color);
        return this;
    }

    /**
     * setting dialog right button
     *
     * @param text
     * @param listener
     * @return
     */
    public CustomDialog setPositiveButton(String text, final View.OnClickListener listener) {
        if ("".equals(text)) {
            mTvSure.setText("确认");
        } else {
            mTvSure.setText(text);
        }
        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    /**
     * 可设置颜色
     *
     * @param text
     * @param color
     * @param listener
     * @return
     */
    public CustomDialog setPositiveButton(String text, int color, final View.OnClickListener listener) {
        if ("".equals(text)) {
            mTvSure.setText("确认");
        } else {
            mTvSure.setText(text);
        }

        mTvSure.setTextColor(color);
        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * setting dialog left button
     *
     * @param text
     * @param listener
     * @return
     */
    public CustomDialog setNegativeButton(String text, final View.OnClickListener listener) {
        if ("".equals(text)) {
            mTvCancel.setText("取消");
        } else {
            mTvCancel.setText(text);
        }

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public CustomDialog setNegativeButton(String text, int color, final View.OnClickListener listener) {
        if ("".equals(text)) {
            mTvCancel.setText("取消");
        } else {
            mTvCancel.setText(text);
        }
        mTvCancel.setTextColor(color);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * setting dialog center button
     *
     * @param text
     * @param listener
     * @return
     */
    public CustomDialog setCenterButton(String text, final View.OnClickListener listener) {
        if ("".equals(text)) {
            mTvOK.setText("确认");
        } else {
            mTvOK.setText(text);
        }
        mLinearBottom.setVisibility(View.GONE);
        mTvOK.setVisibility(View.VISIBLE);

        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * setting dialog center button
     *
     * @param text
     * @param listener
     * @return
     */
    public CustomDialog setCenterButton(String text, int color, final View.OnClickListener listener) {
        if ("".equals(text)) {
            mTvOK.setText("确认");
        } else {
            mTvOK.setText(text);
        }
        mTvOK.setTextColor(color);
        mLinearBottom.setVisibility(View.GONE);
        mTvOK.setVisibility(View.VISIBLE);

        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * setting dialog cancelable
     *
     * @param cancelable
     * @return
     */
    public CustomDialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    /**
     * cancel dialog
     *
     * @return
     */
    public CustomDialog dismiss() {
        dialog.dismiss();
        return this;
    }

    /**
     * show dialog
     */
    public void show() {
        dialog.show();
    }
}

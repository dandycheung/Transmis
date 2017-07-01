package com.dss886.transmis.call;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

public class CallActivity extends BaseActivity {

    private SwitchItem mCallSwitch;
    private TextItem mTitleItem;
    private TextItem mContentItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.call_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addViews() {
        mCallSwitch = new SwitchItem(this, "未接电话提醒", Tags.SP_MISSED_CALL_ENABLE, true);
        mTitleItem = new TextItem(this, "未接电话提醒标题").setCallback(sp -> {
            String value = sp.getString(Tags.SP_CALL_TITLE_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mContentItem = new TextItem(this, "未接电话提醒内容").setCallback(sp -> {
            String value = sp.getString(Tags.SP_CALL_CONTENT_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });

        addView(mCallSwitch);
        addView(mTitleItem);
        addView(mContentItem);
    }

    @Override
    protected void setListeners() {
        mCallSwitch.setOnClickListener(v -> {});
        setTextItemListener(mTitleItem, Tags.SP_CALL_TITLE_REGEX, "设置提醒标题");
        setTextItemListener(mContentItem, Tags.SP_CALL_CONTENT_REGEX, "设置提醒内容");
    }

    private void setTextItemListener(TextItem item, String key, String showTitle) {
        item.setOnClickListener(v -> {
            String value = App.sp.getString(key, null);
            DialogBuilder.showEditTextDialog(this, showTitle, value, false, content -> {
                SharedPreferences.Editor editor = App.sp.edit();
                if (TextUtils.isEmpty(content)) {
                    editor.remove(key);
                } else {
                    editor.putString(key, content);
                }
                editor.apply();
                item.onResume();
            });
        });
    }
}

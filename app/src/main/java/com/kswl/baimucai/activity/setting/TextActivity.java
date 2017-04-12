package com.kswl.baimucai.activity.setting;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.utils.Constants;

public class TextActivity extends BaseActivity {

    TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        showTopLine();

        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod());

        int type = getIntent().getIntExtra(Constants.Char.INTENT_TEXT_TYPE, 0);
        switch (type) {
            case Constants.Code.TEXT_PROTOCOL:
                showTopTitle(R.string.user_register_protocol);
                break;

            default:
                break;
        }
    }
}

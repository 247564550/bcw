package com.kswl.baimucai.activity.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.utils.Constants;

public class AccountEditActivity extends BaseActivity {


    EditText et_input;
    ImageView iv_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        showTopTitle(R.string.account_title);
        showRightText(R.string.save);

        et_input = (EditText) findViewById(R.id.et_input);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.GONE);
                }
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_input.setText("");
            }
        });
    }

    @Override
    public void onRightTextBtnClick() {
        String input = et_input.getText().toString().trim();
        if (!TextUtils.isEmpty(input)) {
            Intent intent = new Intent();
            intent.putExtra(Constants.Char.ACCOUNT_TEXT, input);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}

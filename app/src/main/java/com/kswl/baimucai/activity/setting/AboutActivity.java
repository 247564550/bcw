package com.kswl.baimucai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.utils.Constants;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        showTopTitle(R.string.own_about);

        ListView lv_content = (ListView) findViewById(R.id.lv_content);
        String[] content = getResources().getStringArray(R.array.about_content);
        lv_content.setAdapter(new ArrayAdapter<String>(this, R.layout.item_about, R.id.tv_name,
                content));
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AboutActivity.this, WebviewActivity.class);
                switch (i) {
                    case 0:
                        // 网站介绍
                        intent.putExtra(Constants.Char.INTENT_WEB_TYPE, Constants.Code.WEB_SITES);
                        break;
                    case 1:
                        // 公司介绍
                        intent.putExtra(Constants.Char.INTENT_WEB_TYPE, Constants.Code.WEB_COMPANY);
                        break;
                    case 2:
                        // 法律声明
                        intent.putExtra(Constants.Char.INTENT_WEB_TYPE, Constants.Code
                                .WEB_LEGAL_NOTICES);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

}

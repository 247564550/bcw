package com.kswl.baimucai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

public class VipActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        showTopTitle(R.string.own_vip);

        ListView lv_content = (ListView) findViewById(R.id.lv_content);
        String[] content = getResources().getStringArray(R.array.vip_content);
        lv_content.setAdapter(new ArrayAdapter<String>(this, R.layout.item_about, R.id.tv_name,
                content));
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        // 联系平台

                        break;
                    case 1:
                        // 问题解答
                        startActivity(new Intent(VipActivity.this, ProblemsActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

}

package com.kswl.baimucai.activity.shoppingcart;

import android.os.Bundle;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

public class ShoppingCartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart, true);
        ShoppingCartFragment fragment = ShoppingCartFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment, fragment, "0")
                .show(fragment).commit();
    }
}

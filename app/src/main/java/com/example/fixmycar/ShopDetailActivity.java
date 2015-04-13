package com.example.fixmycar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ShopDetailActivity extends ActionBarActivity {
    private TextView textView_shop_name;
    private ImageView imageView_shop_image;
    private String shop_detail_phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));

        textView_shop_name = (TextView)findViewById(R.id.shop_detail_name);
        Intent intent = this.getIntent();
        final ShopListItem shopDetailInfo =(ShopListItem)intent.getSerializableExtra("ShopDetailInfo");
        final DamageCarInfo damageCarInfo = (DamageCarInfo)intent.getSerializableExtra("DamageCarInfo");

        textView_shop_name.setText(shopDetailInfo.getShopName());

        imageView_shop_image = (ImageView)findViewById(R.id.shop_detail_image);
        imageView_shop_image.setImageResource(shopDetailInfo.getShopImageId());

        TextView textView_shop_addr= (TextView)findViewById(R.id.shop_detail_addr);
        textView_shop_addr.setText(shopDetailInfo.getShopAddr());

        TextView textView_shop_phone = (TextView)findViewById(R.id.shop_detail_phone);
        shop_detail_phone_number = (shopDetailInfo.getShopPhone());
        textView_shop_phone.setText(shop_detail_phone_number);

        ImageButton imb_phone_call = (ImageButton)findViewById(R.id.phone_call);
        imb_phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + shop_detail_phone_number));
                startActivity(intent);
            }
        });
        ImageButton imb_take_order = (ImageButton)findViewById(R.id.take_order);
        imb_take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopDetailActivity.this,OrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ShopDetailInfo", shopDetailInfo);
                bundle.putSerializable("DamageCarInfo",damageCarInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

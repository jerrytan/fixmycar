package com.example.fixmycar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class OrderActivity extends ActionBarActivity {
    private ShopListItem shopDetailInfo;
    private DamageCarInfo damageCarInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));

        Intent intent = this.getIntent();
        shopDetailInfo =(ShopListItem)intent.getSerializableExtra("ShopDetailInfo");
        damageCarInfo = (DamageCarInfo)intent.getSerializableExtra("DamageCarInfo");

        TextView txv_car_model = (TextView)findViewById(R.id.order_car_model);
        txv_car_model.setText(getResources().getString(R.string.order_car_model)+ damageCarInfo.getCarMode());

        TextView txv_car_desc = (TextView)findViewById(R.id.order_car_desc);
        txv_car_desc.setText(getResources().getString(R.string.order_car_desc)+ damageCarInfo.getCarDamageDesc());

        ImageView imv_car_image = (ImageView)findViewById(R.id.order_car_image);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File("/storage/sdcard0/car.jpg"))));
            imv_car_image.setImageBitmap(bitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }

        TextView txv_shop_name = (TextView)findViewById(R.id.order_shop_name);
        txv_shop_name.setText(getResources().getString(R.string.order_shop_name)+ shopDetailInfo.getShopName());

        Button btn_confirm = (Button)findViewById(R.id.order_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this,CommentActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_order, menu);
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

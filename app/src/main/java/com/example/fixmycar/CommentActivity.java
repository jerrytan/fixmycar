package com.example.fixmycar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CommentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));

        Intent intent = this.getIntent();
        final ShopListItem shopDetailInfo =(ShopListItem)intent.getSerializableExtra("ShopDetailInfo");
        final DamageCarInfo damageCarInfo = (DamageCarInfo)intent.getSerializableExtra("DamageCarInfo");

        TextView txv_shop_name = (TextView)findViewById(R.id.comment_shop_name);
        txv_shop_name.setText(getResources().getString(R.string.order_shop_name)+ shopDetailInfo.getShopName());
        TextView txv_car_model = (TextView)findViewById(R.id.comment_car_model);
        txv_car_model.setText(getResources().getString(R.string.order_car_model)+ damageCarInfo.getCarMode());

        Button btn_submit_comment = (Button)findViewById(R.id.write_comment);
        btn_submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommentActivity.this,"您的评价已经提交，非常感谢.",Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_share_comment = (Button)findViewById(R.id.share_comment);
        btn_share_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommentActivity.this,"您的评价已经分享，非常感谢.",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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

package com.example.fixmycar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GetShopListActivity extends ActionBarActivity {
    public static final int SHOW_RESPONSE = 1;
    private TextView textView_wait;
    private ProgressBar progressBar_wait;

    private ListView listViewShopList;
    private List<ShopListItem>shopList = new ArrayList<ShopListItem>();

    private String remote_uri;

    private void initShopListUsingLocalData() {
        ShopListItem item1 = new ShopListItem("金富力车专家 ",R.drawable.shop4,"海淀区上地东路14-21号(联想中国区总部对面)","010-62968066","2000","钣金加喷漆，3天就好","5","3000");
        shopList.add(item1);
//        ShopListItem item2 = new ShopListItem("四海通达汽车服务中心local",R.drawable.shop2,"海淀区唐家岭路8号鑫达仓储东邻","010-82374349","1000","小问题，4天就好","4","1000");
//        shopList.add(item2);
//        ShopListItem item3 = new ShopListItem("EasyCarLife(上地上第MOMA店)",R.drawable.shop3,"海淀区安宁路上第MOMA小区B2楼","010-82741543","2000","钣金加喷漆，3天就好","5","2000");
//        shopList.add(item3);
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;

                    textView_wait.setText(R.string.shop_bid_list);
                    progressBar_wait.setVisibility(View.GONE);
                    listViewShopList.invalidateViews();
                    listViewShopList.setVisibility(View.VISIBLE);

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_shop_list);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));

        Intent intent_caller = getIntent();
        String requestId = intent_caller.getStringExtra("RequestId");
        final DamageCarInfo carInfo = (DamageCarInfo)intent_caller.getBundleExtra("DamageCarInfoBundle").get("DamageCarInfo");

        try {
            JSONObject jsonObject = new JSONObject(requestId);
            remote_uri = "http://cp01-rdqa-dev372.cp01.baidu.com:8301/api/repair-request/"
                    + jsonObject.getString("request-id") + "/respond";
        }catch (JSONException e) {
            e.printStackTrace();
        }


        textView_wait=(TextView)findViewById(R.id.txt_wait);
        progressBar_wait=(ProgressBar)findViewById(R.id.progressBar);


        //initShopListUsingLocalData();
        final ShopListItemAdapter adapter = new ShopListItemAdapter(GetShopListActivity.this,
                R.layout.shoplist_item,shopList);
        listViewShopList = (ListView)findViewById(R.id.lv_shop_list);
        listViewShopList.setAdapter(adapter);
        listViewShopList.setEnabled(true);
        listViewShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopListItem item = shopList.get(position);

                Intent intent = new Intent(GetShopListActivity.this, ShopDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ShopDetailInfo", item);
                bundle.putSerializable("DamageCarInfo", carInfo);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        listViewShopList.setVisibility(View.INVISIBLE);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendHttpRequest(false);
            }
        };
        timer.schedule(task,5000);
    }

    private void sendHttpRequest(final Boolean usingLocalData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(remote_uri);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity1 = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity1, "utf-8");
                        parseJson(response);
                    }
                    if (usingLocalData == true) {
                        initShopListUsingLocalData();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void parseJson(String response) {

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i=0;i< jsonArray.length();i++ ) {
                JSONObject shopInfo = jsonArray.getJSONObject(i);
                String shopName = shopInfo.getString("shop-name");
                String shopAddr = shopInfo.getString("shop-address");
                String shopPhone = shopInfo.getString("service-phone-number");
                String bidComment = shopInfo.getString("fault-analysis");
                String bidPrice = shopInfo.getString("quote");
                String shopRant=  shopInfo.getString("average-rating");
                String shopDistance = shopInfo.getString("distance");
                int shop_image_id = 0;

                //hack for image
                switch (i) {
                    case 0:
                        shop_image_id= R.drawable.shop1;
                        break;
                    case 1:
                        shop_image_id = R.drawable.shop2;
                        break;
                    case 2:
                        shop_image_id = R.drawable.shop3;
                        break;
                    default:
                        break;
                }

                ShopListItem item = new ShopListItem(shopName,shop_image_id,
                        shopAddr,shopPhone,bidPrice,bidComment,shopRant,shopDistance);
                shopList.add(item);
            }

            Log.d("GetShopListActivity", "ShopList is " + response);
            Message message = new Message();
            message.what = SHOW_RESPONSE;
            message.obj= response.toString();
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_shop_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            shopList.clear();
            sendHttpRequest(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

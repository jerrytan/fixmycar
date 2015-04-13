package com.example.fixmycar;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


public class SubmitCarInfoActivity extends ActionBarActivity {
    public static final int TAKE_PHOTO=1;
    public static final int CROP_PHOTO=2;

    private Button btn_take_photo;
    private Button btn_choose_photo;
    private Button btn_submit;
    private Spinner spn_car_mode;
    private ImageView imv_car_photo;

    private EditText edt_car_desc;
    private TextView txv_location;
    private DamageCarInfo damageCarInfo;
    private ProgressDialog progressDialog = null;


    private String carDesc;
    private Uri carImageUri;
    private String carMode;
    private double carLongitude;
    private double carLatitude;

    public static final String REMOTE_SERVER_API= "http://cp01-rdqa-dev372.cp01.baidu.com:8301/api/repair-request";
    private String request_id = null;

    private LocationClient mLocationClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_car_info);

        txv_location = (TextView)findViewById(R.id.car_location);
        edt_car_desc = (EditText) findViewById(R.id.edt_car_desc);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));


        //got from baidu location sdk
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                txv_location.setText(getString(R.string.current_location)+location.getAddrStr());
                carLatitude = location.getLatitude();
                carLongitude= location.getLongitude();

            }
        });
        mLocationClient.start();
        spn_car_mode = (Spinner)findViewById(R.id.spin_car_mode);
        spn_car_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carMode = getResources().getStringArray(R.array.car_mode_arrays)[position];
                //Toast.makeText(SubmitCarInfoActivity.this, "You clicked " + carMode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imv_car_photo = (ImageView) findViewById(R.id.imv_car_photo);
        btn_take_photo = (Button)findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.getExternalStorageDirectory(),"car.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                carImageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, carImageUri);
                startActivityForResult(intent, TAKE_PHOTO);

            }
        });
        btn_choose_photo = (Button)findViewById(R.id.btn_choose_photo);
        btn_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.getExternalStorageDirectory(),"car.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                carImageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop" ,true);
                intent.putExtra("scale",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, carImageUri);
                startActivityForResult(intent, CROP_PHOTO);
            }
        });

        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubmitCarInfoActivity.this,GetShopListActivity.class);
                String imageString = null;
                if (carImageUri != null) {
                    imageString = carImageUri.toString();
                }
                carDesc = edt_car_desc.getText().toString();
                damageCarInfo = new DamageCarInfo(carMode, imageString,carDesc,
                        carLatitude,carLongitude);

                submitDamageCarInfoUsingHttpUtil();

                progressDialog = new ProgressDialog(SubmitCarInfoActivity.this);
                progressDialog.setTitle(getResources().getString(R.string.please_wait_for_submit));
                progressDialog.setMessage(getResources().getString(R.string.please_wait_for_submit));
                progressDialog.setCancelable(false);
                progressDialog.show();


            }
        });

    }

    public  void uploadMethod(final RequestParams params,final String uploadHost) {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, uploadHost, params,new RequestCallBack<String>() {
            @Override
            public void onStart() {

            }
            @Override
            public void onLoading(long total, long current,boolean isUploading) {

            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                request_id = responseInfo.result;
                Log.d("MyResult",responseInfo.result);
                progressDialog.dismiss();

                Bundle bundle = new Bundle();
                Intent intent = new Intent(SubmitCarInfoActivity.this,GetShopListActivity.class);
                bundle.putSerializable("DamageCarInfo", damageCarInfo);
                intent.putExtra("DamageCarInfoBundle",bundle);
                intent.putExtra("RequestId",request_id);
                startActivity(intent);
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i("SubmitCarInfoActivity", error.getExceptionCode() + ":" + msg);
            }
        });
    }
    private void submitDamageCarInfoUsingHttpUtil() {
        String uploadHost=REMOTE_SERVER_API;
        RequestParams params=new RequestParams();
        params.addBodyParameter("car-model",damageCarInfo.getCarMode());
        params.addBodyParameter("latitude",""+damageCarInfo.getCarLatitude());
        params.addBodyParameter("longitude",""+damageCarInfo.getCarLongitude());
        if (damageCarInfo.getCarDamageDesc() != null) {
            params.addBodyParameter("damage-description",damageCarInfo.getCarDamageDesc());
        }

        //hack for my sumsung note3
        params.addBodyParameter("attachment1", new File("/storage/sdcard0/car.jpg"));
//        if (damageCarInfo.getCarImage() !=null) {
//            params.addBodyParameter("attachment1", new File(damageCarInfo.getCarImage()));
//        }

        uploadMethod(params,uploadHost);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(carImageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,carImageUri);
                    startActivityForResult(intent,CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode== RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(carImageUri));
                        imv_car_photo.setImageBitmap(bitmap);

                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_car_info, menu);
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

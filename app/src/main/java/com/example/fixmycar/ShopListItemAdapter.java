package com.example.fixmycar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tanzhongyi on 2015/4/4.
 */
public class ShopListItemAdapter extends ArrayAdapter<ShopListItem> {
    private int resouceId;

    public ShopListItemAdapter(Context context, int ResouceId, List<ShopListItem> objects) {
        super(context,ResouceId,objects);
        resouceId = ResouceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopListItem shopListItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resouceId, null);
        TextView shopName = (TextView) view.findViewById(R.id.shop_name);
        shopName.setText(shopListItem.getShopName());

        TextView shopAddr = (TextView) view.findViewById(R.id.shop_addr);
        shopAddr.setText(shopListItem.getShopAddr());

        TextView shopDistance = (TextView)view.findViewById(R.id.shop_distance);
        shopDistance.setText(getContext().getResources().getText(R.string.shop_distance)+ shopListItem.getDistance()+"米");

        TextView shopPhone = (TextView) view.findViewById(R.id.shop_phone);
        shopPhone.setText(shopListItem.getShopPhone());


        TextView shopBidprice = (TextView) view.findViewById(R.id.shop_bidprice);
        shopBidprice.setText(getContext().getResources().getText(R.string.bid_price)+ shopListItem.getBidPrice()+"元");

        TextView shopBidComment = (TextView) view.findViewById(R.id.shop_bidcomment);
        shopBidComment.setText(getContext().getResources().getText(R.string.bid_comment)+shopListItem.getBidComment());

        TextView shopRank = (TextView) view.findViewById(R.id.shop_rank);
        shopRank.setText(shopListItem.getShopRank());

        ImageView shopImage = (ImageView)view.findViewById(R.id.shop_image);
        shopImage.setImageResource(shopListItem.getShopImageId());

        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setRating(Float.parseFloat(shopListItem.getShopRank()));

        return view;
    }


}

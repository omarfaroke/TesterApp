package com.codingacademy.testerapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageAdapter extends PagerAdapter {
 private Context mcontext;
 private int[]mimageides = new int[]{R.drawable.c,R.drawable.p,R.drawable.php,R.drawable.j};

 ImageAdapter(Context context)
 {
     mcontext=context;
 }

    @Override
    public int getCount() {
        return mimageides.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView ( mcontext );
        imageView.setScaleType ( ImageView.ScaleType.CENTER_CROP );
        imageView.setImageResource ( mimageides[position] );
        container.addView ( imageView,0 );
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView ( (ImageView)object );
    }
}

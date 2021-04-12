package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class BigImageActivity extends AppCompatActivity {
int position;
    MainActivity mA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        final Intent i = getIntent();

        // Selected image id
         position = i.getExtras().getInt("id");
      //  Bundle extras = i.getExtras();
//        String[] strUrls = i.getStringArrayExtra("strUrls");
        //MainActivity.ImageAdapter imageAdapter = new MainActivity.ImageAdapter(this);
//View bigImgView = (View) findViewById(R.id.BigImageView);

        final ImageView imageView = (ImageView) findViewById(R.id.SingleView);
       // imageView.setImageResource(imageAdapter.mThumbIds[position]);
         mA =new MainActivity();
        imageView.setImageURI(mA.getmUrls()[position]);
       // Bitmap bmp = decodeURI(mA.getmUrls()[position].getPath());
        //BitmapFactory.decodeFile(mUrls[position].getPath());
        //imageView.setImageBitmap(bmp);

        //imageView.setBackground(mA.getmUrls()[position]);
        // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setLayoutParams(new ImageView(100,200));
       // Picasso.get().load(strUrls[position]).into(imageView);
        //imageView.setImageResource(MainActivity.mUrls[position]);
        imageView.setOnTouchListener(new OnSwipeTouchListener(BigImageActivity.this) {

            public void onSwipeTop() {
                Toast.makeText(BigImageActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(BigImageActivity.this, "right", Toast.LENGTH_SHORT).show();
                if(position==0){
                    Toast.makeText(BigImageActivity.this, "right- To ostatnie zdjecie", Toast.LENGTH_SHORT).show();
                }else{
                    position--;
                    i.putExtra("id",position);
                    imageView.setImageURI(mA.getmUrls()[position]);
                }
            }
            public void onSwipeLeft() {
                Toast.makeText(BigImageActivity.this, "left", Toast.LENGTH_SHORT).show();
                if(position==mA.getmUrls().length-1){
                    Toast.makeText(BigImageActivity.this, "left- To ostatnie zdjecie", Toast.LENGTH_SHORT).show();
                }else{
                    position++;
                    i.putExtra("id",position);
                    imageView.setImageURI(mA.getmUrls()[position]);
                }

            }
            public void onSwipeBottom() {
                Toast.makeText(BigImageActivity.this, "bottom", Toast.LENGTH_SHORT).show();
              BigImageActivity.this.finish();
            }

        });
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }

  /*  public Bitmap decodeURI(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth  <= 16384000){
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight* 10
                    : options.outWidth * 10;
            options.inSampleSize =
                    (int)Math.pow(2d, Math.floor(
                            Math.log(sampleSize)/Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        return BitmapFactory.decodeFile(filePath, options);
    }*/
}

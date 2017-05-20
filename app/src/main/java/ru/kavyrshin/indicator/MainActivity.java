package ru.kavyrshin.indicator;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.kavyrshinr.pagerindicator.PagerIndicator;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerIndicator pagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(MainActivity.this);

                int r = (int) (Math.random() * 255);
                int g = (int) (Math.random() * 255);
                int b = (int) (Math.random() * 255);

                imageView.setBackgroundColor(Color.rgb(r, g, b));

                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewPager.addOnPageChangeListener(pagerIndicator);
        pagerIndicator.setCountIndicator(viewPager.getAdapter().getCount());
        pagerIndicator.setCurrentPosition(viewPager.getCurrentItem());

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagerIndicator.setCountIndicator(pagerIndicator.getCountIndicator() + 1);
            }
        });
    }
}

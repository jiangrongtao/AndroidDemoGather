package com.jrt.imageslidertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;
    private SliderLayout mDemoSlider1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        mDemoSlider1 = (SliderLayout)findViewById(R.id.slider1);
        initData(mDemoSlider);
        initData(mDemoSlider1);
        start(mDemoSlider,SliderLayout.Transformer.ZoomOut);
        start(mDemoSlider1,SliderLayout.Transformer.ZoomIn);
    }

    private void start(SliderLayout slider,SliderLayout.Transformer transformer) {
        slider.setPresetTransformer(transformer);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
//        slider.setCustomAnimation(new ChildAnimationExample());
        slider.setDuration(2000);
        slider.addOnPageChangeListener(this);
    }

    private void initData(SliderLayout slider) {
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("banner1", R.drawable.banner1);
        file_maps.put("banner2",R.drawable.banner2);
        file_maps.put("banner3",R.drawable.banner3);
        file_maps.put("banner4",R.drawable.banner4);
        file_maps.put("banner5",R.drawable.banner5);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }
    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        mDemoSlider1.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDemoSlider.recoverCycle();
        mDemoSlider1.recoverCycle();

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

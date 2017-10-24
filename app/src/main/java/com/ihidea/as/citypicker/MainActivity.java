package com.ihidea.as.citypicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;

public class MainActivity extends AppCompatActivity {
    
    TextView tv_resultWheel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button goWheel = (Button) findViewById(R.id.goWheel);
        tv_resultWheel = (TextView) findViewById(R.id.tv_resultWheel);
        //城市滚轮选择器
        goWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityPickerView cityPicker = new CityPickerView.Builder(MainActivity.this).textSize(20)
                        .titleTextColor("#3c4350")
                        .backgroundPop(0xa0000000)
                        .province("浙江")
                        .city("杭州")
                        .title("")
                        .titleBackgroundColor("#ffffff")
                        .confirTextColor("#3c4350")
                        .cancelTextColor("#3c4350")
                        .textColor(Color.parseColor("#3c4350"))
                        .provinceCyclic(true)
                        .cityCyclic(false)
                        .itemPadding(16)
                        .build();
                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city) {
                        //返回结果
                        tv_resultWheel.setText(
                                "所选城市：" + province.getName() + "  " + city.getName());
                    }
                    
                    @Override
                    public void onCancel() {
                        
                    }
                });
            }
        });
        
    }

}

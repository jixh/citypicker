package com.lljjcoder.city_20170724;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;
import com.lljjcoder.citypickerview.R;
import com.lljjcoder.citypickerview.utils.XmlParserHandler;
import com.lljjcoder.citypickerview.widget.CanShow;
import com.lljjcoder.citypickerview.widget.wheel.OnWheelChangedListener;
import com.lljjcoder.citypickerview.widget.wheel.WheelView;
import com.lljjcoder.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 省市区三级选择
 */
public class CityPickerView implements CanShow, OnWheelChangedListener {

    private final LayoutInflater layoutInflater;
    private Context context;
    
    private PopupWindow popwindow;
    
    private View popview;
    
    private WheelView mViewProvince;
    
    private WheelView mViewCity;

    private RelativeLayout mRelativeTitleBg;
    
    private TextView mTvOK;
    
    private TextView mTvTitle;
    
    private TextView mTvCancel;
    
    //***************************20170724更新************************************//
    
    //省份数据
    List<ProvinceBean> mProvinceBeanArrayList = new ArrayList<>();
    
    //城市数据
    List<ArrayList<CityBean>> mCityBeanArrayList;
    
    //***************************20170724更新************************************//
    
    //***************************20170822更新************************************//
    
    private com.lljjcoder.city_20170724.bean.ProvinceBean[] mProvinceBeenArray;
    
    private com.lljjcoder.city_20170724.bean.ProvinceBean mProvinceBean;
    
    private CityBean mCityBean;

    /**
     * key - 省 value - 市
     */
    protected Map<String, CityBean[]> mPro_CityMap = new HashMap<String, CityBean[]>();
    

    //***************************20170822更新************************************//
    
    private OnCityItemClickListener listener;
    
    public interface OnCityItemClickListener {
        void onSelected(com.lljjcoder.city_20170724.bean.ProvinceBean province, CityBean city);
        
        void onCancel();
    }
    
    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }
    
    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;
    
    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;
    
    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;
    
    private int textSize = DEFAULT_TEXT_SIZE;
    
    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;
    
    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;
    
    /**
     * 省滚轮是否循环滚动
     */
    private boolean isProvinceCyclic = true;
    
    /**
     * 市滚轮是否循环滚动
     */
    private boolean isCityCyclic = true;
    
    /**
     * item间距
     */
    private int padding = 5;
    
    /**
     * Color.BLACK
     */
    private String cancelTextColorStr = "#000000";
    
    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";
    
    /**
     * 标题背景颜色
     */
    private String titleBackgroundColorStr = "#E9E9E9";
    
    /**
     * 标题颜色
     */
    private String titleTextColorStr = "#E9E9E9";
    
    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private String defaultProvinceName = "江苏";
    
    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private String defaultCityName = "常州";
    
    /**
     * 第一次默认得显示，一般配合定位，使用
     */
    private String defaultDistrict = "新北区";
    
    /**
     * 两级联动
     */
    private boolean showProvinceAndCity = false;
    
    /**
     * 标题
     */
    private String mTitle = "选择地区";
    
    /**
     * 设置popwindow的背景
     */
    private int backgroundPop = 0xa0000000;
    
    private CityPickerView(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isProvinceCyclic = builder.isProvinceCyclic;
        this.isCityCyclic = builder.isCityCyclic;
        this.context = builder.mContext;
        this.padding = builder.padding;
        this.mTitle = builder.mTitle;
        this.titleBackgroundColorStr = builder.titleBackgroundColorStr;
        this.confirmTextColorStr = builder.confirmTextColorStr;
        this.cancelTextColorStr = builder.cancelTextColorStr;
        
        this.defaultDistrict = builder.defaultDistrict;
        this.defaultCityName = builder.defaultCityName;
        this.defaultProvinceName = builder.defaultProvinceName;
        
        this.showProvinceAndCity = builder.showProvinceAndCity;
        this.backgroundPop = builder.backgroundPop;
        this.titleTextColorStr = builder.titleTextColorStr;
        
        layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.layout_citypicker, null);
        
        mViewProvince =  popview.findViewById(R.id.id_province);
        mViewCity =  popview.findViewById(R.id.id_city);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(R.id.rl_title);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);
        
        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(backgroundPop));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);
        
        /**
         * 设置标题背景颜色
         */
        if (!TextUtils.isEmpty(this.titleBackgroundColorStr)) {
            mRelativeTitleBg.setBackgroundColor(Color.parseColor(this.titleBackgroundColorStr));
        }
        
        /**
         * 设置标题
         */
        if (!TextUtils.isEmpty(this.mTitle)) {
            mTvTitle.setText(this.mTitle);
        }
        
        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.titleTextColorStr)) {
            mTvTitle.setTextColor(Color.parseColor(this.titleTextColorStr));
        }
        
        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.confirmTextColorStr)) {
            mTvOK.setTextColor(Color.parseColor(this.confirmTextColorStr));
        }
        
        //设置取消按钮文字颜色
        if (!TextUtils.isEmpty(this.cancelTextColorStr)) {
            mTvCancel.setTextColor(Color.parseColor(this.cancelTextColorStr));
        }
        initProvinceDatas(context);
        // 添加change事件
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewCity.setDrawShadows(false);

        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(mProvinceBean, mCityBean);
                hide();
            }
        });
        
    }
    
    public static class Builder {
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF585858;
        
        /**
         * Default text size
         */
        public static final int DEFAULT_TEXT_SIZE = 18;
        
        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;
        
        private int textSize = DEFAULT_TEXT_SIZE;
        
        /**
         * 滚轮显示的item个数
         */
        private static final int DEF_VISIBLE_ITEMS = 5;
        
        // Count of visible items
        private int visibleItems = DEF_VISIBLE_ITEMS;
        
        /**
         * 省滚轮是否循环滚动
         */
        private boolean isProvinceCyclic = true;
        
        /**
         * 市滚轮是否循环滚动
         */
        private boolean isCityCyclic = true;

        private Context mContext;
        
        /**
         * item间距
         */
        private int padding = 5;
        
        /**
         * Color.BLACK
         */
        private String cancelTextColorStr = "#000000";
        
        /**
         * Color.BLUE
         */
        private String confirmTextColorStr = "#0000FF";
        
        /**
         * 标题背景颜色
         */
        private String titleBackgroundColorStr = "#E9E9E9";
        
        /**
         * 标题颜色
         */
        private String titleTextColorStr = "#E9E9E9";
        
        /**
         * 第一次默认的显示省份，一般配合定位，使用
         */
        private String defaultProvinceName = "江苏";
        
        /**
         * 第一次默认得显示城市，一般配合定位，使用
         */
        private String defaultCityName = "常州";
        
        /**
         * 第一次默认得显示，一般配合定位，使用
         */
        private String defaultDistrict = "新北区";
        
        /**
         * 标题
         */
        private String mTitle = "";
        
        /**
         * 两级联动
         */
        private boolean showProvinceAndCity = false;
        
        /**
         * 设置popwindow的背景
         */
        private int backgroundPop = 0xa0000000;
        
        public Builder(Context context) {
            this.mContext = context;
        }
        
        /**
         * 设置popwindow的背景
         *
         * @param backgroundPopColor
         * @return
         */
        public Builder backgroundPop(int backgroundPopColor) {
            this.backgroundPop = backgroundPopColor;
            return this;
        }
        
        /**
         * 设置标题背景颜色
         *
         * @param colorBg
         * @return
         */
        public Builder titleBackgroundColor(String colorBg) {
            this.titleBackgroundColorStr = colorBg;
            return this;
        }
        
        /**
         * 设置标题背景颜色
         *
         * @param titleTextColorStr
         * @return
         */
        public Builder titleTextColor(String titleTextColorStr) {
            this.titleTextColorStr = titleTextColorStr;
            return this;
        }
        
        /**
         * 设置标题
         *
         * @param mtitle
         * @return
         */
        public Builder title(String mtitle) {
            this.mTitle = mtitle;
            return this;
        }
        
        /**
         * 是否只显示省市两级联动
         *
         * @param flag
         * @return
         */
        public Builder onlyShowProvinceAndCity(boolean flag) {
            this.showProvinceAndCity = flag;
            return this;
        }
        
        /**
         * 第一次默认的显示省份，一般配合定位，使用
         *
         * @param defaultProvinceName
         * @return
         */
        public Builder province(String defaultProvinceName) {
            this.defaultProvinceName = defaultProvinceName;
            return this;
        }
        
        /**
         * 第一次默认得显示城市，一般配合定位，使用
         *
         * @param defaultCityName
         * @return
         */
        public Builder city(String defaultCityName) {
            this.defaultCityName = defaultCityName;
            return this;
        }
        
        /**
         * 第一次默认地区显示，一般配合定位，使用
         *
         * @param defaultDistrict
         * @return
         */
        public Builder district(String defaultDistrict) {
            this.defaultDistrict = defaultDistrict;
            return this;
        }
        
        //        /**
        //         * 确认按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder confirTextColor(int color) {
        //            this.confirmTextColor = color;
        //            return this;
        //        }
        
        /**
         * 确认按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder confirTextColor(String color) {
            this.confirmTextColorStr = color;
            return this;
        }
        
        //        /**
        //         * 取消按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder cancelTextColor(int color) {
        //            this.cancelTextColor = color;
        //            return this;
        //        }
        
        /**
         * 取消按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder cancelTextColor(String color) {
            this.cancelTextColorStr = color;
            return this;
        }
        
        /**
         * item文字颜色
         *
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }
        
        /**
         * item文字大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }
        
        /**
         * 滚轮显示的item个数
         *
         * @param visibleItems
         * @return
         */
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }
        
        /**
         * 省滚轮是否循环滚动
         *
         * @param isProvinceCyclic
         * @return
         */
        public Builder provinceCyclic(boolean isProvinceCyclic) {
            this.isProvinceCyclic = isProvinceCyclic;
            return this;
        }
        
        /**
         * 市滚轮是否循环滚动
         *
         * @param isCityCyclic
         * @return
         */
        public Builder cityCyclic(boolean isCityCyclic) {
            this.isCityCyclic = isCityCyclic;
            return this;
        }

        
        /**
         * item间距
         *
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
        }
        
        public CityPickerView build() {
            CityPickerView cityPicker = new CityPickerView(this);
            return cityPicker;
        }
        
    }
    
    private void setUpData() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultProvinceName) && mProvinceBeenArray.length > 0) {
            for (int i = 0; i < mProvinceBeenArray.length; i++) {
                if (mProvinceBeenArray[i].getName().contains(defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<>(context, mProvinceBeenArray);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);

        
        updateCities();
    }
    
    /**
     * 解析省市区的XML数据
     */
    
    protected void initProvinceDatas(Context context) {

        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinceBeanArrayList = handler.getDataList();

            mCityBeanArrayList = new ArrayList<>(mProvinceBeanArrayList.size());

            //*/ 初始化默认选中的省、市、区，默认选中第一个省份的第一个市区中的第一个区县
            if (mProvinceBeanArrayList != null && !mProvinceBeanArrayList.isEmpty()) {
                mProvinceBean = mProvinceBeanArrayList.get(0);
                List<CityBean> cityList = mProvinceBean.getCityList();
                if (cityList != null && !cityList.isEmpty() && cityList.size() > 0) {
                    mCityBean = cityList.get(0);
                }
            }

            //省份数据
            mProvinceBeenArray = new com.lljjcoder.city_20170724.bean.ProvinceBean[mProvinceBeanArrayList.size()];

            for (int p = 0; p < mProvinceBeanArrayList.size(); p++) {

                //遍历每个省份
                com.lljjcoder.city_20170724.bean.ProvinceBean itemProvince = mProvinceBeanArrayList.get(p);

                //每个省份对应下面的市
                ArrayList<CityBean> cityList = itemProvince.getCityList();

                //当前省份下面的所有城市
                CityBean[] cityNames = new CityBean[cityList.size()];

                //遍历当前省份下面城市的所有数据
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames[j] = cityList.get(j);
                }

                // 省-市的数据，保存到mCitisDatasMap
                mPro_CityMap.put(itemProvince.getName(), cityNames);

                mCityBeanArrayList.add(cityList);

                //赋值所有省份的名称
                mProvinceBeenArray[p] = itemProvince;

            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
    
    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        //省份滚轮滑动的当前位置
        int pCurrent = mViewProvince.getCurrentItem();
        //省份选中的名称
        mProvinceBean = mProvinceBeenArray[pCurrent];
        
        final CityBean[] cities = mPro_CityMap.get(mProvinceBean.getName());
        if (cities == null) {
            return;
        }
        //设置最初的默认城市
        int cityDefault = -1;
        if (!TextUtils.isEmpty(defaultCityName) && cities.length > 0) {
            for (int i = 0; i < cities.length; i++) {
                if (defaultCityName.contains(cities[i].getName())) {
                    cityDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter(context, cities);
        // 设置可见条目数量
        cityWheel.setTextColor(textColor);
        cityWheel.setTextSize(textSize - 3);
        cityWheel.setPadding(padding);

        mViewCity.setViewAdapter(cityWheel);
        mViewCity.setVisibleItems(visibleItems);
        mViewCity.setCyclic(isCityCyclic);

        if (-1 != cityDefault) {
            mViewCity.setCurrentItem(cityDefault);
            mCityBean = cities[cityDefault];
        }
        else {
            mViewCity.setCurrentItem(0);
            mCityBean = cities[0];
        }
    }

    @Override
    public void setType(int type) {
    }
    
    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }
    
    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }
    
    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }
    
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        }else if (wheel == mViewCity){
            //省份滚轮滑动的当前位置
            int pCurrent = mViewCity.getCurrentItem();
            final CityBean[] cities = mPro_CityMap.get(mProvinceBean.getName());
            //省份选中的名称
            mCityBean = cities[pCurrent];
        }
    }
}

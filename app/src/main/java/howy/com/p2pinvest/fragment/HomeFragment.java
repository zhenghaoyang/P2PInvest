package howy.com.p2pinvest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import howy.com.p2pinvest.R;
import howy.com.p2pinvest.bean.Image;
import howy.com.p2pinvest.bean.Index;
import howy.com.p2pinvest.bean.Product;
import howy.com.p2pinvest.common.AppNetConfig;
import howy.com.p2pinvest.util.UIUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by Howy on 2017/6/12.
 */

public class HomeFragment extends Fragment {
    @Bind(R.id.iv_title_back)
    ImageView ivTitleBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @Bind(R.id.vp_home)
    ViewPager vpHome;
    @Bind(R.id.tv_home_product)
    TextView tvHomeProduct;
    @Bind(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    @Bind(R.id.cp_home_indicator)
    CirclePageIndicator cpHomeIndicator;
    private Index index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = UIUtils.getView(R.layout.fragment_home);
        ButterKnife.bind(this, view);
        //初始化title
        initTitle();
        initData();
        return view;

    }

    private void initData() {
        index = new Index();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppNetConfig.INDEX;
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                //解析json数据：FASTJSON
                JSONObject jsonObject = JSON.parseObject(content);
                Log.d(TAG, "onSuccess: " + jsonObject);

                String proInfo = jsonObject.getString("proInfo");
                Product product = JSON.parseObject(proInfo, Product.class);
                String imageArr = jsonObject.getString("imageArr");
                List<Image> images = jsonObject.parseArray(imageArr, Image.class);
                index.product = product;
                index.images = images;

                //更新页面数据
                tvHomeProduct.setText(product.name);
                tvHomeYearrate.setText(product.yearRate + "%");

                //设置ViewPager
                vpHome.setAdapter(new MyAdatper());
                //设置小圆圈显示
                cpHomeIndicator.setViewPager(vpHome);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                UIUtils.toast("联网获取数据失败", true);
            }
        });

    }

    private void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText("首页");
        ivTitleSetting.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MyAdatper extends PagerAdapter {
        @Override
        public int getCount() {
            List<Image> images = index.images;
            return images == null ? 0 : images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            //1显示图片
            String imageUrl = index.images.get(position).IMAURL;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            //2添加到容器里
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}

package howy.com.p2pinvest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import howy.com.p2pinvest.R;
import howy.com.p2pinvest.bean.Image;
import howy.com.p2pinvest.bean.Index;
import howy.com.p2pinvest.bean.Product;
import howy.com.p2pinvest.common.AppNetConfig;
import howy.com.p2pinvest.ui.RoundProgress;
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
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.tv_home_product)
    TextView tvHomeProduct;
    @Bind(R.id.roundPro_home)
    RoundProgress roundProHome;
    @Bind(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    private Index index;
    private int currentProgress;

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
                //获取进度
                currentProgress = Integer.parseInt(index.product.progress);
                roundProHome.setProgress(currentProgress);
                //在分线程中，实现进度的动态变化
                new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= currentProgress; i++) {
                            roundProHome.setProgress(i);
                            SystemClock.sleep(30);
                            roundProHome.postInvalidate();
                        }
                    }
                }.start();

                //设置banner样式
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //取得图片URL集合
                ArrayList<String> imageUrls = new ArrayList<String>(index.images.size());
                for (int i = 0; i < index.images.size(); i++) {
                    imageUrls.add(index.images.get(i).IMAURL);
                    Log.d(TAG, "onSuccess: " + index.images.get(i).IMAURL);
                }
                //设置图片集合
                banner.setImages(imageUrls);
                //设置banner动画效果
                banner.setBannerAnimation(Transformer.DepthPage);
                //设置标题集合（当banner样式有显示title时）
                String[] titles = new String[]{"分享砍学费", "人脉总动员", "想不到你是这样的app", "购物节，爱不单行"};
                banner.setBannerTitles(Arrays.asList(titles));
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(1500);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();

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


    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */


            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).into(imageView);

        }

    }
}

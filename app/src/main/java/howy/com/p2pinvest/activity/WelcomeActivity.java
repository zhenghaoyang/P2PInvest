package howy.com.p2pinvest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import howy.com.p2pinvest.R;
import howy.com.p2pinvest.common.ActivityManager;

/**
 * Created by Howy on 2017/6/15.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Bind(R.id.iv_welcome_icon)
    ImageView ivWelcomeIcon;
    @Bind(R.id.tv_welcome_icon)
    TextView tvWelcomeIcon;
    @Bind(R.id.tv_welcome_version)
    TextView tvWelcomeVersion;
    @Bind(R.id.rl_welcome)
    RelativeLayout rlWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        ActivityManager.getInstance().addActivity(this);

        //模拟异常
//        String str = null;
//
//        if (str.equals("abc")) {
//            Log.d("TAG", "onCreate: Str == abc ");
//        }


    }
}

package howy.com.p2pinvest.common;

/**
 * Created by Howy on 2017/6/16.
 */

public class AppNetConfig {

    //如下的IPADDRESS可直接访问尚硅谷后台的服务器及数据库，不用在本地安装tomcat及mysql数据库
//    public static final String IPADDRESS = "182.92.5.3";
//    public static final String BASE_URL = "http://" + IPADDRESS + ":8081/P2PInvest/";

    public static final String IPADDRESS = "27.158.159.73";

    public static final String BASE_URL = "http://" + IPADDRESS + ":8080/P2PInvest/";

    public static final String PRODUCT = BASE_URL + "product";

    public static final String LOGIN = BASE_URL + "login";

    public static final String INDEX = BASE_URL + "index";

    public static final String USERRIGISTER = BASE_URL + "UserRigister";

    public static final String FEEDBACK = BASE_URL + "FeedBack";

    public static final String UPDATE = BASE_URL + "update.json";



}

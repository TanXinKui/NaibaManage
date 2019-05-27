package www.xinkui.com.restaurant.network.request;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import www.xinkui.com.restaurant.bean.Admin;
import www.xinkui.com.restaurant.bean.Advertisement;
import www.xinkui.com.restaurant.bean.DishState;
import www.xinkui.com.restaurant.bean.Recharge;
import www.xinkui.com.restaurant.bean.SellState;
import www.xinkui.com.restaurant.bean.SetAdvertisement;
import www.xinkui.com.restaurant.bean.TranscationDetail;
import www.xinkui.com.restaurant.network.response.Response;
import io.reactivex.Observable;
/**
*@description
*@author TONXOK
*@time 2019/5/5 16:37
*/
public interface Request {
    /**
     *  填上需要访问的服务器地址
     * */
    String HOST = "http://www.tanxinkui.cn:8080/ordering/";

    /**
     * 用户登录接口
     * */
    @POST("QueryAdmin")
    Observable<Response<String>> userLogin(@Body Admin admin);
    /**
     * 商家用户修改密码
     * */
    @POST("AdminModify")
    Observable<Response<String>> userModify(@Body Admin admin);
    /**
     * 商家为用户充值
     * */
    @POST("Recharge")
    Observable<Response<String>> recharge(@Body Recharge recharge);
    /**
     * 查询dish状态
     */
    @GET("GetDishState")
    Observable<Response<ArrayList<DishState>>> getDishState();
    /**
     * 设置商家已经查看
     */
    @POST("StateManage")
    Observable<Response<String>> setStateManage(@Query("deskNum") int deskNum,@Query("stateNum") int stateNum,@Query("stateKind") int stateKind);
    /**
     * 设置订单完成
     */
    @POST("Finish")
    Observable<Response<String>> setFinishState(@Query("deskNum") int deskNum);
    /**
     * 获取订单详情
     */
    @POST("TranscationDetail")
    Observable<Response<TranscationDetail>> getTranscationDetail(@Query("deskNum") int deskNum);
    /**
     * 展示sellState
     */
    @GET("SellState")
    Observable<Response<ArrayList<SellState>>> getSellState();
    /**
     * 展示商家广告
     */
    @GET("AdvertisementAll")
    Observable<Response<ArrayList<Advertisement>>> getAdvertisementInfo();
    /**
     * 设置广告
     */
    @POST("SetAdvertisement")
    Observable<Response<String>> setAdvertisementInfo(@Body SetAdvertisement advertisement);
    /**
     * 设置出售状态
     */
    @POST("SetSellState")
    Observable<Response<String>> setSellState(@Body SellState sellState);
}

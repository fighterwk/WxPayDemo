package net.sourceforge.simcpux;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * @Description描述:
 * @Author作者: Kyle
 * @Date日期: 2018/5/15
 */
public class WxPayUtil {

    private static String APP_ID = "";

    /**
     * 注册应用到微信客户端(建议每次初始化的时候调用)
     *
     * @param context
     * @param appId
     */
    public static void registerApp(Context context, String appId) {
        APP_ID = appId;
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, APP_ID);
        iwxapi.registerApp(APP_ID);
    }

    public static void pay(Context context,
                           String appId,
                           String partnerId,
                           String prepayId,
                           String nonceStr,
                           String timeStamp,
                           String packageValue,
                           String sign) {
        PayReq req = new PayReq();
        req.appId = appId;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.packageValue = packageValue;
        req.sign = sign;
        req.extData = "app data"; // optional
        pay(context, req);
    }

    /**
     * {
     * appid: "wx017d1f002a9fb187",
     * noncestr: "b53750516d274d479057911e1c0b1a6d",
     * package: "Sign=WXPay",
     * partnerid: "1507022481",
     * prepayid: "wx08161813832779ea6ced4ff03278511897",
     * sign: "5ABB26E34D8BCA882123C9CF3153290C",
     * timestamp: "1528445894"
     * }
     *
     * @param context
     * @param jsonStr
     * @throws Exception
     */
    public static void pay(Context context, String jsonStr) throws Exception {
        JSONObject json = new JSONObject(jsonStr);
        if (null != json && !json.has("retcode")) {
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("sign");
            req.extData = "app data"; // optional
            pay(context, req);
        } else {
            throw new IllegalAccessException(json.getString("retmsg"));
        }
    }


    public static void pay(Context context, PayReq req) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, req.appId, true);

        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(context, "当前设备未安装微信应用", Toast.LENGTH_SHORT).show();
            return;
        }

        iwxapi.registerApp(req.appId);
        iwxapi.sendReq(req);
    }

    public static String getAppId() {
        return APP_ID;
    }
}

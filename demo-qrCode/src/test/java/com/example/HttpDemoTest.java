package com.example;

import com.example.encry.MrAzuEncryptionUtil;
import com.example.encry.Utils;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by justin on 2017/6/29.
 */
public class HttpDemoTest {

  //  private static String baseUrl = "http://116.228.83.18:33333/";
 private static String baseUrl = "http://payapp.midaipay.com/";

    @Test
    //商户报备接口测试
    public void test1() {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "insData");                                    // 固定值：insData
            json.put("instCode", "123123123");                                   // 所属收单机构
            json.put("mcc32", "3320");                                  // 收单机构32域
            json.put("traderAreaCode", "0330000330200");                                   // 商户号地区编码
            json.put("mcc42", "5411");                                  // 42域MCC
            json.put("busLicenseName", "大轮啊aa");                                 // 商户工商注册名称
            json.put("busName", "大轮啊啊aa");                                // 营业名称
            json.put("busLicense", "3302060574435821");                                 // 营业执照号码
            json.put("busScope", "大胎啊、小啊轮胎a");                                   // 经营范围
            json.put("mainbusiness", "轮胎a");                                   // 主营业务
            json.put("legalName", "刘诗啊");                                  // 法人代表
            json.put("cerType", "01");                                //法人证件类型，默认01
            json.put("cerNum", "110101024303060335");                                  // 法人代表身份证号
            json.put("contactName", "刘诗啊");                                  // 法人代表名称
            json.put("contactPhone", "18000003349");                                   // 商户联系人电话
            json.put("addCountryCode", "CHN");                                  // 商户注册地址国家代码：默认CHN
            json.put("addCoProvinceCode", "330000");                                    // 商户注册地址省代码
            json.put("addCoCityCode", "330200");                                   // 商户注册地址市代码
            json.put("addCoAreaCode", "330211");                                   // 商户注册地址区县代码
            json.put("reqAddress", "四川成都城皇宫");                                 // 商户注册地址
            json.put("settleWay", "1");                                  // 商户结算途径，默认1
            json.put("settleCycle", "0");                                 // 商户结算周期，默认0
            json.put("chargeType", "00");                                  // 商户计费类型，默认00
            json.put("chargingGrade", "0");                                   // 商户计费档次，默认0
            json.put("accountName", "刘诗啊");                                 // 结算账户名称
            json.put("bankNum", "620001000020224039");                                 // 结算账户帐号
            json.put("bankCodeThreeCode", "307");                                     // 结算账户开户行代码
            json.put("bankName", "三国银行");                                    // 结算账户开户行名称
            json.put("bankCode", "307305027511");                                     // 结算账户开户行支付系统行号
            json.put("holidaysSettle", "是");                    // 节假日合并结算

            json.put("wxRfeeType", "0");                                    //微信手续费收取方式，默认0
            json.put("wxRrate", "0.006");                                  //微信手续费收取额度
            json.put("wxRtop", "0");                                   //微信手续费额度封顶
            json.put("wxRrateMin", "0");                                   //微信手续费额度保底

            json.put("zfbRfeeType", "0");                                    //支付宝手续费收取方式，默认0
            json.put("zfbRrate", "0.006");                                  //支付宝手续费收取额度
            json.put("zfbRtop", "0");                                   //支付宝手续费额度封顶
            json.put("zfbRrateMin", "0");                                   //支付宝手续费额度保底
            JSONObject requestJson = new JSONObject();
            System.out.println(json.toString());
            requestJson.put("data", MrAzuEncryptionUtil.getThreeEncry(URLEncoder.encode(json.toString(), "UTF-8")));
            String sync = MrAzuEncryptionUtil.encodeMD5(URLEncoder.encode(json.toString(), "UTF-8"));//MD5完整性验证加密
            requestJson.put("sync", sync);
            System.out.println(URLDecoder.decode(request(baseUrl + "channel/auth", requestJson.toString()), "UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Test
    //签到接口测试
    public void test2() {
        try {
            JSONObject json = new JSONObject();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String sendTime = sf.format(new Date());
            json.put("payPass","1" );   //1-微信， 2-支付宝
            json.put("sendTime", sendTime);   //              发送时间yyyyMMddHHmmss
            json.put("sendSeqId", "ZFBTest_0006");   //             发送流水号
            json.put("transType", "A001");
            json.put("organizationId", "123123123");


            JSONObject requestJson = new JSONObject();
            requestJson.put("data", MrAzuEncryptionUtil.getThreeEncry(URLEncoder.encode(json.toString(), "UTF-8")));
            String sync = MrAzuEncryptionUtil.encodeMD5(URLEncoder.encode(json.toString(), "UTF-8"));//MD5完整性验证加密
            requestJson.put("sync", sync);

            System.out.println(URLDecoder.decode(request(baseUrl + "/channel/signForMacKey", requestJson.toString()), "UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Test
    //二维码接口测试
    public void test3() {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String sendTime = sf.format(new Date());
            JSONObject json = new JSONObject();
            json.put("sendTime", sendTime);
            json.put("sendSeqId", "ZFBTest_0025");
            json.put("transType", "B001");
//            json.put("transType", "Z001");
            json.put("organizationId", "123123123");
            json.put("payPass", "2");
            json.put("transAmt", "2");
            json.put("fee", "1");
            json.put("cardNo", "6217000010080916292");
            json.put("name", "刘备");
            json.put("idNum", "110228199005090919");
            json.put("body", "罗技鼠标");
            json.put("notifyUrl", "http://60.205.113.137:8080/payform/payform");
            json.put("mobile", "15001120301");
             String makeMac = Utils.makeMac(json.toString(), "B339A1E9676EC03FD1BF797E831912D1");
            json.put("mac", makeMac);//报文鉴别码


            JSONObject requestJson = new JSONObject();
            requestJson.put("data", MrAzuEncryptionUtil.getThreeEncry(URLEncoder.encode(json.toString(), "UTF-8")));
            String sync = MrAzuEncryptionUtil.encodeMD5(URLEncoder.encode(json.toString(), "UTF-8"));//MD5完整性验证加密
            requestJson.put("sync", sync);


            System.out.println(URLDecoder.decode(request(baseUrl + "/channel/getQRCode", requestJson.toString()), "UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    @Test
    //二维码交易状态查询接口
    public void test5() {
        try {
            JSONObject json = new JSONObject();
            json.put("sendSeqId", "ZFBTest_0024");
            json.put("transType", "B001");
            json.put("payType", "WeChat");
    //		json.put("transType", "Z001");
    //		json.put("payType", "AliPay");

            JSONObject requestJson = new JSONObject();
            requestJson.put("data", MrAzuEncryptionUtil.getThreeEncry(URLEncoder.encode(json.toString(), "UTF-8")));
            String sync = MrAzuEncryptionUtil.encodeMD5(URLEncoder.encode(json.toString(), "UTF-8"));//MD5完整性验证加密
            requestJson.put("sync", sync);


            System.out.println(URLDecoder.decode(request(baseUrl + "/channel/queryStatus", requestJson.toString()), "UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test4() {

    }


    private String request(String url, String jsonData) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                String string = response.body().string();
                //打印服务端返回结果
                System.out.println(string);
                return string;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

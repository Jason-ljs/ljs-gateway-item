package com.ljs.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.Random;

/**
 * @ClassName YanZhengMa
 * @Description: 生成验证码工具类
 * @Author 小松
 * @Date 2019/8/15
 **/
public class SmsUtil {

    /**
     * 发送短信验证码
     * @param phoneNumber
     * @param code
     * @return
     */
    public static String sendSms(String phoneNumber, String code) {

        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIDuOpBOYMYaq1","GhiewgoFQ3BnqaiPLyLTyfmVBPhKgV");
        try {
            DefaultProfile.addEndpoint("cn-hangzhou","cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient client = new DefaultAcsClient(profile);

        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("JasonItem");
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_172357028");
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }

    /**
     * 获取验证码
     * @return
     */
    public static String getCode(){

        String str = "0123456789";

        String code = "";

        for (int i = 0; i < 4; i++){
            Random random = new Random();
            code = code + str.charAt(random.nextInt(str.length()));
        }

        return  code;
    }

}

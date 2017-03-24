package com.compus.second.Utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.compus.second.Exception.InternalServerErrorException;

/**
 * Created by cai on 30/01/2017.
 */

  enum  TempelateCode {
    TEMPELATE_CODE_LOGIN("SMS_44290101"),
    TEMPELATE_CODE_REGIST("SMS_44470100"),
    TEMPELATE_CODE_RESETMOBILE("SMS_"),
    TEMPELATE_CODE_RESETPWD("SMS_33495443");
    String desc;

    TempelateCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}

public class SMSService {

      private static SingleSendSmsRequest request;
      private static IAcsClient client;

      // 发送注册验证码

    /**
     *
     * @param address
     * @param content
     * @throws ClientException
     */
    public static void sendRegistVerifyCode(String address,String content) throws ClientException {

        initServer();
        setSMSContent(address,content,TempelateCode.TEMPELATE_CODE_REGIST);
    }

        //发送登录验证码
    public static  void sendLoginVerifyCode(String mobile,String content) throws ClientException {

        initServer();
        setSMSContent(mobile,content,TempelateCode.TEMPELATE_CODE_LOGIN);

    }
        // 发送密码重置验证码
    public static  void sendResetPwdVerifyCode(String mobile,String content) throws  ClientException{

        initServer();
        setSMSContent(mobile,content,TempelateCode.TEMPELATE_CODE_RESETPWD);
    }


    // 发送手机号重置验证码
    public static  void sendMobileResetVerifyCode(String mobile,String content) throws ClientException{

        initServer();
        setSMSContent(mobile,content,TempelateCode.TEMPELATE_CODE_RESETMOBILE);
    }

    private static void initServer() throws ClientException {

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "YUWa6JyS0EwLZfP1","V6T1YtMJDFoJzB6OrtCYHUJkU558CG");
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
         client = new DefaultAcsClient(profile);
         request = new SingleSendSmsRequest();
    }

    private static  void setSMSContent(String mobile,String content,TempelateCode tempelateCode){

        try {
            request.setSignName("蔡宏锋");
            request.setTemplateCode(tempelateCode.getDesc());
            request.setParamString("{\"name\":\"二手市场\"}");
            request.setParamString("{'no':'"+content+"'}");
            request.setRecNum(mobile);
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            return;
        } catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
        throw new InternalServerErrorException(500,"短信发送失败");

    }

    public static void sendEmailVerifyCode(String address, String subject,String body){
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "YUWa6JyS0EwLZfP1","V6T1YtMJDFoJzB6OrtCYHUJkU558CG");
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            request.setAccountName("macaque@programingmonkey.cn");
            request.setFromAlias("校园二手市场");
            request.setAddressType(1);
            request.setTagName("二手市场");
            request.setReplyToAddress(true);
            request.setToAddress(address);
            request.setSubject(subject);
            request.setHtmlBody(body);
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
            return;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        throw new InternalServerErrorException(500,"邮件发送失败");
    }

}

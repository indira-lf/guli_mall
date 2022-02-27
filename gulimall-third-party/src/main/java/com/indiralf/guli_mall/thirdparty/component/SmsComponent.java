package com.indiralf.guli_mall.thirdparty.component;

import com.indiralf.guli_mall.thirdparty.util.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @time 2022/2/27 14:52
 * @Description- TODO
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
public class SmsComponent {

    private String host;
    private String path;
    private String skin;
    private String sign;
    private String appcode;

    public void sendCode(String phone,String code){
        String method = "GET";
        String appcode = "937e19861a24c519a7548b17dc16d75";
        Map<String ,String> headers = new HashMap<>();
        headers.put("Authorization","APPCODE"+appcode);
        Map<String,String> querys = new HashMap<>();
        querys.put("code",code);
        querys.put("phone",phone);
        querys.put("skin",skin);
        querys.put("sign",sign);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

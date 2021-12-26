package com.indiralf.guli_mall.thirdparty;

import com.aliyun.oss.OSS;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallThirdPartyApplicationTests {


    @Autowired
    OSS ossClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void testUpload() throws FileNotFoundException {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = "oss-cn-shanghai.aliyuncs.com";
//// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "LTAI5tAFx7XuqqTQz1EpC4kp";
//        String accessKeySecret = "gy2NYIv6brR4DMi1z6QqLfpHitV0jY";

// 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("E:\\14.jpg");
// 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject("market-gulimall", "18.jpg", inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功");
    }

}

package com.indiralf.guli_mall.product;

import com.aliyun.oss.OSS;
import com.indiralf.guli_mall.product.service.BrandService;
import com.indiralf.guli_mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    OSS ossClient;

    //    @Autowired
//    OSS ossClient;
//    @Test
//    public void testFindPath(){
//        Long[] catelogIdPath = categoryService.findCatelogIdPath(225L);
//        log.info("完整路径:{}", Arrays.asList(catelogIdPath));
//    }

//    @Test
//    public void contextLoads(){
//        BrandEntity entity = new BrandEntity();
//        entity.setName("华为a");
//        brandService.save(entity);
//        System.out.println("保存成功");
//    }

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
        ossClient.putObject("market-gulimall", "17.jpg", inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功");
    }
}

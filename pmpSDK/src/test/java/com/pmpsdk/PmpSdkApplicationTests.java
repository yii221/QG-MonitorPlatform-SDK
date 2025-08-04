package com.pmpsdk;

import com.pmpsdk.log.LogUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PmpSdkApplicationTests {

    @Test
    void contextLoads() {


        LogUtils.info("测试");
    }

}

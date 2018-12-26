package com.yz.edu.task;
import com.yz.job.app.MyTaskApplication;
import com.yz.job.service.MendZhiMiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @描述: 5倍智米赠送
 * @作者: DuKai
 * @创建时间: 2018/6/14 17:00
 * @版本号: V1.0
 */
@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = MyTaskApplication.class)
public class MendZhimiTest {

    @Autowired
    private MendZhiMiService mendZhiMiService;

    @Test
    public void testMenZhimi() {
        //mendZhiMiService.referralPidMend();
    }

}

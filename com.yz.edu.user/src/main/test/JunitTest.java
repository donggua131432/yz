import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.dao.UsTaskCardMapper;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yz.app.MyUserApplication;
import com.yz.dao.BdsPaymentMapper;
import com.yz.model.BdPaymentClearing;
import com.yz.model.payment.BdPayInfo;
import com.yz.service.BdsPaymentCallBackService;
import com.yz.service.BdsTuitionService;
import com.yz.util.BigDecimalUtil;
import com.yz.util.ExcelUtil;

/**
 * 缴费失败手动回调
 * Description: 
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2018年5月22日.
 *
 */
@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = MyUserApplication.class)
public class JunitTest {

	@Autowired
	private BdsPaymentCallBackService callBackService;
	@Autowired
	private BdsPaymentMapper payMapper;
	
	@Autowired
	private BdsTuitionService tutionService;

	@Autowired
	private UsTaskCardMapper usTaskCardMapper;

	@Test
	@Transactional // 标明此方法需使用事务
	@Rollback(false) // 标明使用完此方法后事务不回滚,true时为回滚
	public void testLottery() {
		File file = new File("F:" + File.separator + "paymentClear.xlsx");
		InputStream in = null;
		try {
			// 根据文件创建文件的输入流
			in = new FileInputStream(file);
			// 对导入工具进行字段填充

			ExcelUtil.IExcelConfig<BdPaymentClearing> testExcelCofing = new ExcelUtil.IExcelConfig<BdPaymentClearing>();
			testExcelCofing.setSheetName("index").setType(BdPaymentClearing.class)
					.addTitle(new ExcelUtil.IExcelTitle("缴费批次号", "serialMark"))
					.addTitle(new ExcelUtil.IExcelTitle("第三方订单号", "outSerialNo"))
					.addTitle(new ExcelUtil.IExcelTitle("金额", "amount"));
			List<BdPaymentClearing> list = ExcelUtil.importWorkbook(in, testExcelCofing, "paymentClear.xlsx");
			for (BdPaymentClearing c : list) {
				// 转换为 单位；元
				String amount = BigDecimalUtil.multiply(c.getAmount(), "100");
				c.setAmount(amount);
				payMapper.insertPaymentClearing(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭输入流
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	@Test // 标明是测试方法
	@Transactional // 标明此方法需使用事务
	@Rollback(false) // 标明使用完此方法后事务不回滚,true时为回滚
	public void test() {
		List<BdPaymentClearing> list = payMapper.selectPaymentClearingUncheck();
		for (BdPaymentClearing map : list) {
			try {
				callBackService.paySuccess(map.getSerialMark(), map.getOutSerialNo(), null, map.getAmount());
				payMapper.updatePaytime(map.getSerialMark());
			} catch (Exception e) {
				payMapper.updatePaymentClearingStatus(map.getClearId(), "3");
			}
			payMapper.updatePaymentClearingStatus(map.getClearId(), "2");
		}
		/*
		 * BdStudentSerial serial = new BdStudentSerial();
		 * serial.setLearnId("457743676853832041"); serial.setStdName("黄雪如");
		 * serial.setSerialNo("HZ20180413212019001020"); String[] itemNames =
		 * new String[]{"Y1第一年学费","S1第一年书费","W1第一年网络费"};
		 * 
		 * callbackService.sendWechatMsg(serial, "2150.00", itemNames);
		 */
	}


	@Test
	public void insertUsTask(){
		Map map = new HashMap();
		map.put("userId","81780");
		map.put("taskId","25621420373443765");
		map.put("completeCount","1");
		map.put("completeStatus","0");
		map.put("completeTime", DateUtil.getNowDateAndTime());
		usTaskCardMapper.updateUsTaskCard(map);

		int count = usTaskCardMapper.selectUsTaskCardDetailCount("81780","25621420373443764","10001");
		System.out.println(count<=0);
	}
}

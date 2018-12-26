package git_bms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.app.BmsApplication;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.transfer.BdStudentChangeMapper;
import com.yz.model.communi.Body;
import com.yz.model.recruit.BdLearnRules;
import com.yz.model.transfer.BdStudentChangeExcel;
import com.yz.model.transfer.BdStudentChangeMap;
import com.yz.service.transfer.BdStudentChangeService;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BmsApplication.class)
@WebAppConfiguration
public class JuniTest1 {

	@Autowired
	private BdStudentChangeMapper changeMapper;

	@Autowired
	private BdStudentChangeService changeService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Reference(version = "1.0")
	private AtsAccountApi accountApi;
	
	@Test
	public void test111(){
		Body body = new Body();
		accountApi.trans(body);
	}

	@Test
	public void stdChange() {

		File file = new File("F:" + File.separator + "1803级需转为1809级学员名单（163人）.xlsx");
		InputStream in = null;
		try {
			// 根据文件创建文件的输入流
			in = new FileInputStream(file);
			// 对导入工具进行字段填充

			ExcelUtil.IExcelConfig<BdStudentChangeExcel> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentChangeExcel>();
			testExcelCofing.setSheetName("index").setType(BdStudentChangeExcel.class)
					.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("报读院校", "unvsName"))
					.addTitle(new ExcelUtil.IExcelTitle("报读层次", "pfsnLevel"))
					.addTitle(new ExcelUtil.IExcelTitle("报读专业", "pfsnName"));

			List<BdStudentChangeExcel> list = ExcelUtil.importWorkbook(in, testExcelCofing, "1803级需转为1809级学员名单（163人）.xlsx");
			if (null != list && list.size() > 0) {
				for (BdStudentChangeExcel map : list) {
					BdStudentChangeMap change = new BdStudentChangeMap();
					Map<String, String> learnInfo = changeMapper.selectLearnId(map.getIdCard(), "201803");
					if(null == learnInfo){
						continue;
					}
					int nextCount = changeMapper.selectLearnInfoCount(learnInfo.get("stdId"),"201809");
					if(nextCount > 0){
						continue;
					}
					
					String unvsId = changeMapper.selectUnvsId(map.getUnvsName());
					String pfsnLevel = dictExchangeUtil.getParamValue("pfsnLevel", map.getPfsnLevel());
					String pfsnId = changeMapper.selectPfsnId(unvsId,map.getPfsnName(),pfsnLevel,"201809");
					
					if(!StringUtil.hasValue(learnInfo.get("learnId"))){
						System.err.println("---------------------- 找不到学业：" + map.getIdCard());
						continue;
					}
					
					if(!StringUtil.hasValue(pfsnId)){
						System.err.println("---------------------- 找不到专业：" + map.getIdCard());
						continue;
					}
					
					if(!StringUtil.hasValue(unvsId)){
						System.err.println("---------------------- 找不到院校：" + learnInfo.get("learnId"));
						continue;
					}
					
					change.setGrade("201809");
					change.setOldLearnId(learnInfo.get("learnId"));
					change.setOldStdStage(learnInfo.get("stdStage"));
					change.setPfsnId(pfsnId);
					change.setPfsnLevel(pfsnLevel);
					change.setReason("163名需从1803级国开转报为1809级国开学员处理");
					change.setScholarship(learnInfo.get("scholarship"));
					change.setRecruitType(learnInfo.get("recruitType"));
					change.setStdId(learnInfo.get("stdId"));
					change.setTaId(learnInfo.get("taId"));
					change.setUnvsId(unvsId);
					
					BdLearnRules learnRules = new BdLearnRules();
					learnRules.setRecruit(learnInfo.get("recruit"));
					learnRules.setRecruitCampusId(learnInfo.get("recruitCampusId"));
					learnRules.setRecruitCampusManager(learnInfo.get("recruitCampusManager"));
					learnRules.setRecruitDpId(learnInfo.get("recruitDpId"));
					
					changeService.addBdStudentChange(change, learnRules);
				}
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

}

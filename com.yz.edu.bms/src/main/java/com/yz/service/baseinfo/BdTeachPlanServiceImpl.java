package com.yz.service.baseinfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.baseinfo.BdPlanTextbookMapper;
import com.yz.dao.baseinfo.BdTeachPlanMapper;
import com.yz.model.baseinfo.BdPlanTextbookKey;
import com.yz.model.baseinfo.BdTeachPlan;
import com.yz.model.baseinfo.BdTeachPlanMap;
import com.yz.model.baseinfo.BdUniversity;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.baseinfo.TUPDemo;
import com.yz.model.educational.BdTextBook;
import com.yz.service.educational.BdTextBookService;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Transactional
@Service
public class BdTeachPlanServiceImpl {

	@Autowired
	private BdTeachPlanMapper teachPlanMapper;
	@Autowired
	private BdTextBookService textBookService;
	@Autowired
	private BdPlanTextbookMapper planTextbookMapper;

	@Autowired
	private BdUnvsProfessionServiceImpl professionService;
	@Autowired
	private BdUniversityServiceImpl bdUniversityService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	public BdTeachPlan getTeachPlan(String teachPlanId) {
		// TODO Auto-generated method stub
		return teachPlanMapper.selectByPrimaryKey(teachPlanId);
	}

	public List<BdTeachPlanMap> selectAll(TUPDemo postValue) {
		// TODO Auto-generated method stub
		List<BdTeachPlanMap> resultList = teachPlanMapper.selectAll(postValue);
		for (BdTeachPlanMap bdTeachPlanMap : resultList) {
			List<BdTextBook> textBook = textBookService.findTextBookByThpId(bdTeachPlanMap.getThpId());
			bdTeachPlanMap.setTextbook(textBook);
		}
		return resultList;
	}

	public Map<String, Object> selectOne(String teachPlanId) {
		// TODO Auto-generated method stub
		return teachPlanMapper.selectOne(teachPlanId);
	}

	public void updateTeachPlan(BdTeachPlan teachPlan) {
		// TODO Auto-generated method stub
		teachPlanMapper.updateByPrimaryKeySelective(teachPlan);
	}

	public void addTeachPlan(BdTeachPlan teachPlan) {
		// TODO Auto-generated method stub
		teachPlanMapper.insertSelective(teachPlan);
	}

	public int getCount(String pfsnId, String semester) {
		// TODO Auto-generated method stub
		return teachPlanMapper.getCount(pfsnId, semester);
	}

	public void deleteTeachPlan(String[] idArray) {
		// TODO Auto-generated method stub
		teachPlanMapper.deleteTeachPlan(idArray);
	}

	public void deleteTeachPlanOne(String id) {
		// TODO Auto-generated method stub
		teachPlanMapper.deleteByPrimaryKey(id);
	}

	public List<BdTeachPlan> findTeachPlan(String sName) {
		// TODO Auto-generated method stub
		return teachPlanMapper.findTeachPlan(sName);
	}

	public boolean isTeachPlanExit(BdTeachPlan teachPlan) {
		// TODO Auto-generated method stub
		return teachPlanMapper.isTeachPlanExit(teachPlan);
	}

	public BdTeachPlan selectTeachPlan(BdTeachPlan teachPlan) {
		// TODO Auto-generated method stub
		return teachPlanMapper.selectTeachPlan(teachPlan);
	}

	public void insertPlanTextbook(BdPlanTextbookKey planTextbookKey) {
		// TODO Auto-generated method stub
		boolean isExit = planTextbookMapper.selectPlanTextbook(planTextbookKey);
		if (!isExit)
			planTextbookMapper.insertSelective(planTextbookKey);
	}

	public void uploadTeachPlan(MultipartFile excelTeachPlan) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdTeachPlan> testExcelCofing = new ExcelUtil.IExcelConfig<BdTeachPlan>();
		testExcelCofing.setSheetName("index").setType(BdTeachPlan.class)
				.addTitle(new ExcelUtil.IExcelTitle("院校名称", "ext1"))
				.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnId"))
				.addTitle(new ExcelUtil.IExcelTitle("专业编码", "pfsnCode"))
				.addTitle(new ExcelUtil.IExcelTitle("专业层次", "ext2")).addTitle(new ExcelUtil.IExcelTitle("年级", "ext3"))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划名称", "thpName"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
				.addTitle(new ExcelUtil.IExcelTitle("学分", "credit"))
				.addTitle(new ExcelUtil.IExcelTitle("类型", "thpType"))
				.addTitle(new ExcelUtil.IExcelTitle("总学时", "totalHours"))
				.addTitle(new ExcelUtil.IExcelTitle("面授学时", "educatedHour"))
				.addTitle(new ExcelUtil.IExcelTitle("自学学时", "selfHours"))
				.addTitle(new ExcelUtil.IExcelTitle("实践学时", "practiceHours"));
		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<BdTeachPlan> list = ExcelUtil.importWorkbook(excelTeachPlan.getInputStream(), testExcelCofing,
					excelTeachPlan.getOriginalFilename());

			// 遍历插入
			for (BdTeachPlan teachPlan : list) {
				// 教学计划id = [院校类型]院校名称:专业编码与名称[专业层次]
				String teachPlanId = "";
				String pfsnLevelTemp = "";
				// 院校名称转id
				BdUniversity universitynew = new BdUniversity();
				universitynew.setUnvsName(teachPlan.getExt1());
				List<BdUniversity> unvsUniversityList = bdUniversityService.selectAll(universitynew);
				if (null == unvsUniversityList || unvsUniversityList.size() <= 0) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！大学院校不存在");
				}
				// 专业名称转换 根据院校名称 专业层次 年级进行确定唯一性
				BdUnvsProfession temp = new BdUnvsProfession();
				temp.setUnvsId((String) (unvsUniversityList.get(0).getUnvsId()));

				// 院校类型转换
				/*
				 * String valueTemple =
				 * DictExchangeUtil.getParamKey("recruitType",
				 * (String)(unvsUniversityList.get(0).getRecruitType()));
				 * if(null == valueTemple){ throw new
				 * IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！");
				 * }else{ teachPlanId +=
				 * valueTemple+"]"+teachPlan.getExt1()+":"; }
				 */

				// 专业层次转换
				String valueTemple = dictExchangeUtil.getParamValue("pfsnLevel", teachPlan.getExt2().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业层次错误");
				} else {
					pfsnLevelTemp = valueTemple;
					temp.setPfsnLevel(valueTemple);
				}

				// 年级转换
				valueTemple = dictExchangeUtil.getParamValue("grade", teachPlan.getExt3().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级错误");
				} else {
					teachPlan.setGrade(valueTemple);
					temp.setGrade(valueTemple);
				}

				// 学期转换
				valueTemple = teachPlan.getSemester();
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学期不能为空");
				} else {
					teachPlan.setSemester(valueTemple);
				}

				temp.setPfsnName(teachPlan.getPfsnId());

				if (!StringUtil.hasValue(teachPlan.getPfsnCode())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业编码不能为空");
				}
				temp.setPfsnCode(teachPlan.getPfsnCode());
				// 获取专业id
				List<BdUnvsProfession> unvsProfessionList = professionService.selectAll(temp);
				if (null == unvsProfessionList || unvsProfessionList.size() <= 0) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业不存在");
				}
				teachPlan.setPfsnId(unvsProfessionList.get(0).getPfsnId());
				int thpCount = teachPlanMapper.selectTeachPlanCount(teachPlan);
				if (thpCount > 0) {
					continue;
				}
				int order = getCount(unvsProfessionList.get(0).getPfsnId(), teachPlan.getSemester());
				String unvsCode = teachPlanMapper.selectUnvsCodeByPfsnId(unvsProfessionList.get(0).getPfsnId());
				teachPlanId += unvsCode + "_" + unvsProfessionList.get(0).getPfsnCode() + "_" + teachPlan.getGrade()
						+ "_" + teachPlan.getSemester() + "_" + (order + 1 + 10);

				String thpType = teachPlan.getThpType();
				if (StringUtil.hasValue(thpType)) {
					thpType = thpType.trim();
				}

				// 类型转换
				valueTemple = dictExchangeUtil.getParamValue("thpType", thpType);
				if (null != valueTemple) {
					teachPlan.setThpType(valueTemple);
				}

				teachPlan.setExt1("");
				teachPlan.setExt2("");
				teachPlan.setExt3("");

				teachPlan.setThpId(teachPlanId);

				addTeachPlan(teachPlan);
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}

	public String selectPfsnIdByCon(String grade, String pfsnName, String unvsName, String pfsnLevel, String pfsnCode) {
		return teachPlanMapper.selectPfsnIdByCon(grade, pfsnName, unvsName, pfsnLevel, pfsnCode);
	}

}

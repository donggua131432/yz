package com.yz.service.stdService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.stdService.StudentGraduateDataMapper;
import com.yz.dao.system.SysCityMapper;
import com.yz.dao.system.SysDistrictMapper;
import com.yz.dao.system.SysProvinceMapper;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdStudentSendMap;
import com.yz.model.gk.BdStudentExamGKExcel;
import com.yz.model.statistics.PfsnMatchBookStatInfo;
import com.yz.model.stdService.StudentGraduateDataInfo;
import com.yz.model.stdService.StudentGraduateDataQuery;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.service.educational.BdStudentSendService;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
/**
 * 学员服务---毕业资料
 * @author lx
 * @date 2018年1月26日 下午2:58:01
 */
@Service
@Transactional
public class StudentGraduateDataService
{
	private static Logger log = LoggerFactory.getLogger(StudentGraduateDataService.class);
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private StudentGraduateDataMapper graduateDataMapper;
	
	@Autowired
	private SysProvinceMapper provinceMapper;

	@Autowired
	private SysCityMapper cityMapper;

	@Autowired
	private SysDistrictMapper districtMapper;
	
	@Autowired
	private StuGraduateDataAysnImportService aysnImportGraduateData;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object queryStuGraduateDataList(int page,int pageSize,StudentGraduateDataQuery query){
		PageHelper.offsetPage(page, pageSize);
		BaseUser user  =SessionUtil.getUser();
		//TODO 数据权限
		List<String> jtList = user.getJtList();
		if(jtList.contains("FDY")){ //班主任
			user.setUserLevel("4"); //只看自己
		}
		if(jtList.contains("CJXJ") || jtList.contains("GKXJ") || jtList.contains("BMZR")){
			user.setUserLevel("1");
		}
		
		List<StudentGraduateDataInfo> list = graduateDataMapper.queryStuGraduateDataList(query,user);
		
		if(null !=list && list.size() >0){
			for(StudentGraduateDataInfo dataInfo : list){
				SysProvince province = provinceMapper.selectByPrimaryKey(dataInfo.getProvinceCode());
				SysCity city = cityMapper.selectByPrimaryKey(dataInfo.getCityCode());
				SysDistrict district = districtMapper.selectByPrimaryKey(dataInfo.getDistrictCode());
				StringBuilder sb = new StringBuilder();
				if (null != province) {
					sb.append(province.getProvinceName());
				}
				if (null != city) {
					sb.append(city.getCityName());
				}
				if (null != district) {
					sb.append(district.getDistrictName());
				}
				dataInfo.setAddress(sb.toString()+dataInfo.getAddress());
			}
		}
		
		return new IPageInfo<>((Page)list);
	}
	
	public StudentGraduateDataInfo getGraduateDataById(String id){
		StudentGraduateDataInfo dataInfo  =graduateDataMapper.getGraduateDataById(id);
		if(null != dataInfo){
			SysProvince province = provinceMapper.selectByPrimaryKey(dataInfo.getProvinceCode());
			SysCity city = cityMapper.selectByPrimaryKey(dataInfo.getCityCode());
			SysDistrict district = districtMapper.selectByPrimaryKey(dataInfo.getDistrictCode());
			StringBuilder sb = new StringBuilder();
			if (null != province) {
				sb.append(province.getProvinceName());
			}
			if (null != city) {
				sb.append(city.getCityName());
			}
			if (null != district) {
				sb.append(district.getDistrictName());
			}
			dataInfo.setAddress(sb.toString()+dataInfo.getAddress());
			
			if(!StringUtil.hasValue(dataInfo.getIfMail())){
				dataInfo.setIfMail("0");
			}
			if(!StringUtil.hasValue(dataInfo.getIfPass())){
				dataInfo.setIfPass("0");
			}
		}
		return dataInfo;
	}
	
	public void editStuGraduateData(StudentGraduateDataInfo info){
		graduateDataMapper.editStuGraduateData(info);
	}
	
	@SuppressWarnings("unchecked")
	public void importStuGraduateDataInfo(MultipartFile stuGraduateDataImport){
		//对导入工具进行字段填充
		ExcelUtil.IExcelConfig<StudentGraduateDataInfo> testExcelCofing = new ExcelUtil.IExcelConfig<StudentGraduateDataInfo>();
		testExcelCofing.setSheetName("index").setType(StudentGraduateDataInfo.class)
		        .addTitle(new ExcelUtil.IExcelTitle("学籍编号", "schoolRoll"))
		        .addTitle(new ExcelUtil.IExcelTitle("学号", "stdNo"))
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("是否提交", "ifSubmit"))
				.addTitle(new ExcelUtil.IExcelTitle("是否合格", "ifPass"))
				.addTitle(new ExcelUtil.IExcelTitle("不合格原因", "noPassReason"))
				.addTitle(new ExcelUtil.IExcelTitle("快递单号", "expressNo"))
				.addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));	
		
		 // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<StudentGraduateDataInfo> list = ExcelUtil.importWorkbook(stuGraduateDataImport.getInputStream(), testExcelCofing,
            		stuGraduateDataImport.getOriginalFilename());
            // 遍历插入
            for (StudentGraduateDataInfo stuGraduateData : list) {
                if (!StringUtil.hasValue(stuGraduateData.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(stuGraduateData.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学员姓名不能为空");
                }
                if (!StringUtil.hasValue(stuGraduateData.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
                }
                if (!StringUtil.hasValue(stuGraduateData.getStdNo())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学号不能为空");
                }
                if(StringUtil.hasValue(stuGraduateData.getIfSubmit())){
                	if(stuGraduateData.getIfSubmit().equals("是")){
                		stuGraduateData.setIfSubmit("1");
                	}else{
                		stuGraduateData.setIfSubmit("0");
                	}
                }
                if(StringUtil.hasValue(stuGraduateData.getIfPass())){
                	if(stuGraduateData.getIfPass().equals("合格")){
                		stuGraduateData.setIfPass("1");
                	}else{
                		stuGraduateData.setIfPass("0");
                	}
                }
                index++;

            }
            List<Map<String, Object>> resultList = graduateDataMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("表格信息找不到对应学员信息：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("grade") + "-" + map.get("std_name") + "-" + map.get("id_card").toString() + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            if(null != list && list.size()>0){
				if(list.size() >10000){
					throw new BusinessException("E000107"); // 目标年级已存在报读信息
				}
				aysnImportGraduateData.importStuGraduateDataAsyn(list);
			}
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
	}
	
	@SuppressWarnings("unchecked")
	public void exportStuGraduateData(StudentGraduateDataQuery queryMap, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<StudentGraduateDataInfo> testExcelCofing = new ExcelUtil.IExcelConfig<StudentGraduateDataInfo>();
		testExcelCofing.setSheetName("index").setType(StudentGraduateDataInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
				.addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("是否提交", "ifSubmit"))
				.addTitle(new ExcelUtil.IExcelTitle("是否合格", "ifPass"))
				.addTitle(new ExcelUtil.IExcelTitle("不合格原因", "noPassReason"))
				.addTitle(new ExcelUtil.IExcelTitle("是否邮寄", "ifMail"))
				.addTitle(new ExcelUtil.IExcelTitle("邮寄地址和电话", "exportAddress"))
				.addTitle(new ExcelUtil.IExcelTitle("快递单号", "expressNo"))
				.addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));
		
		BaseUser user = SessionUtil.getUser();
		
		List<StudentGraduateDataInfo> list = graduateDataMapper.queryStuGraduateDataList(queryMap, user);

		for (StudentGraduateDataInfo dataInfo : list) {

			String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", dataInfo.getPfsnLevel());
			
			dataInfo.setPfsnLevel(pfsnLevel);
			if (StringUtil.hasValue(dataInfo.getUserName())) {
					SysProvince province = provinceMapper.selectByPrimaryKey(dataInfo.getProvinceCode());
					SysCity city = cityMapper.selectByPrimaryKey(dataInfo.getCityCode());
					SysDistrict district = districtMapper.selectByPrimaryKey(dataInfo.getDistrictCode());
					StringBuilder sb = new StringBuilder();
					if (null != province) {
						sb.append(province.getProvinceName());
					}
					if (null != city) {
						sb.append(city.getCityName());
					}
					if (null != district) {
						sb.append(district.getDistrictName());
					}
					dataInfo.setExportAddress(dataInfo.getUserName() + "--" + sb.toString() + dataInfo.getAddress()
							+ "--" + dataInfo.getMobile());
			}
			//是否提交
			if(StringUtil.hasValue(dataInfo.getIfSubmit())){
				if(dataInfo.getIfSubmit().equals("1")){
					dataInfo.setIfSubmit("已提交");
				}else{
					dataInfo.setIfSubmit("未提交");
				}
			}else{
				dataInfo.setIfSubmit("未提交");
			}
			//是否合格
			if(StringUtil.hasValue(dataInfo.getIfPass())){
				if(dataInfo.getIfPass().equals("1")){
					dataInfo.setIfPass("合格");
				}else{
					dataInfo.setIfPass("不合格");
				}
			}else{
				dataInfo.setIfPass("不合格");
			}
			//是否邮寄
			if(StringUtil.hasValue(dataInfo.getIfMail())){
				if(dataInfo.getIfMail().equals("1")){
					dataInfo.setIfMail("要邮寄");
				}else{
					dataInfo.setIfMail("不邮寄");
				}
			}else{
				dataInfo.setIfMail("不邮寄");
			}
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=stuGraduateData.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
}

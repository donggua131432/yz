package com.yz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.CheckConstants;
import com.yz.constants.EducationConstants;
import com.yz.constants.OrderConstants;
import com.yz.constants.StudentConstants;
import com.yz.dao.BdStudentSendMapper;
import com.yz.dao.StudentAllMapper;
import com.yz.model.BdStudentBaseInfo;
import com.yz.model.educational.BdStudentSend;
import com.yz.util.Assert;

@Service
@Transactional
public class BdsStudentSendService {

	@Autowired
	private BdStudentSendMapper studentSendMapper;

	/**
	 * 缴费完成后发书
	 * 
	 * @param learnId
	 *            学员ID
	 * @param semester
	 *            学期
	 * @param isFD
	 *            false为非辅导教材 true 为辅导教材
	 */
	public void sendBook(String learnId, String year, boolean isFD) {
		Assert.hasText(learnId, "参数learnId不能为空");
		if (!isFD) {
			if (StudentConstants.YEAR_ONE.equals(year)) {
				// 如果是第一年则生成第一学期第二学期的教材发放
				initBookSend(learnId, StudentConstants.SEMESTER_ONE, StudentConstants.SEMESTER_TWO, isFD);
			} else if (StudentConstants.YEAR_TWO.equals(year)) {
				// 如果是第一年则生成第三学期第四学期的教材发放
				initBookSend(learnId, StudentConstants.SEMESTER_THREE, StudentConstants.SEMESTER_FOUR, isFD);
			} else if (StudentConstants.YEAR_THREE.equals(year)) {
				// 如果是第一年则生成第五学期第六学期的教材发放
				initBookSend(learnId, StudentConstants.SEMESTER_FIVE, StudentConstants.SEMESTER_SIX, isFD);
			}
		} else {
			initBookSend(learnId, null, null, isFD);
		}

	}

	@Autowired
	private StudentAllMapper stdAllMapper;

	public void initBookSend(String learnId, String arg0, String arg1, boolean isFD) {

		BdStudentSend studentSend = new BdStudentSend();
		studentSend.setLearnId(learnId);
		studentSend.setTextbookType(isFD ? EducationConstants.TEXT_BOOK_TYPE_FD : EducationConstants.TEXT_BOOK_TYPE_XK);
		studentSend.setReceiveStatus(StudentConstants.RECEIVE_STATUS_RECEIVED);
		studentSend.setOrderBookStatus(OrderConstants.ORDER_BOOK_NO);
		if (!isFD) {

			// 走班主任审核
			studentSend.setAddressStatus(CheckConstants.CHECK_TEACHER_1);

			// String userId = stdAllMapper.selectUserIdByLearnId(learnId);
			/*
			 * Map<String, String> addressInfo =
			 * usMapper.selectUsBookAddress(userId);
			 * studentSend.setAddress(addressInfo.get("address"));
			 * studentSend.setMobile(addressInfo.get("mobile"));
			 * studentSend.setUserName(addressInfo.get("saName"));
			 * studentSend.setProvinceCode(addressInfo.get("provinceCode"));
			 * studentSend.setCityCode(addressInfo.get("cityCode"));
			 * studentSend.setDistrictCode(addressInfo.get("districtCode"));
			 */

			// 插入第一学期发书记录
			studentSend.setSemester(arg0);
			studentSendMapper.insertSelective(studentSend);

			// 插入第二学期发书记录
			studentSend.setSemester(arg1);
			studentSendMapper.insertSelective(studentSend);
			studentSendMapper.insertBdTextBookSend(studentSend.getSendId(), learnId);
		} else {
			// 不走班主任审核
			studentSend.setAddressStatus(CheckConstants.CHECK_SENATE_4);

			BdStudentBaseInfo stdInfo = stdAllMapper.getStudentBaseInfoByLearnId(learnId);
			studentSend.setAddress(stdInfo.getAddress());
			studentSend.setMobile(stdInfo.getMobile());
			studentSend.setUserName(stdInfo.getStdName());
			studentSend.setProvinceCode(stdInfo.getNowProvinceCode());
			studentSend.setCityCode(stdInfo.getNowCityCode());
			studentSend.setDistrictCode(stdInfo.getNowDistrictCode());

			studentSendMapper.insertSelective(studentSend);
			String[] testSub = studentSendMapper.selectTestSubByLearnId(learnId);

			int subCount = studentSendMapper.selectTestBookCount(testSub);
			if (subCount > 0) {
				studentSendMapper.insertBdTextBookSendFD(studentSend.getSendId(), testSub);
			}

		}
		// 插入教材关联
	}

	public void updateStdBookReceive(String learnId, String saName, String mobile, String address, String provinceCode,
			String cityCode, String districtCode) {
		studentSendMapper.updateBookReceiveAddressAndStatus(learnId, saName, mobile, address, provinceCode, cityCode,
				districtCode);
	}

}

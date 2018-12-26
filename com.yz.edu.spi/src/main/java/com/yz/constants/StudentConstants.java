package com.yz.constants;

public class StudentConstants {
	
	/**
	 * 启用、禁用状态
	 */
	public static final String FEE_STATUS_BLOCK = "2";
	public static final String FEE_STATUS_START = "1";

	/** 学员阶段-意向学员 */
	public static final String STD_STAGE_PURPOSE = "1";
	/** 学员阶段-考前辅导 */
	public static final String STD_STAGE_HELPING = "2";
	/** 学员阶段-考前确认 */
	public static final String STD_STAGE_CONFIRM = "3";
	/** 学员阶段-入学考试 */
	public static final String STD_STAGE_TESTING = "4";
	/** 学员阶段-录取学员 */
	public static final String STD_STAGE_ENROLLED = "5";
	/** 学员阶段-注册学员 */
	public static final String STD_STAGE_REGISTER = "6";
	/** 学员阶段-在读学员 */
	public static final String STD_STAGE_STUDYING = "7";
	/** 学员阶段-毕业学员 */
	public static final String STD_STAGE_FINISH = "8";
	/** 学员阶段-留级学员 */
	public static final String STD_STAGE_STAYDOWN = "9";
	/** 学员阶段-退学学员 */
	public static final String STD_STAGE_OUT = "10";
	/** 学员阶段-其他 */
	public static final String STD_STAGE_OTHER = "11";
	/** 学员阶段-待录取 */
	public static final String STD_STAGE_UNENROLLED = "12";
	/** 学员阶段-待注册 */
	public static final String STD_STAGE_PENDINGREGISTER = "13";
	/** 学员阶段-休学学员*/
	public static final String STD_STAGE_QUITSCHOOL = "14";
	
	/** 学员类型-本校注册 */
	public static final String STD_TYPE_LOCATION = "1";
	/** 学员类型-外校注册 */
	public static final String STD_TYPE_ALIEN = "2";
	
	/** 学员附件状态-未上传*/
	public static final String ANNEX_STATUS_UNUPLOAD = "1";
	/** 学员附件状态-待审核*/
	public static final String ANNEX_STATUS_UNCHECK = "2";
	/** 学员附件状态-审核通过*/
	public static final String ANNEX_STATUS_ALLOW = "3";
	/** 学员附件状态-驳回*/
	public static final String ANNEX_STATUS_REJECT = "4";
	
	/** 学员备注类型-是否已缴费 */
	public static final String REMARK_TYPE_CHARGE = "5";
	/** 学员备注类型-是否已通知 */
	public static final String REMARK_TYPE_NOTIFY = "4";
	/** 学员备注类型-是否已加微信 */
	public static final String REMARK_TYPE_WECHAT = "1";
	/** 学员备注类型-是否已记录联系电话 */
	public static final String REMARK_TYPE_TELEPHONE = "2";
	/** 学员备注类型-是否已加QQ */
	public static final String REMARK_TYPE_QQ = "3";
	
	/** 学员附件类型-相片 */
	public static final String ANNEX_TYPE_PHOTO = "5";

	

	/** 学员附件类型-相片 */
	public static final String ANNEX_TYPE_IDCARD_FRONT = "1";
	/** 学员附件类型-相片 */
	public static final String ANNEX_TYPE_IDCARD_BEHIND = "2";
	/** 学员附件类型-相片 */
	public static final String ANNEX_TYPE_EDUCATION = "3";
	
	
	/** 招生老师附件类型-相片*/
	public static final String RECRUITER_ANNEX_TYPE_PHOTO = "4";
	
	/** 教师-照片*/
	public static final String TEACHER_ANNEX_TYPE_PHOTO = "8";
	
	/** 学期-第一学期*/
	public static final String SEMESTER_ONE = "1";
	/** 学期-第二学期*/
	public static final String SEMESTER_TWO = "2";
	/** 学期-第三学期*/
	public static final String SEMESTER_THREE = "3";
	/** 学期-第四学期*/
	public static final String SEMESTER_FOUR = "4";
	/** 学期-第五学期*/
	public static final String SEMESTER_FIVE = "5";
	/** 学期-第六学期*/
	public static final String SEMESTER_SIX = "6";
	
	
	/** 年-第一年*/
	public static final String YEAR_ONE = "1";
	/** 年-第二年*/
	public static final String YEAR_TWO = "2";
	/** 年-第三年*/
	public static final String YEAR_THREE = "3";
	
	/** 招生类型-成教*/
	public static final String RECRUIT_TYPE_CJ = "1";
	
	/** 招生类型-国家开放大学*/
	public static final String RECRUIT_TYPE_GK = "2";
	/** 招生类型-中职教育*/
	public static final String RECRUIT_TYPE_ZZ = "3";
	/** 招生类型-网络教育*/
	public static final String RECRUIT_TYPE_WL = "4";
	/** 教材收件状态 - 未收 */
	public static final String RECEIVE_STATUS_RECEIVED = "1";
	/** 教材收件状态 - 已收 */
	public static final String RECEIVE_STATUS_UNRECEIVED = "2";
	
	/** 优惠类型 - 圆梦计划 */
	public static final String SCHOLARSHIP_YUANMENG = "4";
	/** 优惠类型 - 普通全额 */
	public static final String SCHOLARSHIP_NORMARL = "1";
	/** 优惠类型 - 全额奖学金 */
	public static final String SCHOLARSHIP_FULL = "2";
	/** 优惠类型 - 东莞奖学金 */
	public static final String SCHOLARSHIP_DONGGUAN = "3";
	/** 优惠类型 - 汕尾助学 */
	public static final String SCHOLARSHIP_SHANWEI_HELP = "18";
	/** 优惠类型 - 考前辅导 */
	public static final String SCHOLARSHIP_TEST_HELP = "11";
	/** 通用邀约 */
	public static final String SCHOLARSHIP_DEFAULT = "0";
	
	/** 性别-女 */
	public static final String SEX_WOMEN = "2";
	/** 性别-男 */
	public static final String SEX_MEN = "1";
	/** 性别-未知 */
	public static final String SEX_UNKONW = "0";
	
	/** 证件类型 - 身份证 */
	public static final Object ID_TYPE_SFZ = "1";
	/** 证件类型 - 其他 */
	public static final Object ID_TYPE_OTHER = "3";
	
	/** 加分类型 - 年满25周岁 */
	public static final String BP_TYPE_25 = "3";
	/** 加分类型 - 无加分 */
	public static final String BP_TYPE_NONE = "1";
	
	/** 学员专业科目拆分符号 */
	public static final String UNVS_PROFESSIONAL_SPLIT_SYM = ",";
	
	/** 报名渠道 - 学员系统录入 */
	public static final String ENROLL_CHANNEL_LOCAL = "1";
	/** 报名渠道 - 推荐录入 */
	public static final String ENROLL_CHANNEL_RECONMMEND = "2";
	/** 报名渠道 - 邀约录入 */
	public static final String ENROLL_CHANNEL_INVITE = "3";
	
	/** 圆梦入围等状态 - 1- 待定 */
	public static final String INCLUSION_STATUS_HOLD = "1";
	
}

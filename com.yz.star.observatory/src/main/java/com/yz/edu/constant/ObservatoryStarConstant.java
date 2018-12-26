package com.yz.edu.constant;

import com.yz.edu.model.AnnotationAlarm;
import com.yz.edu.model.EsTraceAnnotation;
import com.yz.edu.model.EsTraceInfo;

public interface ObservatoryStarConstant {

	public static final String OBSERVATORY_STAR_INDEX = "yz-trace-es";

	public static final String OBSERVATORY_STAR_INFO = "es-trace-info";

	public static final String OBSERVATORY_STAR_SPAN = "es-trace-span";

	public static final String OBSERVATORY_STAR_ANNOTATION = "es-trace-annotation";

	/**
	 * @desc 超过2000毫秒
	 */
	public static final String COSTTIME_EPL = "select * from EsTraceInfo(destination>=2000)";
	
	public static final String COSTTIME_TEMPLATE = "costTimeAlarm";
	
	public static final String COSTTIME_REMARK = "调用链耗时预警";

	
	public static final String SLOW_EPL = "select new AnnotationAlarm(avg(destination),max(destination),resouceType,count(*)) from EsTraceAnnotation.win:time(60 sec) group by resouceType output snapshot every 60 seconds ";
	
	public static final String SLOW_TEMPLATE = "slowResourceAlarm";
	
	public static final String SLOW_REMARK = "慢资源预警";
	
	
	
	public enum ObserStarEnum {
		COSTTIME_ALARM(COSTTIME_EPL, COSTTIME_TEMPLATE, EsTraceInfo.class,COSTTIME_REMARK),
		SLOWRES_ALARM(SLOW_EPL, SLOW_TEMPLATE, EsTraceAnnotation.class,SLOW_REMARK,AnnotationAlarm.class); // 耗时预警

		private String epl;

		private Class<?> targetCls; // 目标类

		private String template;// 发送的预警模板
		
		private String remark; // 备注
		
		private Class<?>[] importCls;  // 需要额外导入的类

		private ObserStarEnum(String epl, String template, Class<?> cls,String remark,Class<?>...importCls) {
			this.template = template;
			this.epl = epl;
			this.targetCls = cls;
			this.remark = remark;
			this.importCls= importCls;
		}
		
		public Class<?>[] getImportCls() {
			return importCls;
		}
		
		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		public String getRemark() {
			return remark;
		}

		public void setTemplate(String template) {
			this.template = template;
		}

		public String getTemplate() {
			return template;
		}

		public String getEpl() {
			return epl;
		}

		public void setEpl(String epl) {
			this.epl = epl;
		}

		public Class<?> getTargetCls() {
			return targetCls;
		}

		public void setTargetCls(Class<?> targetCls) {
			this.targetCls = targetCls;
		}

	}

}

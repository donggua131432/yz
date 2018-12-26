package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.edu.trace.TraceAnnotation;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;

/**
 * 
 * @author Administrator
 *
 */
public interface YzTraceMapper {

	public void saveTraceInfo(@Param(value="infos")List<TraceTransfer> infos);
	
	public void saveTraceSpan(@Param(value="spans")List<TraceSpan> spans);
	
	public void saveTraceAnnotation(@Param(value="annotations")List<TraceAnnotation> annotations);
}

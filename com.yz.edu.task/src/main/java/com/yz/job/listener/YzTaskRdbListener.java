package com.yz.job.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.event.JobEventListener;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbIdentity;
import com.dangdang.ddframe.job.event.type.JobExecutionEvent;
import com.dangdang.ddframe.job.event.type.JobExecutionEventDetail;
import com.dangdang.ddframe.job.event.type.JobStatusTraceEvent;
import com.yz.job.common.YzJobInfo;
import com.yz.job.common.YzTaskContext;

/**
 * 
 * @desc taskRdbTrace
 * @author Administrator
 *
 */
@Component
public class YzTaskRdbListener extends JobEventRdbIdentity implements JobEventListener {

	private static Logger logger = LoggerFactory.getLogger(YzTaskRdbListener.class);

	@Autowired
	private DataSource dataSource;

	@Override
	public void listen(JobExecutionEvent event) {
		YzJobInfo info = YzTaskContext.getTaskContext().getYzJobInfo(event.getJobName());
		if (info == null || !info.isLog()) {
			logger.debug("{} not enable log !", event.getJobName());
			return;
		}
		YzTaskContext.getTaskContext().setJobExecutionEvent(event);
		Date date = event.getCompleteTime();
		// 完成时间
		if (date != null) // 表示任务执行完成
		{
			try {
				JobExecutionEvent newEvent = YzTaskContext.getTaskContext().getJobExecutionEvent(event);
				if(newEvent.getDetails()!=null&&!newEvent.getDetails().isEmpty())
				{
					newEvent.setSuccess(event.isSuccess()); 
					newEvent.setCompleteTime(date); 
					batchSaveJobExecutionEvent(newEvent,event.getFailureCause(),info.getTaskDesc());
				}
			} finally {
				YzTaskContext.getTaskContext().removeJobExecutionEvent(event);
			}
		}
	}

	@Override
	public void listen(JobStatusTraceEvent jobStatusTraceEvent) {

	}

	/**
	 * 
	 * @param jobExecutionEvent
	 */
	private void batchSaveJobExecutionEvent(final JobExecutionEvent jobExecutionEvent,String failureCause,String jobName) {
		long batchNo = System.nanoTime();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO common.`job_execution_log` (batch_no, `job_name`, `task_id`, `hostname`, `ip`, `sharding_item`, `execution_source`, `is_success`, `start_time`, `complete_time`,keywords,remark,failure_cause) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?);";
		try {
			conn = DataSourceUtils.doGetConnection(dataSource);
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(sql);
			int index =1 ; 
			for (JobExecutionEventDetail event : jobExecutionEvent.getDetails()) {
				preparedStatement.setLong(1, batchNo);
				preparedStatement.setString(2, jobName);
				preparedStatement.setString(3, jobExecutionEvent.getTaskId());
				preparedStatement.setString(4, jobExecutionEvent.getHostname());
				preparedStatement.setString(5, jobExecutionEvent.getIp());
				preparedStatement.setInt(6, jobExecutionEvent.getShardingItem());
				preparedStatement.setString(7, jobExecutionEvent.getSource().toString());
				preparedStatement.setBoolean(8, jobExecutionEvent.isSuccess());
				preparedStatement.setTimestamp(9, new Timestamp(jobExecutionEvent.getStartTime().getTime()));
				preparedStatement.setTimestamp(10, new Timestamp(jobExecutionEvent.getCompleteTime().getTime()));
				preparedStatement.setString(11, event.getKey());
				preparedStatement.setString(12, String.valueOf(event.getValue()));
				preparedStatement.setString(13, failureCause);
				preparedStatement.addBatch();
				index++;
				if(index%50==0){preparedStatement.executeBatch();}
			}
			preparedStatement.executeBatch();
			conn.commit();
		} catch (final SQLException ex) {
			// TODO 记录失败直接输出日志,未来可考虑配置化
			logger.error(ex.getMessage());
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				DataSourceUtils.doReleaseConnection(conn, dataSource);
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}

}

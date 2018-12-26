package com.yz.job.common;

import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.yz.model.BaseEvent;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public abstract class AbstractDataFlowTask<T extends BaseEvent> extends AbstractTask<T> implements DataflowJob<T> {

	@Override
	public List<T> fetchData(ShardingContext shardingContext) {
		YzJobInfo jobInfo = this.getYzJobInfo(shardingContext);
		return this.fetchData(jobInfo);
	}

	@Override
	public void processData(ShardingContext shardingContext, List<T> data) {
		YzJobInfo jobInfo = this.getYzJobInfo(shardingContext);
		this.processData(jobInfo, data);
	}
 
	public abstract List<T> fetchData(YzJobInfo jobInfo);

	public abstract void processData(YzJobInfo jobInfo, List<T> data);
}

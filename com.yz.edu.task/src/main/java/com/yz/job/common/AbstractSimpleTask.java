package com.yz.job.common;

 
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.yz.model.BaseEvent; 

/**
 * @author Administrator
 */
public abstract class AbstractSimpleTask<T extends BaseEvent> extends AbstractTask<T> implements SimpleJob { }

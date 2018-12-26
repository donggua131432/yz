package com.yz.sub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yz.constants.RedisKeyConstants;
import com.yz.locator.BccServiceLocator;
import com.yz.model.ServiceExportChannel;
import com.yz.util.JsonUtil;

@Component(value = "dubboExporterSubHandler")
@JedisPubSub(enable = true, channel = RedisKeyConstants.DUBBO_EXPORT_CHANNEL, target = ServiceExportChannel.class)
public class DubboExporterSubHandler extends JedisPubSubHandler<ServiceExportChannel> {

	private Logger logger = LoggerFactory.getLogger(DubboExporterSubHandler.class);

	private BccServiceLocator locator = BccServiceLocator.getInstance();

	@Override
	public void execute(ServiceExportChannel obj) {
		logger.info("DubboExporterReg.execute:{}", JsonUtil.object2String(obj));
		switch (obj.getType()) {
		case 0:
			locator.replaceRemoteService(obj.getServiceInfo());
			break;
		case 1:
			locator.removeRemoteService(obj.getServiceInfo());
			break;
		} 
	}

}

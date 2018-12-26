package com.yz.job.core.scan;

import com.yz.scan.CommonScanService;
import com.yz.spi.SPI;

@SPI(isDefault=true,value="YZ_CUSTOM_SPI")
public class TaskScanService extends CommonScanService {
	@Override
	public String getDubboScanPath() {
		return "com.yz.api,com.yz.service,com.yz.job.service";
	}
//	
//	@Override
//	public String getMybatisScanPath() {
//		return "classpath:mappers/*/*.xml";
//	} 
}

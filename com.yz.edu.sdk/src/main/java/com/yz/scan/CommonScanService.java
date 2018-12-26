package com.yz.scan;

import com.yz.spi.SPI;

@SPI(isDefault=true,value="common")
public class CommonScanService implements CustomerScanService {

	@Override
	public String getDubboScanPath() {
		return "com.yz.api,com.yz.service";
	}

	@Override
	public String getMybatisScanPath() {
		return "classpath:mappers/*.xml";
	} 
}

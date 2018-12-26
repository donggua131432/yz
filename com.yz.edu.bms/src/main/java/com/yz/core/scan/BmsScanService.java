package com.yz.core.scan;

import com.yz.scan.CommonScanService; 
import com.yz.spi.SPI;

@SPI(isDefault=true,value="YZ_CUSTOM_SPI")
public class BmsScanService extends CommonScanService {

 
	@Override
	public String getMybatisScanPath() {
		return "classpath:mappers/*/*.xml";
	} 
}

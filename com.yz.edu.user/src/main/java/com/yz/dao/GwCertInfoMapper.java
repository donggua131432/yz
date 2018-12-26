package com.yz.dao;

import com.yz.cache.common.YzCache;
import com.yz.model.GwCertInfo;
import com.yz.model.GwCertInfoKey;

public interface GwCertInfoMapper { 

	@YzCache(useCache=true,cacheKey="-${certId}-${transType}",cachePrefix="yz.cert",expire = 3600 * 24 )
    GwCertInfo selectByPrimaryKey(GwCertInfoKey key); 
}
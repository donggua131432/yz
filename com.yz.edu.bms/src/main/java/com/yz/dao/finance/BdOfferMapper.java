package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.offer.BdOffer;
import com.yz.model.finance.offer.BdOfferQuery;
import com.yz.model.finance.offer.BdOfferResponse;

public interface BdOfferMapper {

	List<BdOfferResponse> selectOfferByPage(@Param("offer") BdOfferQuery offer);

	int insertOffer(@Param("offer") BdOffer offer);

	int updateOffer(@Param("offer") BdOffer offer);

	BdOfferResponse selectOfferById(@Param("offerId") String offerId);

	int deleteOfferById(@Param("offerId") String offerId);

	int deleteOffersByIds(@Param("offerIds") String[] offerIds);

	void blockOffer(@Param("offerId") String offerId, @Param("status") String status);

	List<Map<String, String>> selectExistOffer(@Param("pfsns") String[] pfsnIds, @Param("testAreas") String[] taIds,
			@Param("scholarship") String scholarship, @Param("inclusionStatus") String inclusionStatus);

	String[] selectOfferPfsnIds(String offerId);

	String[] selectOfferTaIds(String offerId);

	String selectOfferScholarship(String offerId);

	void deletePtoByOfferId(String offerId);

	String selectSgByScholarship(String scholarship);

	String selectInclusionStatus(String offerId);

	List<Map<String, String>> selectTmpAddPto(@Param("offer") BdOffer offer);

	void insertPtoUpdate(@Param("list")List<Map<String, String>> list);

}

package com.twm.mgmt.ws.proxy;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twm.mgmt.utils.CommonUtils;
import com.twm.mgmt.ws.nt.NtSsoRq;
import com.twm.mgmt.ws.nt.NtSsoRs;

@Component
public class NtSsoProxy {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Value("${nt.sso.url}")
	public String ntSsoUrl;

	@Autowired
	private RestTemplate ntSsoRestTemplate;

	public <Q extends NtSsoRq, S extends NtSsoRs> S callWs(Q rq, Class<S> clazz) throws Exception {
		try {
			MultiValueMap<String, Object> params = getParams(rq);

			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params);

			ResponseEntity<String> rs = ntSsoRestTemplate.exchange(ntSsoUrl, HttpMethod.POST, entity, String.class);

			if (rs.getStatusCode() == HttpStatus.OK) {
				String body = rs.getBody();

				log.debug("NtSso Return Body: {}", body);

				return CommonUtils.xmlStr2Obj(body, clazz);
			}
		} catch (Exception e) {
			log.error("Call NtSso Error: {}", e.getMessage(), e);

			throw e;
		}

		return null;
	}

	/**
	 * 
	 * @param rq
	 * @return
	 */
	private <Q extends NtSsoRq> MultiValueMap<String, Object> getParams(Q rq) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

		params.setAll(covertValue(rq));

		return params;
	}

	/**
	 * 物件轉MAP
	 * 
	 * @param rq
	 * @return
	 */
	private <Q extends NtSsoRq> Map<String, Object> covertValue(Q rq) {
		ObjectMapper om = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		Map<String, Object> map = om.convertValue(rq, new TypeReference<Map<String, Object>>() {
		});

		log.debug("NtSso Rq to Map: {}", map);

		return map;
	}

}

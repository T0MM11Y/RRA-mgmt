package com.twm.mgmt;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
public class TestApplication {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Value("${spring.profiles.active}")
	private String active;

	@Test
	@Transactional
	public void test() throws Exception {
		log.info("test...");

//		String content = Files.readString(Paths.get("rs/tokenValueRs.txt"));
//		log.info("content: {}", content);
//		JAXBContext context = JAXBContext.newInstance(GetTokenValueRs.class);
//		Unmarshaller unmarshaller = context.createUnmarshaller();
//		GetTokenValueRs rs = (GetTokenValueRs) unmarshaller.unmarshal(new StringReader(content));
//		GetTokenValueRs rs = CommonUtils.xmlStr2Obj(content, GetTokenValueRs.class);
//		log.info("rs: {}", rs);

//		CampaignDetailRepository repo = SpringUtils.getBean(CampaignDetailRepository.class);
//
//		log.info("result: {}", repo.getDetailByCampaignDetailId(new BigDecimal("117")));

		log.info("format: {}", active);
	}

}

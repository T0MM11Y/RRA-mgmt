package com.twm.mgmt.model.common;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class MailVo {
	
	/** email 發件人 */
	private String from;

	/** email 收件人 */
	@Singular
	private Set<String> toEmails;

	/** email cc副件 */
	@Singular
	private Set<String> copyEmails;

	/** email bcc密本副件 */
	@Singular
	private Set<String> secretEmails;

	/** email 主旨 */
	@Default
	private String subject = "無標題";

	/** email 內容 */
	private String content;

	/** email 附件 */
	@Singular
	private Map<String, byte[]> attachments;

	/** email 模板 */
	private String template;

	/** email 模板值 */
	private Map<String, Object> params;

}
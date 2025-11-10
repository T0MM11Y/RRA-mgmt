package com.twm.mgmt.ws.nt;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("serial")
public class GetTokenValueRq extends NtSsoRq {

	/** Function名稱 */
	@JsonProperty("Func")
	private String func;

	/** 唯一的ID */
	@JsonProperty("TokenID")
	private String tokenId;

}

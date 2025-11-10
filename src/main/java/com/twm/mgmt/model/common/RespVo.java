package com.twm.mgmt.model.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@SuppressWarnings("serial")
public class RespVo implements Serializable {

	private String code;

	private String message;

	private String dateTime;

	private Map<String, List<String>> errorMessages;

}

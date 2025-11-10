package com.twm.mgmt.model.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@SuppressWarnings("serial")
public class ErrorVo implements Serializable {

	private String id;

	private String message;

}

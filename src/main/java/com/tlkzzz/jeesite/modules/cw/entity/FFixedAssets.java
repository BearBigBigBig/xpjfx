/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/tlkzzz/jeesite">JeeSite</a> All rights reserved.
 */
package com.tlkzzz.jeesite.modules.cw.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.tlkzzz.jeesite.common.persistence.DataEntity;

/**
 * 固定资产登记Entity
 * @author xrc
 * @version 2017-04-05
 */
public class FFixedAssets extends DataEntity<FFixedAssets> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 资产名称
	private Date payDate;		// 购买日期
	private String total;		// 折算金额
	private String office;		// 归属部门
	private String fzr;		// 负责人
	
	public FFixedAssets() {
		super();
	}

	public FFixedAssets(String id){
		super(id);
	}

	@Length(min=0, max=100, message="资产名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	@Length(min=0, max=100, message="归属部门长度必须介于 0 和 100 之间")
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
	
	@Length(min=0, max=64, message="负责人长度必须介于 0 和 64 之间")
	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}
	
}
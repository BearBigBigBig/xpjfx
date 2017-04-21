/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/tlkzzz/jeesite">JeeSite</a> All rights reserved.
 */
package com.tlkzzz.jeesite.modules.ck.entity;

import com.tlkzzz.jeesite.modules.cw.entity.FAccount;
import com.tlkzzz.jeesite.modules.cw.entity.FReceipt;
import com.tlkzzz.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.tlkzzz.jeesite.common.persistence.DataEntity;

/**
 * 总订单Entity
 * @author xrc
 * @version 2017-03-27
 */
public class CRkckddinfo extends DataEntity<CRkckddinfo> {
	
	private static final long serialVersionUID = 1L;
	private String ddbh;		// 订单编号
	private String je;		// 金额
	private String lx;		// 0入库1出库
	private String state;		// 0临时采购入库1其他入库2出库录单3其它出库4报废录单5退货录单
	private String issp;		// 0未审批1已审批
	private User spr;		// 审批人
	private Date spsj;		// 审批时间
	private String sjje;     //实际金额
	private FReceipt receipt;	//收款信息
	private FAccount fAccount;	//付款信息
	private CHouse cHouse;	//仓库信息

	private String thxx;  //退货信息

	public CHouse getcHouse() {
		return cHouse;
	}

	public void setcHouse(CHouse cHouse) {
		this.cHouse = cHouse;
	}

	public FAccount getfAccount() {
		return fAccount;
	}

	public void setfAccount(FAccount fAccount) {
		this.fAccount = fAccount;
	}

	public CRkckddinfo() {
		super();
	}

	public CRkckddinfo(String id){
		super(id);
	}

	@Length(min=0, max=64, message="订单编号长度必须介于 0 和 64 之间")
	public String getDdbh() {
		return ddbh;
	}

	public void setDdbh(String ddbh) {
		this.ddbh = ddbh;
	}
	
	public String getJe() {
		return je;
	}

	public void setJe(String je) {
		this.je = je;
	}
	
	@Length(min=0, max=1, message="0入库1出库长度必须介于 0 和 1 之间")
	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Length(min=0, max=1, message="0未审批1已审批长度必须介于 0 和 1 之间")
	public String getIssp() {
		return issp;
	}

	public void setIssp(String issp) {
		this.issp = issp;
	}
	
	public User getSpr() {
		return spr;
	}

	public void setSpr(User spr) {
		this.spr = spr;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSpsj() {
		return spsj;
	}

	public void setSpsj(Date spsj) {
		this.spsj = spsj;
	}

	public FReceipt getReceipt() {
		return receipt;
	}

	public void setReceipt(FReceipt receipt) {
		this.receipt = receipt;
	}

	public String getThxx() {
		return thxx;
	}

	public void setThxx(String thxx) {
		this.thxx = thxx;
	}

	public String getSjje() {
		return sjje;
	}

	public void setSjje(String sjje) {
		this.sjje = sjje;
	}
}
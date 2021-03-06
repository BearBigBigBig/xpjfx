/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/tlkzzz/jeesite">JeeSite</a> All rights reserved.
 */
package com.tlkzzz.jeesite.modules.ck.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tlkzzz.jeesite.modules.ck.entity.*;
import com.tlkzzz.jeesite.modules.ck.service.*;
import com.tlkzzz.jeesite.modules.cw.entity.FArrears;
import com.tlkzzz.jeesite.modules.cw.entity.FPayment;
import com.tlkzzz.jeesite.modules.cw.entity.FReceipt;
import com.tlkzzz.jeesite.modules.cw.service.*;
import com.tlkzzz.jeesite.modules.sys.utils.ExcelCreateUtils;
import com.tlkzzz.jeesite.modules.sys.utils.NumberToCN;
import com.tlkzzz.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tlkzzz.jeesite.common.config.Global;
import com.tlkzzz.jeesite.common.persistence.Page;
import com.tlkzzz.jeesite.common.web.BaseController;
import com.tlkzzz.jeesite.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购订单Controller
 * @author xrc
 * @version 2017-03-17
 */
@Controller
@RequestMapping(value = "${adminPath}/ck/cCgzbinfo")
public class CCgzbinfoController extends BaseController {

	@Autowired
	private CRkckddinfoService cRkckddinfoService;
	@Autowired
	private CSupplierService cSupplierService;
	@Autowired
	private CCgzbinfoService cCgzbinfoService;
	@Autowired
	private CGoodsService cGoodsService;
	@Autowired
	private CHouseService cHouseService;
	@Autowired
	private CDdinfoService cDdinfoService;
	@Autowired
	private CHgoodsService cHgoodsService;
	@Autowired
	private FReceiptService fReceiptService;
	@Autowired
	private FPaymentService fPaymentService;
	@Autowired
	private FIncomeRecordService fIncomeRecordService;
	@Autowired
	private FExpenRecordService fExpenRecordService;
	@Autowired
	private FArrearsService fArrearsService;
	
	@ModelAttribute
	public CCgzbinfo get(@RequestParam(required=false) String id) {
		CCgzbinfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cCgzbinfoService.get(id);
		}
		if (entity == null){
			entity = new CCgzbinfo();
		}
		return entity;
	}
	
	@RequiresPermissions("ck:cCgzbinfo:view")
	@RequestMapping(value = {"list",""})
	public String list(CCgzbinfo cCgzbinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CCgzbinfo> page = cCgzbinfoService.findPage(new Page<CCgzbinfo>(request, response), cCgzbinfo);
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("orderCodeList", cCgzbinfoService.findOrderCodeList(new CCgzbinfo()));
		model.addAttribute("cCgzbinfo", cCgzbinfo);
		model.addAttribute("page", page);
		return "modules/ck/cCgzbinfoList";
	}

	@RequiresPermissions("ck:cCgzbinfo:view")
	@RequestMapping(value = "cgPrint")//打印采购单
	public String cgPrint(CCgzbinfo cCgzbinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(cCgzbinfo!=null&&StringUtils.isNotBlank(cCgzbinfo.getOrderCode())) {
			List<CCgzbinfo> list = cCgzbinfoService.findList(cCgzbinfo);
			double sumMoney = 0;
			int sumNub = 0;
			for(CCgzbinfo cc: list){
				cCgzbinfo = cc;
				cc.getGoods().setCd(cc.getRknub());
				int nub = Integer.parseInt(cc.getNub());
				sumMoney += Double.parseDouble(cc.getJg())*nub;
				sumNub += nub;
			}
			cCgzbinfoService.processUnit(list);
			BigDecimal numberOfMoney = new BigDecimal(sumMoney);
			model.addAttribute("CNMoney", NumberToCN.number2CNMontrayUnit(numberOfMoney));
			model.addAttribute("cCgzbinfo", cCgzbinfo);
			model.addAttribute("list", list);
			model.addAttribute("date", new Date());
			model.addAttribute("sumMoney", sumMoney);
			model.addAttribute("sumNub", sumNub);
			model.addAttribute("user", UserUtils.getUser());
			return "modules/ck/cDdinfoPrintRK";
		}else {
			return "error/400";
		}
	}

	/**
	 * 送货上门填写页面
	 * @param cCgzbinfo
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ck:cCgzbinfo:view")
	@RequestMapping(value = "form")
	public String form(CCgzbinfo cCgzbinfo, Model model) {
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCgzbinfo", cCgzbinfo);
		return "modules/ck/cCgzbinfoForm";
	}

	/**
	 * 送货上门保存信息
	 * @param cCgzbinfo
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ck:cCgzbinfo:edit")
	@RequestMapping(value = "save")
	public String save(CCgzbinfo cCgzbinfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cCgzbinfo)){
			return form(cCgzbinfo, model);
		}
		cCgzbinfoService.save(cCgzbinfo);
		addMessage(redirectAttributes, "保存采购订单成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCgzbinfo/?repage";
	}
	
	@RequiresPermissions("ck:cCgzbinfo:edit")
	@RequestMapping(value = "delete")
	public String delete(CCgzbinfo cCgzbinfo, RedirectAttributes redirectAttributes) {
		cCgzbinfoService.delete(cCgzbinfo);
		addMessage(redirectAttributes, "删除采购订单成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCgzbinfo/?repage";
	}

	@ResponseBody
	@RequestMapping(value = "changeState")
	public String changeState(String id,String state){
		String retStr = "false";
		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(state)){
			try {
				cCgzbinfoService.changeState(id, state);
				retStr = "true";
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return retStr;
	}

	/**
	 * 审批总订单，通过总订单ID将子订单信息汇总到采购汇总表中
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "auditing")
	public String auditing(String id,String state){
		String retStr = "false";
		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(state)){
			boolean saveTrue = true;
			CRkckddinfo cRkckddinfo = cRkckddinfoService.get(id);
			CDdinfo cDdinfo = new CDdinfo();
			cDdinfo.setRkckddinfo(cRkckddinfo);
			List<CDdinfo> cdList = cDdinfoService.findList(cDdinfo);
			if("0".equals(state)||"1".equals(state)) {//入库
				for (CDdinfo cd: cdList) {
					cd.setHouse(cRkckddinfo.getcHouse());
					if(saveTrue&&!cCgzbinfoService.saveInfo(cd)) {
						saveTrue = false;
					}
				}
			}else {//出库
				saveTrue = cHgoodsService.checkStore(cdList);//
				if(saveTrue) {
					for (CDdinfo cd : cdList) {//一个一个订单出库
						cd.setHouse(cRkckddinfo.getcHouse());
						if (saveTrue && !cHgoodsService.CKMinStore(cd)) {
							saveTrue = false;
						}
					}
					/** 财务信息记录	**/
					if ("5".equals(state)) {//退货退款
						FPayment payment = new FPayment();
						payment.setPaymentCode(cRkckddinfo.getId());
						payment = fPaymentService.getByPaymentCode(payment);
						if (payment != null) {
							payment.setApprovalStatus("1");
							payment.setAuditor(UserUtils.getUser());
							fPaymentService.updateApprovalStatus(payment);
							fExpenRecordService.saveByPayment(payment);
						}
					} else if ("4".equals(state)) {//报废 暂时没有财务

					} else {//录单，其他
						FReceipt receipt = new FReceipt();
						receipt.setReceiptCode(cRkckddinfo.getId());
						receipt = fReceiptService.getByReceiptCode(receipt);
						if (receipt != null) {
							receipt.setApprovalStatus("1");
							receipt.setAuditor(UserUtils.getUser());
							fReceiptService.updateApprovalStatus(receipt);
							fIncomeRecordService.saveByReceipt(receipt);
							double htje = Double.parseDouble(receipt.getHtje());
							double sfje = Double.parseDouble(receipt.getJe());
							if (htje > 0 && htje > sfje) {
								//当合同金额大于实付金额的时候产生欠款
								fArrearsService.saveByReceipt(receipt, htje, sfje);
							}
						}
					}
				}
			}
			if(saveTrue) {
				retStr = "true";
				cRkckddinfo.setIssp("1");
				cRkckddinfo.setSpr(UserUtils.getUser());
				cRkckddinfo.setSpsj(new Date());
				cRkckddinfoService.changeIssp(cRkckddinfo);//修改总订单的审批状态
			}
		}
		return retStr;
	}

	/**
	 * 采购单查询
	 * 报表开始
	 */
	@RequiresPermissions("ck:cCgzbinfoInquire:view")
	@RequestMapping(value = "ckInquire")
	public String ckInquire(CCgzbinfo cCgzbinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CCgzbinfo> page = cCgzbinfoService.findPage(new Page<CCgzbinfo>(request, response), cCgzbinfo);
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCgzbinfo", cCgzbinfo);
		model.addAttribute("page", page);
		return "modules/report/cCgzbinfoInquireList";
	}
	/**
	 * 采购单导出
	 * 报表开始
	 */
	@RequiresPermissions("ck:cCgzbinfoInquire:view")
	@RequestMapping(value = "cgExcel")
	public String cgExcel(CCgzbinfo cCgzbinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CCgzbinfo> list = cCgzbinfoService.findList( cCgzbinfo);
		ExcelCreateUtils.cgexport(response,list,"1");
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCgzbinfo", cCgzbinfo);
		model.addAttribute("list", list);
		return null;
	}
	/**
	 * 业务员start
	 * @param cCgzbinfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("report:cCgzbinfoInquire:view")
	@RequestMapping(value = "ywyInquire")
	public String ywyInquire(CCgzbinfo cCgzbinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CCgzbinfo> page = cCgzbinfoService.findPage(new Page<CCgzbinfo>(request, response), cCgzbinfo);
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCgzbinfo", cCgzbinfo);
		model.addAttribute("page", page);
		return "modules/report/cCgzbinfoywyInquireList";
	}

	/**
	 * 业务员品项销售
	 * @param cCgzbinfo
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "xslist")
	public String xslist(CCgzbinfo cCgzbinfo, String type, Model model ) {
		List<CCgzbinfo> list = new ArrayList<CCgzbinfo>();
		if (StringUtils.isNotBlank(type) && "2".equals(type)) {
			list = cCgzbinfoService.findList(cCgzbinfo);
		} else if (StringUtils.isNotBlank(type) && "3".equals(type)) {
			list = cCgzbinfoService.findListxl(cCgzbinfo);
		}
		else {
			list = cCgzbinfoService.findList(cCgzbinfo);

		}
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("cCgzbinfo", cCgzbinfo);
		model.addAttribute("type", type);
		model.addAttribute("list", list);
		return "modules/report/cCgzbinfoxsList";
	}
}
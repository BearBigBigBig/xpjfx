/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/tlkzzz/jeesite">JeeSite</a> All rights reserved.
 */
package com.tlkzzz.jeesite.modules.cw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tlkzzz.jeesite.modules.cw.entity.FReceipt;
import com.tlkzzz.jeesite.modules.cw.service.FReceiptService;
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
import com.tlkzzz.jeesite.modules.cw.entity.FTransferAccount;
import com.tlkzzz.jeesite.modules.cw.service.FTransferAccountService;

/**
 * 转账调账Controller
 * @author xrc
 * @version 2017-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/cw/fTransferAccount")
public class FTransferAccountController extends BaseController {

	@Autowired
	private FTransferAccountService fTransferAccountService;
	@Autowired
	private FReceiptService fReceiptService;
	
	@ModelAttribute
	public FTransferAccount get(@RequestParam(required=false) String id) {
		FTransferAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = fTransferAccountService.get(id);
		}
		if (entity == null){
			entity = new FTransferAccount();
		}
		return entity;
	}
	
	@RequiresPermissions("cw:fTransferAccount:view")
	@RequestMapping(value = {"list", ""})
	public String list(FTransferAccount fTransferAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FTransferAccount> page = fTransferAccountService.findPage(new Page<FTransferAccount>(request, response), fTransferAccount); 
		model.addAttribute("page", page);
		return "modules/cw/fTransferAccountList";
	}

	@RequiresPermissions("cw:fTransferAccount:view")
	@RequestMapping(value = "form")
	public String form(FTransferAccount fTransferAccount, Model model) {
		model.addAttribute("fTransferAccount", fTransferAccount);
		return "modules/cw/fTransferAccountForm";
	}

	/**
	 * 应付款增加页面
	 * @param transferAccount
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("cw:fTransferAccount:view")
	@RequestMapping(value = "receiptForm")
	public String receiptForm(FTransferAccount transferAccount, Model model){
		if(StringUtils.isNotBlank(transferAccount.getTransferType())) {
			model.addAttribute("fTransferAccount", transferAccount);
			model.addAttribute("orderList", fReceiptService.findList(new FReceipt()));
			return "modules/cw/fTransferReceiptForm";
		}else {
			return "error/400";
		}
	}

//	@RequiresPermissions("cw:fTransferAccount:edit")
	@RequestMapping(value = "save")
	public String save(FTransferAccount fTransferAccount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, fTransferAccount)){
			return form(fTransferAccount, model);
		}
		fTransferAccountService.save(fTransferAccount);
		addMessage(redirectAttributes, "保存转账调账成功");
		return "redirect:"+Global.getAdminPath()+"/cw/fTransferAccount/?repage";
	}
	
	@RequiresPermissions("cw:fTransferAccount:edit")
	@RequestMapping(value = "delete")
	public String delete(FTransferAccount fTransferAccount, RedirectAttributes redirectAttributes) {
		fTransferAccountService.delete(fTransferAccount);
		addMessage(redirectAttributes, "删除转账调账成功");
		return "redirect:"+Global.getAdminPath()+"/cw/fTransferAccount/?repage";
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "auditing")
	public String auditing(String id,String state) {
		String retStr = "false";
		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(state)) {
			FTransferAccount transferAccount = new FTransferAccount(id);
			transferAccount.setApprovalStatus(state);
			transferAccount.setAuditor(UserUtils.getUser());
			fTransferAccountService.updateApprovalState(transferAccount);
			transferAccount = fTransferAccountService.get(transferAccount);
			if(transferAccount!=null) {
				switch (Integer.parseInt(state)) {
					case 1: {
						FReceipt receipt = new FReceipt(transferAccount.getOrderId());
						receipt.setHtje(transferAccount.getTransMoney());
						fReceiptService.addHTJE(receipt);
						break;
					}
					case 2: {
						FReceipt receipt = new FReceipt(transferAccount.getOrderId());
						receipt.setHtje(transferAccount.getTransMoney());
						fReceiptService.minHTJE(receipt);
						break;
					}
				}
			}
			retStr = "true";
		}
		return retStr;
	}

}
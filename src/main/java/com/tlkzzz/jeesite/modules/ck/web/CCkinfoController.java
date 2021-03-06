/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/tlkzzz/jeesite">JeeSite</a> All rights reserved.
 */
package com.tlkzzz.jeesite.modules.ck.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import com.tlkzzz.jeesite.modules.ck.entity.*;
import com.tlkzzz.jeesite.modules.ck.service.*;
import com.tlkzzz.jeesite.modules.cw.entity.FReceipt;
import com.tlkzzz.jeesite.modules.sys.utils.ExcelCreateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tlkzzz.jeesite.common.config.Global;
import com.tlkzzz.jeesite.common.persistence.Page;
import com.tlkzzz.jeesite.common.web.BaseController;
import com.tlkzzz.jeesite.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 出库信息Controller
 * @author xrc
 * @version 2017-03-21
 */
@Controller
@RequestMapping(value = "${adminPath}/ck/cCkinfo")
public class CCkinfoController extends BaseController {

	@Autowired
	private CCkinfoService cCkinfoService;
	@Autowired
	private CHouseService cHouseService;
	@Autowired
	private CGoodsService cGoodsService;
	@Autowired
	private CStoreService cStoreService;
	@Autowired
	private CSupplierService cSupplierService;
	@Autowired
	private CRkckddinfoService cRkckddinfoService;

	@ModelAttribute
	public CCkinfo get(@RequestParam(required=false) String id) {
		CCkinfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cCkinfoService.get(id);
		}
		if (entity == null){
			entity = new CCkinfo();
		}
		return entity;
	}

	@RequiresPermissions("ck:cCkinfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(CCkinfo cCkinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CCkinfo> page = cCkinfoService.findPage(new Page<CCkinfo>(request, response), cCkinfo);
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("page", page);
		return "modules/ck/cCkinfoList";
	}
	@RequiresPermissions("ck:cCkinfo:view")
	@RequestMapping(value = "lrtb")
	public String lrtb(CCkinfo cCkinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CCkinfo> page = cCkinfoService.findPage(new Page<CCkinfo>(request, response), cCkinfo);
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("page", page);
		return "modules/ck/cCkinfolvList";
	}
//	@RequiresPermissions("ck:cCkinfo:view")
//	@RequestMapping(value = "lrtblist")
//	public String lrtblist(CCkinfo cCkinfo, String type, String ckdate, Model model) {
////		SimpleDateFormat sdf = new SimpleDateFormat();
////		if (cCkinfo.getCkdate() == null && StringUtils.isNotBlank(ckdate)) {
////			cCkinfo.setCkdate(new Date(Integer.parseInt(ckdate) - 1900, 0, 1));
////		}
////		sdf.applyPattern("yyyy");
////		Date date = new Date();
////		// cCkinfo.setState("1,3,4");//只查询状态为1,3,4的数据(出库信息)
////		List<Map> mapList = new ArrayList<Map>();
////		if (StringUtils.isBlank(type) || "1".equals(type)) {
////			sdf.applyPattern("yyyy");
//	//	}
////			cCkinfo = cCkinfoService.processYear(cCkinfo, date);
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
//		String a;
//
//			List<CCkinfo> list = cCkinfoService.state("3,4", cCkinfo);
//
//			for(int i=0; i< list.size(); i++){
//				Map map = new HashMap();
//			Date b= list.get(i).getCreateDate();
//				String str=sdf.format(b);
//				a=ckdate+"01";
//				if(a.equals(b)){
//					double c=0.0;  double d=0.0; double e=0.0;//(利润)
//					c=Double.parseDouble(cCkinfo.getJe())*Double.parseDouble(cCkinfo.getNub())-Double.parseDouble(cCkinfo.getDdinfo().getYhje());//(合同金额*数量-优惠价)
//					d=Double.parseDouble(cCkinfo.getCkqcbj())*Double.parseDouble(cCkinfo.getNub());//(成本价*数量)
//		            e=c-d;
//
//				}
//
//
//			}
//
////			double a=0.0;  double b=0.0; double c=0.0;//(利润)
////            a=Double.parseDouble(cCkinfo.getJe())*Double.parseDouble(cCkinfo.getNub())-Double.parseDouble(cCkinfo.getDdinfo().getYhje());//(合同金额*数量-优惠价)
////            b=Double.parseDouble(cCkinfo.getCkqcbj())*Double.parseDouble(cCkinfo.getNub());//(成本价*数量)
////		    c=a-b;
////		}
////			model.addAttribute("cCkinfo", cCkinfo);
////			model.addAttribute("type", type);
//			return "modules/ck/cCkinfolvList";
//
//		}


	@RequiresPermissions("ck:cCkinfo:view")
	@RequestMapping(value = "form")
	public String form(CCkinfo cCkinfo, Model model) {
		//model.addAttribute("cCkinfo", cCkinfo);
		return "error/400";
	}

	@RequiresPermissions("ck:cCkinfo:view")
	@RequestMapping(value = "library")
	public String library(CCkinfo cCkinfo, Model model) {//出库录单
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCkinfo", cCkinfo);
		return "modules/ck/cCkInfoLibrary";
	}

	@RequiresPermissions("ck:cCkinfo:edit")
	@RequestMapping(value = "saveLibrary")
	public String saveLibrary(CCkinfo cCkinfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cCkinfo)){//出库录单保存信息
			return form(cCkinfo, model);
		}
		cCkinfoService.outOfTheLibrary(cCkinfo,"4","1");
		addMessage(redirectAttributes, "出库录单成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCkinfo/?repage";
	}

	@RequiresPermissions("ck:cCkinfo:view")
	@RequestMapping(value = "other")
	public String other(CCkinfo cCkinfo, Model model) {//其他出库
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCkinfo", cCkinfo);
		return "modules/ck/cCkInfoOther";
	}

	@RequiresPermissions("ck:cCkinfo:edit")
	@RequestMapping(value = "saveOther")
	public String saveOther(CCkinfo cCkinfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cCkinfo)){//其他出库保存信息
			return form(cCkinfo, model);
		}
		cCkinfoService.outOfTheLibrary(cCkinfo,"3","1");
		addMessage(redirectAttributes, "其他出库成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCkinfo/?repage";
	}

	@RequiresPermissions("ck:cCkinfo:view")
	@RequestMapping(value = "scrapped")
	public String scrapped(CCkinfo cCkinfo, Model model) {//报废录单
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCkinfo", cCkinfo);
		return "modules/ck/cCkInfoScrapped";
	}

	@RequiresPermissions("ck:cCkinfo:edit")
	@RequestMapping(value = "saveScrapped")
	public String saveScrapped(CCkinfo cCkinfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cCkinfo)){//报废录单保存信息
			return form(cCkinfo, model);
		}
		cCkinfoService.outOfTheLibrary(cCkinfo,"2","1");
		addMessage(redirectAttributes, "报废录单成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCkinfo/?repage";
	}

	@RequiresPermissions("ck:cCkinfo:view")
	@RequestMapping(value = "returnOfGoods")
	public String returnOfGoods(CCkinfo cCkinfo, Model model) {//退货录单
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("cCkinfo", cCkinfo);
		return "modules/ck/cCkInfoReturn";
	}

	@RequiresPermissions("ck:cCkinfo:edit")
	@RequestMapping(value = "saveReturnOfGoods")
	public String saveReturnOfGoods(CCkinfo cCkinfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cCkinfo)){//退货录单保存信息
			return form(cCkinfo, model);
		}
		cCkinfoService.outOfTheLibrary(cCkinfo,"1","1");
		addMessage(redirectAttributes, "退货录单成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCkinfo/?repage";
	}

	@RequiresPermissions("ck:cCkinfo:edit")
	@RequestMapping(value = "save")
	public String save(CCkinfo cCkinfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cCkinfo)){
			return form(cCkinfo, model);
		}
//		cCkinfoService.save(cCkinfo);
		addMessage(redirectAttributes, "保存出库信息成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCkinfo/?repage";
	}
	
	@RequiresPermissions("ck:cCkinfo:edit")
	@RequestMapping(value = "delete")
	public String delete(CCkinfo cCkinfo, RedirectAttributes redirectAttributes) {
//		cCkinfoService.delete(cCkinfo);
		addMessage(redirectAttributes, "删除出库信息成功");
		return "redirect:"+Global.getAdminPath()+"/ck/cCkinfo/?repage";
	}
//客户订单
	@RequiresPermissions("ck:cCkinfoInquire:view")
	@RequestMapping(value = "listInquire")
	public String listInquire(CCkinfo cCkinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CCkinfo> list = cCkinfoService.selectList("2,3,4,9",new CCkinfo());
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("list", list);
		return "modules/report/cCkinfoInquireList";
	}
	//客户订单导出
	@RequestMapping(value = "khExcel")
	public String khExcel(CCkinfo cCkinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CCkinfo> list = cCkinfoService.selectList("2,3,4,9",new CCkinfo());
		ExcelCreateUtils.khexport(response,list,"1");
		return null;
	}
	/**
	 * 销售单查询state
	 * @param cCkinfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */

	@RequiresPermissions("ck:cCkinfoInquire:view")
	@RequestMapping(value = "xsInquire" )
	public String xsInquire(CCkinfo cCkinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		cCkinfo.setState("3,4,8");
		Page<CCkinfo> page = cCkinfoService.findPage(new Page<CCkinfo>(request, response), cCkinfo);
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("page", page);
		return "modules/report/cCkinfoxsInquireList";
	}

	/**
	 * 销售导出
	 * @param cCkinfo
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "xsExcel")
	@ResponseBody
	public String xsExcel(CCkinfo cCkinfo, HttpServletResponse response, Model model) {
		cCkinfo.setState("3,4,8");
		List<CCkinfo> list = cCkinfoService.findList(cCkinfo);
		ExcelCreateUtils.xsexport(response,list,"1");
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("list", list);
		return "true";
	}

	/**
	 * 出库报表显示
	 * @param cCkinfo
	 * @param type
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("ck:cRkinfoReport:view")
	@RequestMapping(value = "rkReport")//报表
	public String rkReport(CCkinfo cCkinfo, String type, Model model ) {
	//	cCkinfo.setState("1,2,3,4");
		List<CCkinfo> list = new ArrayList<CCkinfo>();
			if (StringUtils.isNotBlank(type) && "2".equals(type)
					) {
				list = cCkinfoService.selectList("1,2,3,4",new CCkinfo());
			} else if (StringUtils.isNotBlank(type) && "3".equals(type)) {
				list = cCkinfoService.selectList("1,2,3,4",new CCkinfo());
			} else if (StringUtils.isNotBlank(type) && "4".equals(type)) {
				list = cCkinfoService.storeList("1,2,3,4",new CCkinfo());
			} else if (StringUtils.isNotBlank(type) && "5".equals(type)) {
				list = cCkinfoService.storeList("1,2,3,4",new CCkinfo());
			}else if (StringUtils.isNotBlank(type) && "6".equals(type)) {
				list = cCkinfoService.storeList("1,2,3,4",new CCkinfo());
			}else if (StringUtils.isNotBlank(type) && "7".equals(type)) {
				list = cCkinfoService.bandsList("1,2,3,4",new CCkinfo());
			} else if (StringUtils.isNotBlank(type) && "8".equals(type)) {
				list = cCkinfoService.bandsList("1,2,3,4",new CCkinfo());
			}else {
				list = cCkinfoService.selectList("1,2,3,4",new CCkinfo());
			}
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("type", type);
		model.addAttribute("list", list);
		return "modules/report/cCkinfoReportList";
	}
	//退货单查询
	@RequiresPermissions("ck:cCkinfoInquire:view")
	@RequestMapping(value = "listInquiret")
	public String listInquiret(CCkinfo cCkinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CCkinfo> list = cCkinfoService.selectList("1",new CCkinfo());
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("list", list);
		return "modules/report/cCkinfothList";
	}
	//退货单导出
	@RequestMapping(value = "thExcel")
	public String thExcel(CCkinfo cCkinfo,HttpServletResponse response, Model model) {
		List<CCkinfo> list = cCkinfoService.selectList("1",new CCkinfo());
		ExcelCreateUtils.thexport(response,list,"1");
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("list", list);
		return null;
	}
	//报废单查询
	@RequiresPermissions("ck:cCkinfoInquire:view")
	@RequestMapping(value = "bfInquiret")
	public String bfInquiret(CCkinfo cCkinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CCkinfo> list = cCkinfoService.selectList("2",new CCkinfo());
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("list", list);
		return "modules/report/cCkinfobfList";
	}
	//报废导出查询
	@RequestMapping(value = "bfExcel")
	public String bfExcel(CCkinfo cCkinfo,HttpServletResponse response, Model model) {
		List<CCkinfo> list = cCkinfoService.selectList("2",new CCkinfo());
		ExcelCreateUtils.bfexport(response,list,"1");
		model.addAttribute("supplierList", cSupplierService.findList(new CSupplier()));
		model.addAttribute("houseList", cHouseService.findList(new CHouse()));
		model.addAttribute("goodsList", cGoodsService.findList(new CGoods()));
		model.addAttribute("storeList", cStoreService.findList(new CStore()));
		model.addAttribute("cCkinfo", cCkinfo);
		model.addAttribute("list", list);
		return null;
	}
}
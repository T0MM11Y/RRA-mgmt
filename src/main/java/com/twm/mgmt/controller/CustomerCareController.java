package com.twm.mgmt.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.twm.mgmt.model.common.QueryResultVo;
import com.twm.mgmt.model.customer.CustomerCareConditionVo;
import com.twm.mgmt.persistence.entity.OpcoEntity;
import com.twm.mgmt.service.CustomerCareService;
import com.twm.mgmt.service.TransactionHistoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerCareController extends BaseController {

    private static final String VIEW_NAME = "customer/customerCare";
    private static final String MENU_DATA = "4";
    private static final String MENU_OP = "customer/care";
    private static final String SESSION_CONDITION_KEY = "customerCareCondition";
    private static final String HOME_OPCO_LABEL = "TWM";

    private final CustomerCareService customerCareService;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public CustomerCareController(CustomerCareService customerCareService,
            TransactionHistoryService transactionHistoryService) {
        this.customerCareService = customerCareService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping("/care")
    public ModelAndView customerCarePage() {
        ModelAndView mv = new ModelAndView(VIEW_NAME);
        mv.addObject("menudata", MENU_DATA);
        mv.addObject("menuop", MENU_OP);
        mv.addObject("homeOpcoLabel", HOME_OPCO_LABEL);
        mv.addObject("transactionTypes", transactionHistoryService.findTransactionTypes());
        mv.addObject("paymentMethods", transactionHistoryService.findPaymentMethods());
        List<OpcoEntity> catalogOwners = transactionHistoryService.findCatalogOwners();
        mv.addObject("catalogOwners", catalogOwners);
        return mv;
    }

    @PostMapping("/care")
    @ResponseBody
    public ResponseEntity<?> searchTransactions(CustomerCareConditionVo condition, HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            if (condition.getAction() != null && condition.getAction().isQuery()) {
                session.setAttribute(SESSION_CONDITION_KEY, cloneCondition(condition));
            } else {
                CustomerCareConditionVo stored = (CustomerCareConditionVo) session.getAttribute(SESSION_CONDITION_KEY);
                customerCareService.copyQueryCondition(stored, condition);
            }
            QueryResultVo result = customerCareService.findTransactionsByIdentifier(condition);
            // Provide identifiers map so front-end can display all related IDs even if some
            // are null
            java.util.Map<String, Object> identifiers = customerCareService.getUserIdentifiers(condition);
            java.util.Map<String, Object> response = new java.util.LinkedHashMap<>();
            response.put("total", result.getTotal());
            response.put("result", result.getResult());
            response.put("number", result.getNumber());
            response.put("size", result.getSize());
            response.put("order", result.getOrder());
            response.put("name", result.getName());
            if (identifiers != null) {
                response.put("identifiers", identifiers);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("CustomerCareController searchTransactions Error: {}", e.getMessage(), e);
            return getErrorResponse();
        }
    }

    private CustomerCareConditionVo cloneCondition(CustomerCareConditionVo original) {
        if (original == null) {
            return new CustomerCareConditionVo();
        }
        CustomerCareConditionVo cloned = new CustomerCareConditionVo();
        customerCareService.copyQueryCondition(original, cloned);
        cloned.setNumber(original.getNumber());
        cloned.setSize(original.getSize());
        cloned.setOrder(original.getOrder());
        cloned.setName(original.getName());
        return cloned;
    }
}

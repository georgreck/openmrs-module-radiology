/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.radiology.web.controller;

import static org.openmrs.module.radiology.RadiologyRoles.READING_PHYSICIAN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiology.RadiologyOrder;
import org.openmrs.module.radiology.RadiologyService;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PortletsController {
	
	Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private RadiologyService radiologyService;
	
	@Autowired
	private PatientService patientService;
	
	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Context.getDateFormat(), true));
	}
	
	/**
	 * Get URL to the patientOverview portlet
	 * 
	 * @return String
	 * @should should return string with patient info route
	 */
	@RequestMapping("/module/radiology/portlets/patientOverview.portlet")
	String getPatientInfoRoute() {
		String route = "module/radiology/portlets/patientOverview";
		return route;
	}
	
	/**
	 * Get model and view containing radiology orders and studies for given patient string and date
	 * range
	 * 
	 * @param patientQuery Patient string for which radiology orders and studies should be returned
	 *            for
	 * @param startDate Date from which on the radiology orders and studies should be returned for
	 * @param endDate Date until which the radiology orders and studies should be returned for
	 * @return model and view containing radiology orders and studies corresponding to given
	 *         criteria
	 * @should populate model and view with table of orders associated with given empty patient and
	 *         given date range null
	 * @should not populate model and view with table of orders if start date is after end date
	 * @should populate model and view with empty table of orders associated with given end date and
	 *         start date before any order has started
	 * @should populate model and view with empty table of orders associated with given end date and
	 *         start date after any order has started
	 * @should populate model and view with table of orders associated with given start date but
	 *         given end date null
	 * @should populate model and view with table of orders associated with given end date but given
	 *         start date null
	 * @should populate model and view with table of orders including obsId accessed as reading
	 *         physician
	 * @should populate model and view with table of orders associated with given date range
	 */
	@RequestMapping(value = "/module/radiology/portlets/orderSearch.portlet")
	ModelAndView ordersTable(@RequestParam(value = "patientQuery", required = false) String patientQuery,
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate,
	        @RequestParam(value = "pending", required = false) boolean pending,
	        @RequestParam(value = "completed", required = false) boolean completed) {
		ModelAndView mav = new ModelAndView("module/radiology/portlets/orderSearch");
		
		List<RadiologyOrder> matchedOrders = dateFilter(patientQuery, startDate, endDate, mav);
		mav.addObject("orderList", matchedOrders);
		mav.addObject("matchedOrdersSize", matchedOrders.size());
		
		if (Context.getAuthenticatedUser().hasRole(READING_PHYSICIAN, true)) {
			mav.addObject("obsId", "&obsId");
		}
		
		return mav;
	}
	
	/**
	 * Get all orders for given date and patient criteria
	 * 
	 * @param patientQuery Patient string for which radiology orders and studies should be returned
	 *            for
	 * @param startDate Date from which on the radiology orders and studies should be returned for
	 * @param endDate Date until which the radiology orders and studies should be returned for
	 * @return list of radiology orders corresponding to given criteria
	 * @should return list of radiology orders matching the criteria
	 * @should return empty list of radiology orders, if date criteria is not met
	 * @should return list of radiology orders for all patients if patientQuery criteria is not met
	 */
	private List<RadiologyOrder> dateFilter(String patientQuery, Date startDate, Date endDate, ModelAndView mav) {
		
		if (startDate != null && endDate != null) {
			if (startDate.after(endDate)) {
				mav.addObject("exceptionText", "radiology.crossDate");
				return new ArrayList<RadiologyOrder>();
			}
		}
		
		List<Patient> patientList = patientService.getPatients(patientQuery);
		List<RadiologyOrder> preMatchedOrders = radiologyService.getRadiologyOrdersByPatients(patientList);
		List<RadiologyOrder> matchedOrders = new Vector<RadiologyOrder>();
		if (startDate == null && endDate == null) {
			return preMatchedOrders;
		}

		else if (startDate == null && endDate != null) {
			for (RadiologyOrder order : preMatchedOrders) {
				if (order.getStartDate() != null && order.getStartDate().compareTo(endDate) <= 0) {
					
					matchedOrders.add(order);
				}
			}
			
		} else if (startDate != null && endDate == null) {
			for (RadiologyOrder order : preMatchedOrders) {
				if (order.getStartDate() != null && order.getStartDate().compareTo(startDate) >= 0) {
					matchedOrders.add(order);
				}
			}
		}

		else {
			for (RadiologyOrder order : preMatchedOrders) {
				if (order.getStartDate() != null && order.getStartDate().compareTo(startDate) >= 0
				        && order.getStartDate().compareTo(endDate) <= 0) {
					matchedOrders.add(order);
				}
			}
		}
		
		return matchedOrders;
	}
	
	/**
	 * Handle all exceptions of type TypeMismatchException which occur in this class
	 * 
	 * @param TypeMismatchException the thrown TypeMismatchException
	 * @return model and view with exception information
	 * @should populate model with exception text from message properties and invalid value if date
	 *         is expected but nut passed
	 * @should should populate model with exception text
	 */
	@ExceptionHandler(TypeMismatchException.class)
	public ModelAndView handleTypeMismatchException(TypeMismatchException ex) {
		
		log.error(ex.getMessage());
		ModelAndView mav = new ModelAndView();
		
		if (ex.getRequiredType().equals(Date.class)) {
			mav.addObject("invalidValue", ex.getValue());
			mav.addObject("exceptionText", "typeMismatch.java.util.Date");
		} else {
			mav.addObject("exceptionText", ex.getMessage());
		}
		
		return mav;
	}
	
}

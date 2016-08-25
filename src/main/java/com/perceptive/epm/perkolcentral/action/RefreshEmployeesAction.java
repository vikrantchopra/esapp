package com.perceptive.epm.perkolcentral.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeSyncBL;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;

@SuppressWarnings("serial")
public class RefreshEmployeesAction extends ActionSupport {

	private EmployeeSyncBL employeeSyncBL;
	
	public void setEmployeeSyncBL(EmployeeSyncBL employeeSyncBL) {
		this.employeeSyncBL = employeeSyncBL;
	}
	
	public EmployeeSyncBL getEmployeeSyncBL() {
        return employeeSyncBL;
    }
	
	public String executeRefreshEmployeeList() throws ExceptionWrapper {
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        	Date date = new Date();
			LoggingHelpUtil.printDebug("Date: " + dateFormat.format(date) + "-----------------------------------------------Refresh Employees List started-----------------------------------------------");
			this.employeeSyncBL.executeEmployeeSync();
			LoggingHelpUtil.printDebug("-----------------------------------------------Refresh Employees List finished-----------------------------------------------");
		} catch (Exception ex) {
			throw new ExceptionWrapper(ex);
		}
		return SUCCESS;
	}
	
}

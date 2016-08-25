package com.perceptive.epm.perkolcentral.action;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.bl.GroupsBL;
import com.perceptive.epm.perkolcentral.bl.ImageNowLicenseBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.Constants;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Imagenowlicenses;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 21/9/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageNowLicenseAction extends ActionSupport {
    private GroupsBL groupsBL;
    private ImageNowLicenseBL imageNowLicenseBL;
    private ArrayList<GroupBO> rallyGroups;
    private File sysfpFile;
    private String sysfpFileFileName;
    private String groupRequestedFor;
    ArrayList<Imagenowlicenses> imagenowlicensesArrayList;
    private InputStream sysFpFileInputStream;
    private String errorMessage = StringUtils.EMPTY;
    private String dupsub = StringUtils.EMPTY;
    private EmployeeBL employeeBL;
    
    private String hostname;


    private Imagenowlicenses imagenowlicenses = new Imagenowlicenses();

    public void setGroupsBL(GroupsBL groupsBL) {
        this.groupsBL = groupsBL;
    }

    public ArrayList<GroupBO> getRallyGroups() {
        return rallyGroups;
    }

    public Imagenowlicenses getImagenowlicenses() {
        return imagenowlicenses;
    }

    public void setImagenowlicenses(Imagenowlicenses imagenowlicenses) {
        this.imagenowlicenses = imagenowlicenses;
    }

    public File getSysfpFile() {
        return sysfpFile;
    }

    public void setSysfpFile(File sysfpFile) {
        this.sysfpFile = sysfpFile;
    }

    public String getSysfpFileFileName() {
        return sysfpFileFileName;
    }

    public void setSysfpFileFileName(String sysfpFileFileName) {
        this.sysfpFileFileName = sysfpFileFileName;
    }

    public String getGroupRequestedFor() {
        return groupRequestedFor;
    }

    public void setGroupRequestedFor(String groupRequestedFor) {
        this.groupRequestedFor = groupRequestedFor;
    }

    public void setImageNowLicenseBL(ImageNowLicenseBL imageNowLicenseBL) {
        this.imageNowLicenseBL = imageNowLicenseBL;
    }

    public ArrayList<Imagenowlicenses> getImagenowlicensesArrayList() {
        return imagenowlicensesArrayList;
    }

    public void setImagenowlicensesArrayList(ArrayList<Imagenowlicenses> imagenowlicensesArrayList) {
        this.imagenowlicensesArrayList = imagenowlicensesArrayList;
    }

    public InputStream getSysFpFileInputStream() {
        return sysFpFileInputStream;
    }

    public String getErrorMessage() {
        return errorMessage != null ? errorMessage.trim() : errorMessage;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public String getDupsub() {
        return dupsub;
    }

    public void setDupsub(String dupsub) {
        this.dupsub = dupsub;
    }

    public String executeImageNowLicenseRequestView() throws ExceptionWrapper {
        try {
            rallyGroups = groupsBL.getAllRallyGroups();
            getSpecificGroupsThatRequireINLicense();
            if (StringUtils.isNotEmpty(dupsub)) {
                errorMessage = errorMessage + "* You are trying to re-submit a form that you have already submitted.Please wait for some time." + IOUtils.LINE_SEPARATOR;
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }


    public String executeImageNowLicenseRequest() throws ExceptionWrapper {
        try {
            rallyGroups = groupsBL.getAllRallyGroups();
            getSpecificGroupsThatRequireINLicense();
            //Check whether this person is part of perceptive kolkata
            if (ActionContext.getContext().getSession().get(Constants.logged_in_user) == null) {
                errorMessage = errorMessage + "* You have to be a part of Perceptive Kolkata Group to request for ImageNow Development License." + IOUtils.LINE_SEPARATOR;
                return ERROR;
            }
            if (!FilenameUtils.getExtension(sysfpFileFileName).equalsIgnoreCase("sysfp")) {
                errorMessage = errorMessage + "* Please upload a file of type sysfp." + IOUtils.LINE_SEPARATOR;
                return ERROR;
            }
            if (FileUtils.sizeOf(sysfpFile) > FileUtils.ONE_MB * 2) {
                errorMessage = errorMessage + "* Please upload a file of size less than 2 MB." + IOUtils.LINE_SEPARATOR;
                return ERROR;
            }

            String employeeUID = ((EmployeeBO) ActionContext.getContext().getSession().get(Constants.logged_in_user)).getEmployeeUid();
            //Get the logged in userId
            //rallyGroups = groupsBL.getAllRallyGroups();
            //getSpecificGroupsThatRequireINLicense();

            imagenowlicenses.setImageNowLicenseRequestId(UUID.randomUUID().toString());
            imagenowlicenses.setLicenseRequestedOn(Calendar.getInstance().getTime());
            imagenowlicenses.setFileName(sysfpFileFileName);

            imageNowLicenseBL.addImageNowLicenseRequest(imagenowlicenses, groupRequestedFor, employeeUID, sysfpFile, sysfpFileFileName);
            imagenowlicensesArrayList = imageNowLicenseBL.getLicensesRequestedByMe(employeeUID);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeSysFPFileDownLoad() throws ExceptionWrapper {
        try {
            sysFpFileInputStream = imageNowLicenseBL.getSysFpFileInputStream(imagenowlicenses);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeImageNowLicensesRequestedByMe() throws ExceptionWrapper {
        try {
            String employeeUID =
                    ActionContext.getContext().getSession().get(Constants.logged_in_user) != null ?
                            ((EmployeeBO) ActionContext.getContext().getSession().get(Constants.logged_in_user)).getEmployeeUid() : "";
            imagenowlicensesArrayList = imageNowLicenseBL.getLicensesRequestedByMe(employeeUID);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeImageNowLicensesToProvide() throws ExceptionWrapper {
        try {
            imagenowlicensesArrayList = imageNowLicenseBL.getLicensesToBeProvided();
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeCancelImageNowLicenseRequest() throws ExceptionWrapper {
        try {
            imageNowLicenseBL.cancelImageNowLicense(imagenowlicenses);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeProvideImageNowLicenseRequest() throws ExceptionWrapper {
        try {
            String employeeUID = ((EmployeeBO) ActionContext.getContext().getSession().get(Constants.logged_in_user)).getEmployeeUid();
            imageNowLicenseBL.provideImageNowLicense(imagenowlicenses, employeeUID);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    private void getSpecificGroupsThatRequireINLicense() throws ExceptionWrapper {
        try {
            if (rallyGroups == null)
                return;
            ArrayList<GroupBO> groupBOArrayList = new ArrayList<GroupBO>(groupsBL.getAllGroups().values());
            CollectionUtils.filter(groupBOArrayList, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ((GroupBO) o).getGroupId().trim().equalsIgnoreCase("22");
                }
            });
            for (GroupBO groupBO : groupBOArrayList) {
                rallyGroups.add(groupBO);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }
    
    public String executeINowLicenseRequestView() throws ExceptionWrapper {
    	String result = ERROR;
        try {
        	
        	String hostAddress = this.imagenowlicenses.getHostname();
        	System.out.println("Hostname is: " + hostAddress );
        	
        	InetAddress address = InetAddress.getByName(hostAddress);
        	boolean reachable = address.isReachable(5000);
        	System.out.println("Is Host Reachable: " + reachable );
        	
        	if(!reachable) {System.out.println("Not Reachable");
        	errorMessage = errorMessage + "* Hostname not Reachable." + IOUtils.LINE_SEPARATOR;
        		return result;
        	} else {
        		result = SUCCESS; 
        		System.out.println("RESULT is: " + result);
	            rallyGroups = groupsBL.getAllRallyGroups();
	            getSpecificGroupsThatRequireINLicense();
	            if (StringUtils.isNotEmpty(dupsub)) {
	                errorMessage = errorMessage + "* You are trying to re-submit a form that you have already submitted.Please wait for some time." + IOUtils.LINE_SEPARATOR;
	            }
	            
        	}
        } catch (Exception ex) {
            //throw new ExceptionWrapper(ex);
        	errorMessage = errorMessage + "* Invalid Hostname." + IOUtils.LINE_SEPARATOR;
        	result = ERROR;
        }
        
        return result;
    }
    
    public void setHostname(String hostname) {
    	this.hostname = hostname;
    }
    
    public String getHostname() {
    	return this.hostname;
    }
}

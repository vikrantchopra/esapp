package com.perceptive.epm.perkolcentral.hibernate.pojo;
// Generated Sep 23, 2012 6:50:23 PM by Hibernate Tools 3.2.4.GA



/**
 * Employeegroupmap generated by hbm2java
 */
public class Employeegroupmap  implements java.io.Serializable {


     private Long employeeGroupMappingId;
     private Employee employee;
     private Groups groups;

    public Employeegroupmap() {
    }

    public Employeegroupmap(Employee employee, Groups groups) {
       this.employee = employee;
       this.groups = groups;
    }
   
    public Long getEmployeeGroupMappingId() {
        return this.employeeGroupMappingId;
    }
    
    public void setEmployeeGroupMappingId(Long employeeGroupMappingId) {
        this.employeeGroupMappingId = employeeGroupMappingId;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Groups getGroups() {
        return this.groups;
    }
    
    public void setGroups(Groups groups) {
        this.groups = groups;
    }




}


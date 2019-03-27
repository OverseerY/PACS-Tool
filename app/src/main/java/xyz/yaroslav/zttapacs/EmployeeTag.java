package xyz.yaroslav.zttapacs;

public class EmployeeTag {
    private String tagId;
    private String empName;
    private String empDepartment;

    public EmployeeTag() {}

    public EmployeeTag(String tagId, String empName, String empDepartment) {
        this.tagId = tagId;
        this.empName = empName;
        this.empDepartment = empDepartment;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setEmpDepartment(String empDepartment) {
        this.empDepartment = empDepartment;
    }

    public String getTagId() {
        return tagId;
    }

    public String getEmpDepartment() {
        return empDepartment;
    }

    public String getEmpName() {
        return empName;
    }
}

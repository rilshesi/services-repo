package co.uk.webapi.schemeModel;

public class PostEmpoyeeModel {

    // We are getting the data from Web API and then set it in here. this is called deSerialization
    // Here, we can add data to post from PostEmployeeStepDef class, so we can post it into WEB API

    private int id;
    private String firstname;
    private String lastname;
    private String gender;
    private int salary;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFilename() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname(String dataSurename) {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}

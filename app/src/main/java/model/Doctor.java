package model;

public class Doctor {
    /////////////////DoctorID///////////////////////
    private String clinicName ,doctorName,clinicAddress,clinicPhone,doctorEmail;
    //  ArrayList<Patient> pateints = new ArrayList<Patient>();


    public Doctor() {
    }

    public Doctor(String clinicName, String doctorName, String clinicAddress, String clinicPhone, String doctorEmail) {
        this.clinicName = clinicName;
        this.doctorName = doctorName;
        this.clinicAddress = clinicAddress;
        this.clinicPhone = clinicPhone;
        this.doctorEmail = doctorEmail;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getClinicPhone() {
        return clinicPhone;
    }

    public void setClinicPhone(String clinicPhone) {
        this.clinicPhone = clinicPhone;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }


//    public ArrayList<Patient> getPateints() {
//        return pateints;
//    }
//
//    public void setPateints(ArrayList<Patient> pateints) {
//        this.pateints = pateints;
//    }
}


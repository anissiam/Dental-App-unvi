package model;

import java.util.Date;

public class Patient {

    private String doctorId,patientName,patientAge,NDC,p_phone;
    private String patientٍSex,patientSmoke;
    private String documentid;
    Date patientDate =new Date();
    //               سكري     نفسي     ضغط
    private boolean is_htn, is_psychic,is_dm;


//    ArrayList<vistPatient> pateintvisits = new ArrayList<vistPatient>();

    public Patient() {
    }


    public Patient(String doctorId, String patientName, String patientAge, String patientٍSex,
                   String patientSmoke, Date patientDate, boolean is_htn, boolean is_psychic, boolean is_dm,String p_phone) {
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        // this.NDC = NDC;
        this.patientٍSex = patientٍSex;
        this.patientSmoke = patientSmoke;
        this.patientDate = patientDate;
        this.is_htn = is_htn;
        this.is_psychic = is_psychic;
        this.is_dm = is_dm;
        this.p_phone = p_phone;
    }

    public Patient(String doctorId,String patientName, String patientAge, String patientٍSex, String patientSmoke, boolean is_htn, boolean is_psychic, boolean is_dm,String p_phone) {
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientٍSex = patientٍSex;
        this.patientSmoke = patientSmoke;
        this.is_htn = is_htn;
        this.is_psychic = is_psychic;
        this.is_dm = is_dm;
        this.p_phone = p_phone;
    }
    /*public Patient(String doctorId, String patientName, String patientAge, boolean patientٍSex, String NDC, boolean patientSmoke, Date patientDate) {
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientٍSex = patientٍSex;
        this.NDC = NDC;
        this.patientSmoke = patientSmoke;
        this.patientDate = patientDate;
    }*/

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientٍSex() {
        return patientٍSex;
    }

    public void setPatientٍSex(String patientٍSex) {
        this.patientٍSex = patientٍSex;
    }

    public String getPatientSmoke() {
        return patientSmoke;
    }

    public void setPatientSmoke(String patientSmoke) {
        this.patientSmoke = patientSmoke;
    }


    /*public boolean isPatientٍSex() {
            return patientٍSex;
        }

        public void setPatientٍSex(boolean patientٍSex) {
            this.patientٍSex = patientٍSex;
        }

        public String isNDC() {
            return NDC;
        }

        public void setNDC(String NDC) {
            this.NDC = NDC;
        }

        public boolean isPatientSmoke() {
            return patientSmoke;
        }

        public void setPatientSmoke(boolean patientSmoke) {
            this.patientSmoke = patientSmoke;
        }
    */
    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public Date getPatientDate() {
        return patientDate;
    }

    public void setPatientDate(Date patientDate) {
        patientDate = patientDate;
    }

    public boolean isIs_htn() {
        return is_htn;
    }

    public void setIs_htn(boolean is_htn) {
        this.is_htn = is_htn;
    }

    public boolean isIs_psychic() {
        return is_psychic;
    }

    public void setIs_psychic(boolean is_psychic) {
        this.is_psychic = is_psychic;
    }

    public boolean isIs_dm() {
        return is_dm;
    }

    public void setIs_dm(boolean is_dm) {
        this.is_dm = is_dm;
    }

    public String getP_phone() {
        return p_phone;
    }

    public void setP_phone(String p_phone) {
        this.p_phone = p_phone;
    }

    //    public ArrayList<vistPatient> getPateintvisits() {
//        return pateintvisits;
//    }
//
//    public void setPateintvisits(ArrayList<vistPatient> pateintvisits) {
//        this.pateintvisits = pateintvisits;
//    }
}


package model;

import java.util.Date;

public class vistPatient {
    private String PatientId,visittId, doctorId , patentName , vistID,patientProplem,patientDiagnosis,patientNote,documentid;
    Date visitDate =new Date();

    public vistPatient(String patentName, String patientProplem, String patientDiagnosis, String patientNote, Date visitDate) {
        this.patentName = patentName;
        this.patientProplem = patientProplem;
        this.patientDiagnosis = patientDiagnosis;
        this.patientNote = patientNote;
        this.visitDate = visitDate;
    }

    public vistPatient() {
    }


    public vistPatient(String visittId, String doctorId, String patentName, String patientProplem, String patientDiagnosis, String patientNote, Date visitDate) {
        this.visittId = visittId;
        this.doctorId = doctorId;
        this.patentName = patentName;
        this.vistID = vistID;
        this.patientProplem = patientProplem;
        this.patientDiagnosis = patientDiagnosis;
        this.patientNote = patientNote;
        this.documentid = documentid;
        this.visitDate = visitDate;
    }

    public vistPatient(String patientProplem, String patientDiagnosis, String patientNote, Date visitDate) {
        this.patientProplem = patientProplem;
        this.patientDiagnosis = patientDiagnosis;
        this.patientNote = patientNote;
        this.visitDate = visitDate;
    }

    /*public vistPatient(String vistID, String patientProplem, String patientDiagnosis, String patientNote, Date visitDate) {
        this.vistID = vistID;
        this.visitDate = visitDate;
        this.patientProplem = patientProplem;
        this.patientDiagnosis = patientDiagnosis;
        this.patientNote = patientNote;
    }*/

    public String getvisittId() {
        return visittId;
    }

    public void setvisittId(String patientId) {
        this.visittId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getVistID() {
        return vistID;
    }
    public void setVistID(String vistID) {
        this.vistID = vistID;
    }

    /*public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }*/

    public String getPatentName() {
        return patentName;
    }

    public void setPatentName(String patentName) {
        this.patentName = patentName;
    }

    public String getPatientProplem() {
        return patientProplem;
    }

    public void setPatientProplem(String patientProplem) {
        this.patientProplem = patientProplem;
    }

    public String getPatientDiagnosis() {
        return patientDiagnosis;
    }

    public void setPatientDiagnosis(String patientDiagnosis) {
        this.patientDiagnosis = patientDiagnosis;
    }

    public String getPatientNote() {
        return patientNote;
    }

    public void setPatientNote(String patientNote) {
        this.patientNote = patientNote;
    }


    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }
}


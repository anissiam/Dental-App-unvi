package model;

import java.util.Date;

public class VisitPatient {
    private String visitId_Primary,doctorId,patentName,documentid,problemId
            ,paymentDone,visitNote,visit_id_copy,patientId,patientPhone;
    private Date VisitDate = new Date();

    public VisitPatient() {
    }
    //-- for primary
    public VisitPatient( String doctorId, String patentName
            , String problemId, String paymentDone, Date visitDate,String visitNote) {
       // this.visitId_Primary = visitId_Primary;
        this.doctorId = doctorId;
        this.patentName = patentName;
        this.problemId = problemId;
        this.paymentDone = paymentDone;
        VisitDate = visitDate;
        this.visitNote = visitNote;
    }
    //-- for copy
    public VisitPatient(String visitId_Primary, String doctorId
            , String patentName, String problemId, String paymentDone, String visitNote, Date visitDate) {
        this.visitId_Primary = visitId_Primary;
        this.doctorId = doctorId;
        this.patentName = patentName;
        this.problemId = problemId;
        this.paymentDone = paymentDone;
        this.visitNote = visitNote;
        VisitDate = visitDate;
    }

    public VisitPatient(String visitId_Primary, String doctorId, String patentName, String problemId
            , String paymentDone, String visitNote, String patientId, String patientPhone, Date visitDate) {
        this.visitId_Primary = visitId_Primary;
        this.doctorId = doctorId;
        this.patentName = patentName;
        this.problemId = problemId;
        this.paymentDone = paymentDone;
        this.visitNote = visitNote;
        this.patientId = patientId;
        this.patientPhone = patientPhone;
        VisitDate = visitDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getVisit_id_copy() {
        return visit_id_copy;
    }

    public void setVisit_id_copy(String visit_id_copy) {
        this.visit_id_copy = visit_id_copy;
    }

    public String getVisitId_Primary() {
        return visitId_Primary;
    }

    public void setVisitId_Primary(String visitId_Primary) {
        this.visitId_Primary = visitId_Primary;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatentName() {
        return patentName;
    }

    public void setPatentName(String patentName) {
        this.patentName = patentName;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(String paymentDone) {
        this.paymentDone = paymentDone;
    }

    public Date getVisitDate() {
        return VisitDate;
    }

    public void setVisitDate(Date visitDate) {
        VisitDate = visitDate;
    }

    public String getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(String visitNote) {
        this.visitNote = visitNote;
    }
}

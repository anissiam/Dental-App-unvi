package model;

import java.util.Date;

public class Problem {
    private String ProProblem, ProName, ProPayment, ProPlace, ProNote,patentName;
    private String documentid;
    private Date ProDate = new Date();

    public Problem() {
    }

    public Problem(String proProblem, String proPayment, String proPlace, String proNote, Date proDate,String patentName) {
        this.patentName = patentName;
        ProProblem = proProblem;
        ProPayment = proPayment;
        ProPlace = proPlace;
        ProNote = proNote;
        ProDate = proDate;
    }

    public String getPatentName() {
        return patentName;
    }

    public void setPatentName(String patentName) {
        this.patentName = patentName;
    }

    public String getProProblem() {
        return ProProblem;
    }

    public void setProProblem(String proProblem) {
        ProProblem = proProblem;
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getProPayment() {
        return ProPayment;
    }

    public void setProPayment(String proPayment) {
        ProPayment = proPayment;
    }

    public String getProPlace() {
        return ProPlace;
    }

    public void setProPlace(String proPlace) {
        ProPlace = proPlace;
    }

    public String getProNote() {
        return ProNote;
    }

    public void setProNote(String proNote) {
        ProNote = proNote;
    }

    public Date getProDate() {
        return ProDate;
    }

    public void setProDate(Date proDate) {
        ProDate = proDate;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }
}
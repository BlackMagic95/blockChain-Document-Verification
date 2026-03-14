package com.verify.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.Instant;

// NEW FILE — one record per college that applies to join the platform
// Status lifecycle:   PENDING  →  APPROVED  or  REJECTED
@Document(collection = "college_requests")
public class CollegeRequest {

    @Id
    private String id;

    private String collegeName;

    @Indexed(unique = true)
    private String adminEmail; // the Google email that will log in after approval

    private String emailDomain; // auto-extracted from adminEmail if blank
    private String contactPhone;
    private String address;

    private String status = "PENDING"; // PENDING | APPROVED | REJECTED

    private Instant submittedAt = Instant.now();
    private Instant reviewedAt;
    private String reviewedBy; // super-admin email who clicked approve/reject

    /* getters & setters */
    public String getId() {
        return id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String v) {
        this.collegeName = v;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String v) {
        this.adminEmail = v;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String v) {
        this.emailDomain = v;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String v) {
        this.contactPhone = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String v) {
        this.address = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        this.status = v;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant v) {
        this.submittedAt = v;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant v) {
        this.reviewedAt = v;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String v) {
        this.reviewedBy = v;
    }
}

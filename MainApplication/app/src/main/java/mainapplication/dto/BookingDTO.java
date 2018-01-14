package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 18/12/2017.
 */

public class BookingDTO implements Serializable {

    private int id;
    private String qrCode;
    private String facilityName;
    private int facilityId;
    private String imageURL;
    private int timeSlotId;
    private String beginTime;
    private String endTime;

    /** participant-specific */
    private int invitationId;
    private String email;

    /** author-specific */
    private double timeSlotPrice;
    private double totalPrice;
    private String bookerUserName;
    private SupplementalEquipmentDTO[] bookedSupplementalEquipments;
    private int countBookedSupplementalEquipments;
    private String[] participantsEmail;
    private int countParticipants;

    private AtendeeRatingDTO rating;

    public BookingDTO() {
    }

    public BookingDTO(int id, String qrCode, int facilityId, int timeSlotId, String beginTime, String endTime,
                      int invitationId, String email) {
        this.id = id;
        this.qrCode = qrCode;
        this.facilityId = facilityId;
        this.timeSlotId = timeSlotId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.invitationId = invitationId;
        this.email = email;
    }

    public BookingDTO(int id, String qrCode, int facilityId, int timeSlotId, String beginTime, String endTime,
                      double timeSlotPrice, double totalPrice, String bookerUserName,
                      SupplementalEquipmentDTO[] bookedSupplementalEquipments, int countBookedSupplementalEquipments,
                      String[] participantsEmail, int countParticipants) {
        this.id = id;
        this.qrCode = qrCode;
        this.facilityId = facilityId;
        this.timeSlotId = timeSlotId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.timeSlotPrice = timeSlotPrice;
        this.totalPrice = totalPrice;
        this.bookerUserName = bookerUserName;
        this.bookedSupplementalEquipments = bookedSupplementalEquipments;
        this.countBookedSupplementalEquipments = countBookedSupplementalEquipments;
        this.participantsEmail = participantsEmail;
        this.countParticipants = countParticipants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTimeSlotPrice() {
        return timeSlotPrice;
    }

    public void setTimeSlotPrice(double timeSlotPrice) {
        this.timeSlotPrice = timeSlotPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookerUserName() {
        return bookerUserName;
    }

    public void setBookerUserName(String bookeeUserName) {
        this.bookerUserName = bookeeUserName;
    }

    public SupplementalEquipmentDTO[] getBookedSupplementalEquipments() {
        return bookedSupplementalEquipments;
    }

    public void setBookedSupplementalEquipments(SupplementalEquipmentDTO[] bookedSupplementalEquipments) {
        this.bookedSupplementalEquipments = bookedSupplementalEquipments;
    }

    public int getCountBookedSupplementalEquipments() {
        return countBookedSupplementalEquipments;
    }

    public void setCountBookedSupplementalEquipments(int countBookedSupplementalEquipments) {
        this.countBookedSupplementalEquipments = countBookedSupplementalEquipments;
    }

    public String[] getParticipantsEmail() {
        return participantsEmail;
    }

    public void setParticipantsEmail(String[] participantsEmail) {
        this.participantsEmail = participantsEmail;
    }

    public int getCountParticipants() {
        return countParticipants;
    }

    public void setCountParticipants(int countParticipants) {
        this.countParticipants = countParticipants;
    }

    public AtendeeRatingDTO getRating() {
        return rating;
    }

    public void setRating(AtendeeRatingDTO rating) {
        this.rating = rating;
    }
}

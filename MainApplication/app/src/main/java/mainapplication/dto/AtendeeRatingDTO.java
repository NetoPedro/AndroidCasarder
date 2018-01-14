package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 21/12/2017.
 */

public class AtendeeRatingDTO implements Serializable {

    private int id;
    private boolean isBooker;
    private String comment;
    private int priceValue;
    private int premisesValue;
    private int accessesValue;
    private int average;
    private String userId;
    private int facilityId;
    private int bookingId;
    private String date;
    private String raterUserName;

    public AtendeeRatingDTO() {
        isBooker = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBooker() {
        return isBooker;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(int priceValue) {
        this.isBooker = true;
        this.priceValue = priceValue;
    }

    public int getPremisesValue() {
        return premisesValue;
    }

    public void setPremisesValue(int premisesValue) {
        this.premisesValue = premisesValue;
    }

    public int getAccessesValue() {
        return accessesValue;
    }

    public void setAccessesValue(int accessesValue) {
        this.accessesValue = accessesValue;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRaterUserName() {
        return raterUserName;
    }

    public void setRaterUserName(String raterUserName) {
        this.raterUserName = raterUserName;
    }
}

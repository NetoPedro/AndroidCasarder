package mainapplication.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Luis Gouveia on 11/12/2017.
 */

public class TimeSlotDTO implements Serializable {

    private int id;
    private String beginTime;
    private String endTime;
    private double price;
    private int facilityId;
    private int timeSlotTypeId;
    private boolean available;
    private BookingDTO[] bookingsDTO;

    public TimeSlotDTO() {
    }

    public TimeSlotDTO(int id, String beginTime, String endTime, double price, int facilityId, int timeSlotTypeId, boolean available, BookingDTO[] bookingsDTO) {
        this.id = id;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.price = price;
        this.facilityId = facilityId;
        this.timeSlotTypeId = timeSlotTypeId;
        this.available = available;
        this.bookingsDTO = bookingsDTO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getTimeSlotTypeId() {
        return timeSlotTypeId;
    }

    public void setTimeSlotTypeId(int timeSlotTypeId) {
        this.timeSlotTypeId = timeSlotTypeId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public BookingDTO[] getBookingsDTO() {
        return bookingsDTO;
    }

    public void setBookingsDTO(BookingDTO[] bookingsDTO) {
        this.bookingsDTO = bookingsDTO;
    }
}

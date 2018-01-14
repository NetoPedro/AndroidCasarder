package mainapplication.dto;

import java.io.Serializable;

/**
 * Created by Luis Gouveia on 28/12/2017.
 */

public class FacilityPromotionalCampaignDTO implements Serializable {

    private int id;
    private String name;
    private float discount;
    private String beginTime;
    private String endTime;
    private int facilityId;

    public FacilityPromotionalCampaignDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
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

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
}

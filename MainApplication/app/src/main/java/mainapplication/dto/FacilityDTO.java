package mainapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Luis Gouveia on 04/12/2017.
 */

public class FacilityDTO implements Serializable {

    private int id;
    private String name;
    private String description;
    private int sportsComplexId;
    private int facilityTypeId;
    private double latitude;
    private double longitude;
    private CityDTO city;
    private double averageRating;
    private double averagePremisesRating;
    private double averageAccessesRating;
    private double averagePriceRating;
    private int ratingsCounter;
    private String imageURL;
    private String[] otherImagesURL;
    private FacilityProfileDTO facilityProfile;
    private CategoryDTO[] categories;
    private SportDTO[] sports;
    private SupplementalEquipmentDTO[] supplementalEquipments;
    private BaseEquipmentDTO[] baseEquipments;
    private FacilityPromotionalCampaignDTO promotionNext30Days;

    public FacilityDTO() {
    }

    public FacilityDTO(SolrFacilityDTO facility){
        this.id = facility.getId();
        this.name = facility.getName();
        try {
            this.description = facility.getDescription()
                    .substring(facility.getDescription().indexOf("<" + Locale.getDefault().getLanguage() + ">") + 4,
                            facility.getDescription().indexOf("</" + Locale.getDefault().getLanguage() + ">"));
        }catch (StringIndexOutOfBoundsException e){
            this.description = facility.getDescription()
                    .substring(facility.getDescription().indexOf("<pt>") + 4,
                            facility.getDescription().indexOf("</pt>"));
        }
        this.latitude = facility.getLatitude();
        this.longitude = facility.getLongitude();
        this.averageRating = facility.getAverageRating();
        this.averagePremisesRating = facility.getAveragePremisesRating();
        this.averageAccessesRating = facility.getAverageAccessesRating();
        this.averagePriceRating = facility.getAveragePriceRating();
        this.ratingsCounter = facility.getRatingsCounter();
        this.imageURL = facility.getImageURL();
        if(facility.getPromotion() != null) {
            String[] details = facility.getPromotion().split("\\|");

            this.promotionNext30Days = new FacilityPromotionalCampaignDTO();
            this.promotionNext30Days.setDiscount(Float.parseFloat(details[0]));
            this.promotionNext30Days.setBeginTime(details[1]);
            this.promotionNext30Days.setEndTime(details[2]);
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSportsComplexId() {
        return sportsComplexId;
    }

    public void setSportsComplexId(int sportsComplexId) {
        this.sportsComplexId = sportsComplexId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(int facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getAveragePremisesRating() {
        return averagePremisesRating;
    }

    public void setAveragePremisesRating(double averagePremisesRating) {
        this.averagePremisesRating = averagePremisesRating;
    }

    public double getAverageAccessesRating() {
        return averageAccessesRating;
    }

    public void setAverageAccessesRating(double averageAccessesRating) {
        this.averageAccessesRating = averageAccessesRating;
    }

    public double getAveragePriceRating() {
        return averagePriceRating;
    }

    public void setAveragePriceRating(double averagePriceRating) {
        this.averagePriceRating = averagePriceRating;
    }

    public int getRatingsCounter() {
        return ratingsCounter;
    }

    public void setRatingsCounter(int ratingsCounter) {
        this.ratingsCounter = ratingsCounter;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String[] getOtherImagesURL() {
        return otherImagesURL;
    }

    public void setOtherImagesURL(String[] otherImagesURL) {
        this.otherImagesURL = otherImagesURL;
    }

    public FacilityProfileDTO getFacilityProfile() {
        return facilityProfile;
    }

    public void setFacilityProfile(FacilityProfileDTO facilityProfile) {
        this.facilityProfile = facilityProfile;
    }

    public CategoryDTO[] getCategories() {
        return categories;
    }

    public void setCategories(CategoryDTO[] categories) {
        this.categories = categories;
    }

    public SportDTO[] getSports() {
        return sports;
    }

    public void setSports(SportDTO[] sports) {
        this.sports = sports;
    }

    public SupplementalEquipmentDTO[] getSupplementalEquipments() {
        return supplementalEquipments;
    }

    public void setSupplementalEquipments(SupplementalEquipmentDTO[] supplementalEquipments) {
        this.supplementalEquipments = supplementalEquipments;
    }

    public BaseEquipmentDTO[] getBaseEquipments() {
        return baseEquipments;
    }

    public void setBaseEquipments(BaseEquipmentDTO[] baseEquipments) {
        this.baseEquipments = baseEquipments;
    }

    public FacilityPromotionalCampaignDTO getPromotionNext30Days() {
        return promotionNext30Days;
    }

    public void setPromotionNext30Days(FacilityPromotionalCampaignDTO promotionNext30Days) {
        this.promotionNext30Days = promotionNext30Days;
    }

}


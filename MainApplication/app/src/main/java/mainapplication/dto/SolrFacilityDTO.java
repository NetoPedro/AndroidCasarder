package mainapplication.dto;

import java.io.Serializable;

import mainapplication.utils.SearchFilters;

/**
 * Created by Luis Gouveia on 10/01/2018.
 */

public class SolrFacilityDTO implements Serializable {
    private int id;
    private long _version_;
    private float weight;
    private String[] name;
    private String description;
    private double latitude;
    private double longitude;
    private String lanlon;
    private String[] city;
    private double averageRating;
    private double averagePremisesRating;
    private double averageAccessesRating;
    private double averagePriceRating;
    private int ratingsCounter;
    private String imageURL;
    private int[] categories;
    private int[] sports;
    private String promotion;

    public SolrFacilityDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long get_version_() {
        return _version_;
    }

    public void set_version_(long _version_) {
        this._version_ = _version_;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name[0];
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLanlon() {
        return lanlon;
    }

    public void setLanlon(String lanlon) {
        this.lanlon = lanlon;
    }

    public String getCity() {
        return city[0];
    }

    public void setCity(String[] city) {
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

    public int[] getCategories() {
        return categories;
    }

    public void setCategories(int[] categories) {
        this.categories = categories;
    }

    public int[] getSports() {
        return sports;
    }

    public void setSports(int[] sports) {
        this.sports = sports;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }
}

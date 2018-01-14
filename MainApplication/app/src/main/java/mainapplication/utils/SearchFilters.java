package mainapplication.utils;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Luis Gouveia on 22/12/2017.
 */

public class SearchFilters {

    private static int distance = 0;
    private static float rating = 0;
    private static float priceRating = 0;
    private static float premisesRating = 0;
    private static float accessRating = 0;
    private static List<Integer> categories = new ArrayList<>();
    private static List<Integer> sports = new ArrayList<>();
    private static boolean orderByDistance = false;
    private static boolean orderByRating = false;
    private static boolean orderByName = false;

    public static int getDistance() {
        return distance;
    }

    public static void setDistance(int distance) {
        SearchFilters.distance = distance;
    }

    public static float getRating() {
        return rating;
    }

    public static void setRating(float rating) {
        SearchFilters.rating = rating;
    }

    public static List<Integer> getCategories() {
        return categories;
    }

    public static void addCategory(int id){
        SearchFilters.categories.add(id);
    }

    public static void removeCategory(int id){
        SearchFilters.categories.remove((Integer) id);
    }

    public static boolean isOrderByDistance() {
        return orderByDistance;
    }

    public static void setOrderByDistance(boolean orderByDistance) {
        SearchFilters.orderByDistance = orderByDistance;
    }

    public static boolean isOrderByRating() {
        return orderByRating;
    }

    public static void setOrderByRating(boolean orderByRating) {
        SearchFilters.orderByRating = orderByRating;
    }

    public static boolean isOrderByName() {
        return orderByName;
    }

    public static void setOrderByName(boolean orderByName) {
        SearchFilters.orderByName = orderByName;
    }

    public static float getPriceRating() {
        return priceRating;
    }

    public static void setPriceRating(float priceRating) {
        SearchFilters.priceRating = priceRating;
    }

    public static float getPremisesRating() {
        return premisesRating;
    }

    public static void setPremisesRating(float premisesRating) {
        SearchFilters.premisesRating = premisesRating;
    }

    public static float getAccessRating() {
        return accessRating;
    }

    public static void setAccessRating(float accessRating) {
        SearchFilters.accessRating = accessRating;
    }

    public static List<Integer> getSports() {
        return sports;
    }

    public static void addSport(int id){
        SearchFilters.sports.add(id);
    }

    public static void removeSport(int id){
        SearchFilters.sports.remove((Integer) id);
    }


    public static void setSports(List<Integer> sports) {
        SearchFilters.sports = sports;
    }

    public static void clearFilters(){
        distance = 0;
        rating = 0;
        categories = new ArrayList<>();
        sports = new ArrayList<>();
        orderByDistance = false;
        orderByRating = false;
        orderByName = false;

    }
}

package mainapplication.dto;

import java.util.ArrayList;
import java.util.HashMap;

import mainapplication.activity.SuggesterActivity;

/**
 * Created by Luis Gouveia on 11/01/2018.
 */

public class SuggestDTO {
    private HashMap<String, Object> infixSuggester;
    private int numFound;
    private ArrayList<HashMap<String, Object>> suggestions;

    public SuggestDTO() {
    }

    public HashMap<String, Object> getInfixSuggester() {
        return infixSuggester;
    }

    public void setInfixSuggester(HashMap<String, Object> infixSuggester) {
        this.infixSuggester = infixSuggester;
        HashMap<String, Object> suggestionResults = (HashMap<String, Object>) infixSuggester.get(SuggesterActivity.text);
        numFound = (int) suggestionResults.get("numFound");
        suggestions = (ArrayList<HashMap<String, Object>>) suggestionResults.get("suggestions");

    }

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public ArrayList<HashMap<String, Object>> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(ArrayList<HashMap<String, Object>> suggestions) {
        this.suggestions = suggestions;
    }
}

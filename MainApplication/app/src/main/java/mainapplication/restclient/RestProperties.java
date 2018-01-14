package mainapplication.restclient;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class RestProperties {
    private static final String PROPERTIES_FILE = "restserver.properties";
    private InputStream inputStream;
    private Properties properties;

    public RestProperties(Context context){
        properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHost(){
        return properties.getProperty("webservice.host");
    }
    public String getDnsHost(){
        return properties.getProperty("webservice.dns.host");
    }

    public String getPort(){
        return properties.getProperty("webservice.port");
    }
    public String getSolrPort(){
        return properties.getProperty("webservice.solr.port");
    }

    public String getAppBaseUri(){
        return properties.getProperty("webservice.app.baseuri");
    }
    public String getSolrBaseUri(){
        return properties.getProperty("webservice.solr.baseuri");
    }

    public String getLanguageUri(){
        return  properties.getProperty("webservice.language.baseuri");
    }

    public String getFacilitiesBaseUri(){
        return  properties.getProperty("webservice.facilities.baseuri");
    }

    public String getTimeSlotsBaseUri() {
        return properties.getProperty("webservice.timeslots.baseuri");
    }


    public String getRatingsBaseUri() {
        return properties.getProperty("webservice.ratings.baseuri");
    }

    public String getCategoriesBaseUri() {
        return properties.getProperty("webservice.categories.baseuri");
    }

    public String getSportsBaseUri() {
        return properties.getProperty("webservice.sports.baseuri");
    }

    public String getScheme(){
        return properties.getProperty("webservice.scheme");
    }

    public String getNormalScheme(){
        return properties.getProperty("webservice.scheme.normal");
    }

    public String getConnectionUri(){
        return properties.getProperty("webservice.connection.baseuri");
    }


}

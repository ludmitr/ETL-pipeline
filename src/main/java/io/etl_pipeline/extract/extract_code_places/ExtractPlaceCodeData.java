package io.etl_pipeline.extract.extract_code_places;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Map;

public class ExtractPlaceCodeData {
    public static void main(String[] args) {
        ExtractPlaceCodeData test = new ExtractPlaceCodeData();
        List<Place> places = test.ExtractData(1);
        System.out.println("done");
    }

    final private String resourcesFolderPath = "src/main/resources/";

    final private String requestUrlTemplate = "https://gw.yad2.co.il/address-autocomplete/realestate?text=";
    private Map<Integer, String> placeTypeMappingToFileName = Map.of(1, "cities.json", 2, "kibutzim.json",
            3, "moshavim.json", 4, "notCityKibutzMoshav.json");
    private String[] placeNameForFilter = {"","עיר","קיבוץ","מושב","ישוב קהילתי"};
    // ישוב,ישוב קהילתי,מועצה מקומית,ישוב חקלאי,מושב
    // TO DO: placeType 4 add other places, not only ישוב קהילתי
    public ExtractPlaceCodeData() {
    }

    /*
    Extract data of cities to List<Place> and returns it.
    passing values : 1 for cities, 2 for kibutzim, 3 for moshavim, 4 for other notCityKibutzMoshav.
     */
    public List<Place> ExtractData(int placeTypeIndex) {
        if (placeTypeIndex < 1 || placeTypeIndex > 4) {
            throw new IllegalArgumentException("Invalid place type: " + placeTypeIndex + ". Expected values are 1, 2, 3, or 4.");
        }
        List<Place> foundPlaces = new ArrayList<>();
        try {
            String placeTypeFileName = placeTypeMappingToFileName.get(placeTypeIndex);
            String content = Files.readString(Paths.get(resourcesFolderPath+ placeTypeFileName));
            JSONArray places = new JSONArray(content);

            // running over all places and gathering data
            for (int i = 0; i < 20; i++) {
                // getting json data from api
                // TO DO: handle connection error
                // TO DO: add Asynchronous requests
                String encodedName = URLEncoder.encode(places.getString(i), StandardCharsets.UTF_8);
                String requestUrl = requestUrlTemplate + encodedName;
                String sourceData = sendGetRequest(requestUrl);

                //deserialize data for specific place
                Place deserializedPlace = DeserializeData(sourceData, placeNameForFilter[placeTypeIndex]);
                if (deserializedPlace != null) {
                    System.out.println("%d out of %d ".formatted(i, places.length()));
                    foundPlaces.add(deserializedPlace);
                } else {
                    System.out.println("Place type" + " not added: " + places.getString(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundPlaces;
    }

    private Place DeserializeData(String jsonData, String placeNameForFilter) {
        Gson gson = new Gson();
        Type placeListType = new TypeToken<List<Place>>() {}.getType();
        List<Place> placeList = gson.fromJson(jsonData, placeListType);
        Optional<Place> placeOptional = placeList.stream()
                .filter(place -> place.getInfo().equals(placeNameForFilter))
                .findFirst();

        return placeOptional.orElse(null);
    }

    private String sendGetRequest(String requestUrl) throws IOException {
        // TO DO: throw error connection exception and handle it in user of function
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Check the response code and read the response
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                return scanner.useDelimiter("\\A").next();
            }
        } else {
            // Handle non-OK response
            return "Error: " + connection.getResponseCode();
        }
    }
}

package io.etl_pipeline.transform;

import io.etl_pipeline.extract.extract_code_places.Place;
import io.etl_pipeline.model.LocationSearchData;

import java.util.List;
import java.util.ArrayList;

public class CodeDataTransformer {
    /**
     * Transform data from extracted List<Place> to the List<LocationSearchData>.
     *
     * @param placesExtracted extracted Places with related data from yad2
     * @return transformed data as List<LocationSearchData>
     */
    public List<LocationSearchData> transformCodePlaces(List<Place> placesExtracted) {
        System.out.println("Started transforming code data");
        List<LocationSearchData> placesForDB = new ArrayList<>();
        for (Place place : placesExtracted) {
            try {

                LocationSearchData transformedDataToAdd = LocationSearchData.builder()
                        .locationID(Integer.parseInt(place.getValue().getCity()))
                        .locationName(place.getText())
                        .areaID(parseToInt(place.getValue().getArea(), 0))
                        .topAreaID(parseToInt(place.getValue().getArea(), 0))
                        .info(place.getInfo())
                        .centerPoint(place.getOther().getCenterPoint())
                        .build();

                placesForDB.add(transformedDataToAdd);

            } catch (NumberFormatException e) {
                System.out.printf("location for - %s did not created, failed to parse extracted id - %s to int",
                        place.getText(), place.getValue().getCity());
            }
        }

        return placesForDB;
    }

    private int parseToInt(String stringToParse, int defaultValue) {
        try {
            return Integer.parseInt(stringToParse);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}

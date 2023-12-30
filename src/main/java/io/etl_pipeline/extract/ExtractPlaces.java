package io.etl_pipeline.extract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to extract ctities from axie.co.il ...
 */
public class ExtractPlaces {
    public static void main(String[] args) {
        String fileNameForPlaces = "notCityKibutzMoshav.txt";
        String url = "https://axie.co.il/%F0%9F%87%AE%F0%9F%87%B1-%D7%A8%D7%A9%D7%99%D7%9E%D7%AA-%D7%94%D7%99%D7%A9%D7%95%D7%91%D7%99%D7%9D-%D7%91%D7%99%D7%A9%D7%A8%D7%90%D7%9C-%D7%A2%D7%A8%D7%99%D7%9D-%D7%9E%D7%95%D7%A9%D7%91%D7%99%D7%9D/";
        String cssSelector = "#content > figure:nth-child(15) > table > tbody > tr";

        try {
            // getting the right tags
            Document doc = Jsoup.connect(url).get();
            Elements allTrTags = doc.select(cssSelector);

            // Saving places in txt file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameForPlaces))) {
                for (Element trTag : allTrTags) {
                    Element firstTd = trTag.select("td").first();
                    if (firstTd != null) {
                        String text = firstTd.text();
                        writer.write(text + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

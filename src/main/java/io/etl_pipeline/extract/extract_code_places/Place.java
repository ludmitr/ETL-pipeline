package io.etl_pipeline.extract.extract_code_places;

import java.util.Map;
import lombok.Data;

@Data
public class Place {
    private String text;
    private String info;
    private Value value;
    private Other other;
}

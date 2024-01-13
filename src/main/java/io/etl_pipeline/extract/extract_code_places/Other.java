package io.etl_pipeline.extract.extract_code_places;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Other {
    @SerializedName("center_point")
    private String centerPoint;
}

package io.etl_pipeline.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "location_search_data")
@Data
@Builder
public class LocationSearchData {
        @Id
        @Column(name = "location_id")
        private int locationID;

        @Column(name = "location_name")
        private String locationName;

        @Column(name = "area_id")
        private int areaID;

        @Column(name = "top_area_id")
        private int topAreaID;

        @Column(name = "info")
        private String info;

        @Column(name = "center_point")
        private String centerPoint;
}

package com.swpu.uchain.openexperiment.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import lombok.Data;

/**
 * @author dengg
 */
@Data
public class ConclusionDTO {

    private Long projectId;


    /**
     * 所属学院  {@link CollegeType#getValue()}
     */
    @JsonIgnore
    private Integer subordinateCollege;

}

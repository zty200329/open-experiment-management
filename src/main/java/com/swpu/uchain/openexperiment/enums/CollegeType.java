package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author panghu
 */
@Getter
public enum  CollegeType {
    /**
     *
     */
    FUNCTIONAL_DEPARTMENT(0,"职能部门"),
    MARXISM_COLLEGE(1,"石油与天然气工程学院"),
    ART_COLLEGE(2,"地球科学与技术学院"),
    CHEMICAL_COLLEGE(3,"机电工程学院"),
    EARTH_SCIENCE_AND_TECHNOLOGY_COLLEGE(4,"化学化工学院"),
    PETROLEUM_ENGINEERING_COLLEGE(5,"材料科学与工程学院"),
    ELECTRICAL_INFORMATION_COLLEGE(6,"计算机科学学院"),
    ECONOMIC_MANAGEMENT_COLLEGE(7,"电气信息学院"),
    SPORTS_COLLEGE(8,"土木工程与建筑学院"),
    MECHANICAL_AND_ELECTRICAL_ENGINEERING_COLLEGE(9,"理学院"),
    MATERIALS_SCIENCE_AND_ENGINEERING_COLLEGE(10,"经济管理学院"),
    RICHARD_LEARNING_COLLEGE(11,"法学院"),
    CIVIL_ENGINEERING_AND_ARCHITECTURE_COLLEGE(12,"外国语学院"),
    LAW_COLLEGE(13,"体育学院"),
    FOREIGN_LANGUAGES_COLLEGE(14,"艺术学院"),
    COMPUTER_SCIENCE_COLLEGE(15,"马克思主义学院");

    CollegeType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }


    /**
     * 值
     */
    private Integer value;

    /**
     * 提示
     */
    private String tips;

}

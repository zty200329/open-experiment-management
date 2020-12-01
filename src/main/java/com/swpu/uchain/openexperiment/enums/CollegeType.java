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
    FUNCTIONAL_DEPARTMENT(39,"职能部门"),
    MARXISM_COLLEGE(1,"石油与天然气工程学院"),
    EARTH_COLLEGE(2,"地球科学与技术学院"),
    CHEMICAL_COLLEGE(3,"机电工程学院"),
    EARTH_SCIENCE_AND_TECHNOLOGY_COLLEGE(4,"化学化工学院"),
    PETROLEUM_ENGINEERING_COLLEGE(5,"新能源与材料学院"),
    ELECTRICAL_INFORMATION_COLLEGE(6,"计算机科学学院"),
    ECONOMIC_MANAGEMENT_COLLEGE(7,"电气信息学院"),
    SPORTS_COLLEGE(8,"土木工程与建筑学院"),
    MECHANICAL_AND_ELECTRICAL_ENGINEERING_COLLEGE(9,"理学院"),
    MATERIALS_SCIENCE_AND_ENGINEERING_COLLEGE(10,"经济管理学院"),
    RICHARD_LEARNING_COLLEGE(11,"法学院"),
    ACADEMY_OF_MARXISM_COLLEGE(12,"马克思主义学院"),
    FOREIGN_LANGUAGE_COLLEGE(13,"外国语学院"),
    PHYSICAL_COLLEGE(14,"体育学院"),
    ART_COLLEGE(15,"艺术学院"),
    ENGINEERING_COLLEGE(76,"工程学院"),
    INFORMATION_COLLEGE(77,"信息学院"),
    FINANCE_COLLEGE(78,"财经学院");

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

package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author panghu
 */
@Getter
public enum GroupType {
    /**
     * 石工和地质勘探
     */
    PETROLEUM_AND_GEOLOGICAL_EXPLORATION(1),
    /**
     * 化工材料
     */
    CHEMICAL_INDUSTRY_AND_MATERIAL(2),
    /**
     * 机械力学
     */
    MECHANICAL_MECHANICS(3),
    /**
     * 计算机应用
     */
    COMPUTER_APPLICATION(4),
    /**
     * 软件与数学
     */
    SOFTWARE_AND_MATHEMATICS(5),
    /**
     *  经济管理,法学,艺术,人文
     */
    ECONOMIC_MANAGEMENT_LAW_ART_AND_HUMANITIES(6)
    ;

    GroupType(Integer value) {
        this.value = value;
    }



    private Integer value;

}

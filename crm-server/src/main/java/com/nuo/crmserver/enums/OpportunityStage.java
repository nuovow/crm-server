package com.nuo.crmserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpportunityStage {

    INITIAL("初期沟通"),
    PROPOSAL("方案报价"),
    NEGOTIATION("商务谈判"),
    WON("赢单"),
    LOST("输单");

    private final String label;

    public static OpportunityStage of(String value) {
        for (OpportunityStage stage : values()) {
            if (stage.name().equalsIgnoreCase(value)) {
                return stage;
            }
        }
        return null;
    }
}

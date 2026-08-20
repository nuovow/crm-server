package com.nuo.crmserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FollowType {

    PHONE("电话"),
    VISIT("拜访"),
    WECHAT("微信"),
    EMAIL("邮件");

    private final String label;

    public static FollowType of(String value) {
        for (FollowType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}

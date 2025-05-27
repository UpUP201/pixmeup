package com.corp.pixelro.check.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AmslerStatus {
    NORMAL("정상 시야"),
    ABNORMAL("시야 이상");

    private final String desc;

    public static AmslerStatus inferAmslerStatus(String left, String right) {
        String all = left + "," + right;
        boolean hasW = all.contains("w");
        boolean hasD = all.contains("d");
        boolean hasB = all.contains("b");
        boolean allNormal = all.replace(",", "").replace("n", "").isEmpty();
    
        if (allNormal) {
            return AmslerStatus.NORMAL;
        }
        if (hasB && (hasW || hasD)) {
            return AmslerStatus.ABNORMAL;
        }
        if (hasB) {
            return AmslerStatus.ABNORMAL;
        }
        if (hasD) {
            return AmslerStatus.ABNORMAL;
        }
        if (hasW) {
            return AmslerStatus.ABNORMAL;
        }
        return AmslerStatus.ABNORMAL;
    }

}

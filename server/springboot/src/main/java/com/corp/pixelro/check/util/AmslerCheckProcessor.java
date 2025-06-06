package com.corp.pixelro.check.util;

import com.corp.pixelro.check.entity.AmslerCheck;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AmslerCheckProcessor {
    public static Map<String, Object> process(AmslerCheck check) {
        long normal = 0, distorted = 0, blacked = 0, whited = 0;

        normal += countByCode(check.getLeftMacularLoc(), 'n');
        distorted += countByCode(check.getLeftMacularLoc(), 'd');
        blacked += countByCode(check.getLeftMacularLoc(), 'b');
        whited += countByCode(check.getLeftMacularLoc(), 'w');

        normal += countByCode(check.getRightMacularLoc(), 'n');
        distorted += countByCode(check.getRightMacularLoc(), 'd');
        blacked += countByCode(check.getRightMacularLoc(), 'b');
        whited += countByCode(check.getRightMacularLoc(), 'w');

        boolean abnormal = distorted + blacked + whited > 0;

        Map<String, Object> map = new HashMap<>();
        map.put("mchart_check_id", check.getId());
        map.put("amsler_normal_count", normal);
        map.put("amsler_distorted_count", distorted);
        map.put("amsler_blacked_count", blacked);
        map.put("amsler_whited_count", whited);
        map.put("amsler_abnormal_flag", abnormal);
        return map;
    }

    private static long countByCode(String raw, char code) {
        return Arrays.stream(raw.split(","))
                .filter(s -> s.equalsIgnoreCase(String.valueOf(code)))
                .count();
    }

    public static String mapDisorderListToCodeString(List<String> input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        return input.stream()
                .map(s -> switch (s) {
                    case "Normal" -> "n";
                    case "Distorted" -> "d";
                    case "Blacked" -> "b";
                    case "Whited" -> "w";
                    default -> "x";
                })
                .collect(Collectors.joining(","));
    }
}

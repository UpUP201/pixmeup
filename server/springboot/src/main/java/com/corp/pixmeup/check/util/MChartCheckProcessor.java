package com.corp.pixmeup.check.util;

import com.corp.pixmeup.check.entity.MChartCheck;

import java.util.HashMap;
import java.util.Map;

public class MChartCheckProcessor {
    public static Map<String, Object> process(MChartCheck check) {
        boolean abnormal = check.getLeftEyeHor() >= 5 || check.getLeftEyeVer() >= 5
                || check.getRightEyeHor() >= 5 || check.getRightEyeVer() >= 5;

        Map<String, Object> map = new HashMap<>();
        map.put("amsler_check_id", check.getId());
        map.put("left_eye_horizontal", check.getLeftEyeHor());
        map.put("left_eye_vertical", check.getLeftEyeVer());
        map.put("right_eye_horizontal", check.getRightEyeHor());
        map.put("right_eye_vertical", check.getRightEyeVer());
        map.put("mchart_abnormal_flag", abnormal);
        return map;
    }

}

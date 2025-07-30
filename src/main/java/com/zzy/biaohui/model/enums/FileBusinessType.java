package com.zzy.biaohui.model.enums;

import com.zzy.biaohui.model.vo.LabelValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FileBusinessType {
    POINT(0, "点"),
    LINE(1, "线"),
    POLYGON(2, "面"),
    MODEL(3, "模型");

    private int value;
    private String label;
    FileBusinessType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static List<LabelValue> getValues() {
        FileBusinessType[] values = FileBusinessType.values();
        List<LabelValue> res = Arrays.stream(values).map(item -> {
            LabelValue labelValue = new LabelValue(item.getLabel(), String.valueOf(item.getValue()));
            return labelValue;
        }).collect(Collectors.toList());
        return res;
    }
}

package tools;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ToolFunctions {
    // 利用反射填充数组
    public static void fillData(Object array, String content, int globalDim, int currentDim, TypeParser parser) {
        int arrayLength = Array.getLength(array);
        if (currentDim == globalDim) {
            int startIdx = 1;
            int endIdx = 1;
            while (!content.substring(endIdx, endIdx+1).equals("]")) {
                endIdx += 1;
                continue;
            }
            String[] nums = content.substring(startIdx, endIdx).split(",");
            assert nums.length == arrayLength;
            for (int i = 0; i < arrayLength; i++) {
//                Array.setInt(array, i, Integer.parseInt(nums[i]));
                parser.setArrayValue(array, i, nums[i]);
            }
        } else {
            int startIdx = 0;
            int endIdx = 0;
            int tag = 0;
            int idx = 0;
            String[] contents = new String[arrayLength];
            while (endIdx < content.length()) {
                if (content.charAt(endIdx) == '[') {
                    tag += 1;
                    if (tag == 2) {
                        startIdx = endIdx;
                    }
                } else if (content.charAt(endIdx) == ']') {
                    tag -= 1;
                    if (tag == 1) {
                        contents[idx] = content.substring(startIdx, endIdx + 1);
                        idx += 1;
                    }
                }
                endIdx += 1;
            }
            for (int i = 0; i < arrayLength; i++) {
                fillData(Array.get(array, i), contents[i], globalDim, currentDim+1, parser);
            }
        }
    }

    // 将反射类数组转化为字符串
    public static String Object2StringTool(Object array, int globalDim, int currentDim) {
        if (array == null) {
            return "null";
        }
        List<String> result = new ArrayList<>();
//        result.add("[");
        for (int i = 0; i < Array.getLength(array); i++) {
            if (globalDim == currentDim) {
                result.add(Array.get(array, i).toString());
            } else {
                result.add(Object2StringTool(Array.get(array, i), globalDim, currentDim+1));
            }
        }
//        result.add("]");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i)).append(",");
        }
        return result.isEmpty()?"[]":"[" + sb.toString().substring(0, sb.toString().length() - 1) + "]";
    }
}

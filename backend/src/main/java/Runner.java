import tools.TypeCaster;
import tools.TypeParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Runner {

    public void run() {
        try {
            String path = System.getProperty("user.dir");
            System.out.println("项目路径： " + path);

            // 从 resources 文件夹中读取 leetcode 配置项
            InputStream is = FileGenerator.class.getClassLoader().getResourceAsStream("test.txt");
            assert is != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String methodContent = reader.readLine();
            String inputContent = reader.readLine();
            String outputContent = reader.readLine();

            // 处理输入数据.
            String methodName = methodContent.split("\\(")[0].split(" ")[2];
            SolutionClassLoader loader = new SolutionClassLoader("Solution", methodName);
            int parametersLength = loader.getTypeNameList().size();
            Object[] inputParams = new Object[parametersLength];
            List<String> inputContents = getInputContents(inputContent);
            for (int i = 0; i < loader.getTypeNameList().size(); i++) {
                String typeName = loader.getTypeNameList().get(i);
                String paramContent = formatContent(inputContents.get(i));
                TypeParser parser = TypeCaster.stringCast2Type(typeName);
                Object param = parser.getString2ObjectContent(paramContent, typeName);
                inputParams[i] = param;
            }

            // 处理输出数据
            String targetResult = formatContent(outputContent);

            //调用方法
            System.out.println("正在执行方法...");
            Object result = loader.getTargetMethod().invoke(loader.getClazz().newInstance(), inputParams);
            String returnType = loader.getReturnType();
            TypeParser parser = TypeCaster.stringCast2Type(returnType);
            System.out.println();
            if (targetResult.equals(parser.getObject2StringContent(result))) {
                System.out.println("恭喜！答案正确。");
            } else {
                System.out.println("解答错误！");
                System.out.println("标准输出：" + targetResult);
                System.out.println("我的输出：" + parser.getObject2StringContent(result));
            }

        } catch (IOException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public List<String> getInputContents(String content) {
        int startIdx = 0;
        int endIdx = 0;
        int tag = 0;
        List<String> inputContents = new ArrayList<>();
        content += ",";
        while (endIdx < content.length()) {
            if (content.charAt(endIdx) == '=') {
                startIdx = endIdx;
            } else if (content.charAt(endIdx) == '[') {
                tag += 1;
            } else if (content.charAt(endIdx) == ']') {
                tag -= 1;
                if (tag == 0) {
                    inputContents.add(content.substring(startIdx+1, endIdx+1));
                    startIdx = -1;
                }
            } else if (content.charAt(endIdx) == ',' && tag == 0 && startIdx >= 0) {
                inputContents.add(content.substring(startIdx+1, endIdx));
                startIdx = -1;
            }
            endIdx += 1;
        }
        return inputContents;
    }

    public static String formatContent(String content) {
        content = content.replaceAll("\\t|\\s|\\n", "");
        return content;
    }

}

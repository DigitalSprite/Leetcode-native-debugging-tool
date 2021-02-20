import tools.TypeCaster;
import tools.TypeParser;

import java.io.*;

public class FileGenerator {

    public static void createClass() {
        try {
            String path = System.getProperty("user.dir");
            System.out.println("Project Path: " + path);

            // 从 resources 文件夹中读取 leetcode 配置项
            InputStream is = FileGenerator.class.getClassLoader().getResourceAsStream("test.txt");
            assert is != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // 生成 Solution.java 文件的文本内容
            String content = "";
            String className = "Solution";
            content += "public class " + className + " {\n";
            String function = reader.readLine();
            content += "\t" + function + " {\n";
            String returnTypeName = function.split(" ")[1];
            TypeParser parser = TypeCaster.stringCast2Type(returnTypeName);

            if (!returnTypeName.equals("void")) {
                content += "\t\t" + parser.getReturnContent() + "\n";
            }
            content += "\t}\n" ;
            content += "}\n";
            is.close();

            // 删除旧文件
            File file = new File(path + "/" + className + ".java");
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println(file.getName() + " 文件已删除！");
                } else {
                    System.out.println("文件删除失败！");
                }
            } else {
                System.out.println("文件首次创建！");
            }

            BufferedWriter out = new BufferedWriter(new FileWriter(path + "/" + className + ".java"));
            out.write(content);
            out.close();
            System.out.println("文件创建成功！");

        } catch (IOException e) {
            System.out.println("error");
            System.out.println(e.getMessage());
        }
    }

    public static void print(Object[] objects) {
        for (Object o : objects) {
            System.out.print(o.toString() + " & ");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {
        createClass();
    }
}

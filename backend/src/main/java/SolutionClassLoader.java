import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SolutionClassLoader extends ClassLoader{

    private String path;

    private final Class<?> clazz;
    private Method targetMethod;
    private List<String> typeNameList;
    private String returnTypeName;

    public Class<?> getClazz() {
        return clazz;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public List<String> getTypeNameList() {
        return typeNameList;
    }

    public String getReturnType() {
        return returnTypeName;
    }

    public void setPath(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在！");
            this.path = null;
        } else {
            this.path = path;
            JavaCompiler javac= ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager javafile=javac.getStandardFileManager(null, null, null);
            //编译单元，可以有多个
            Iterable units=javafile.getJavaFileObjects(path);
            //编译任务
            JavaCompiler.CompilationTask t=javac.getTask(null, javafile, null, null, null, units);
            t.call();
            javafile.close();
        }
    }

    @Override
    protected Class<?> findClass(String className) {
        byte[] classBytes = null;
        try {
            if (this.path != null) {
                Path path = Paths.get(new URI(className));
                classBytes = Files.readAllBytes(path);
                return defineClass(classBytes, 0, classBytes.length);
            } else {
                System.out.println("Generate Class Loader Failed!");
                return null;
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SolutionClassLoader(String className, String methodName) throws IOException, NoSuchMethodException{
        String path = System.getProperty("user.dir");
        String filePath = path + "\\" + className + ".java";
        String classPath = "file:/" + path + "\\" +className + ".class";
        classPath = classPath.replaceAll("\\\\", "/");
        System.out.println("class文件路径：" + classPath);
        setPath(filePath);
        clazz = findClass(classPath);
        typeNameList = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                m.setAccessible(true);
                targetMethod = m;
                for (Type t : m.getGenericParameterTypes()) {
                    typeNameList.add(t.getTypeName());
                }
                returnTypeName = m.getGenericReturnType().getTypeName();
            }
        }
        if (targetMethod == null) {
            throw new NoSuchMethodException();
        }
    }
}

package tools;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static tools.ToolFunctions.fillData;
import static tools.ToolFunctions.Object2StringTool;

public class TypeParser {

    private Status status;

    public enum Status {
        VOID ("void") {
            @Override
            public Class getType(String name) {
                return void.class;
            }
            @Override
            public String getReturnContent() {
                return "";
            }
        },
        BYTE ("byte") {
            @Override
            public Class getType(String name) {
                return byte.class;
            }
            @Override
            public String getReturnContent() {
                return "return 0;";
            }
            @Override
            public Object String2Object(String content, String type) {
                try {
                    byte[] bytes = content.getBytes(content);
                    return bytes[0];
                } catch (UnsupportedEncodingException e) {
                    System.out.println("cannot cast content into byte");
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setByte(array, index, Byte.parseByte(value));
            }
        },
        CHAR ("char") {
            @Override
            public Class getType(String name) {
                return char.class;
            }
            @Override
            public String getReturnContent() {
                return "return '\0';";
            }
            @Override
            public Object String2Object(String content, String type) {
                char[] chars = new char[1];
                content.getChars(0, 1, chars, 0);
                return chars[0];
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setChar(array, index, value.charAt(0));
            }
        },
        SHORT ("short") {
            @Override
            public Class getType(String name) {
                return short.class;
            }
            @Override
            public String getReturnContent() {
                return "return 0;";
            }
            @Override
            public Object String2Object(String content, String type) {
                return Short.parseShort(content);
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setShort(array, index, Short.parseShort(value));
            }
        },
        INT ("int") {
            @Override
            public Class getType(String name) {
                return int.class;
            }
            @Override
            public String getReturnContent() {
                return "return 0;";
            }
            @Override
            public Object String2Object(String content, String type) {
                return Integer.parseInt(content);
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setInt(array, index, Integer.parseInt(value));
            }
        },
        LONG ("long") {
            @Override
            public Class getType(String name) {
                return long.class;
            }
            @Override
            public String getReturnContent() {
                return "return 0L;";
            }
            @Override
            public Object String2Object(String content, String type) {
                return Long.parseLong(content);
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setLong(array, index, Long.parseLong(value));
            }
        },
        FLOAT ("float") {
            @Override
            public Class getType(String name) {
                return float.class;
            }
            @Override
            public String getReturnContent() {
                return "return 0.0f;";
            }
            @Override
            public Object String2Object(String content, String type) {
                return Float.parseFloat(content);
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setFloat(array, index, Float.parseFloat(value));
            }
        },
        DOUBLE ("double") {
            @Override
            public Class getType(String name) {
                return double.class;
            }
            @Override
            public String getReturnContent() {
                return "return 0.0;";
            }
            @Override
            public Object String2Object(String content, String type) {
                return Double.parseDouble(content);
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setDouble(array, index, Double.parseDouble(value));
            }
        },
        BOOLEAN("boolean") {
            @Override
            public Class getType(String name) {
                return boolean.class;
            }
            @Override
            public String getReturnContent() {
                return "return false;";
            }
            @Override
            public Object String2Object(String content, String type) {
                return Boolean.parseBoolean(content);
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.setBoolean(array, index, Boolean.parseBoolean(value));
            }
        },
        STRING("string") {
            @Override
            public Class getType(String name) {
                return String.class;
            }
            @Override
            public String getReturnContent() {
                return "return \"\";";
            }
            @Override
            public Object String2Object(String content, String type) {
                return content;
            }
            @Override
            public void setArrayValue(Object array, int index, String value) {
                Array.set(array, index, value);
            }
        },
        ARRAY("array") {
            @Override
            public Class<?> getType(String name) {
                String baseTypeName = name.split("\\[")[0];
                TypeParser parser = TypeCaster.stringCast2Type(baseTypeName);
                Type baseClass = parser.status.getType(baseTypeName);
                int[] arrayDimList = new int[name.split("\\[").length - 1];
                Object array = Array.newInstance((Class<?>) baseClass, arrayDimList);
                return array.getClass();
            }
            @Override
            public String getReturnContent() {
                return "return null;";
            }
            @Override
            public Object String2Object(String content, String type) {
                type = type.split("\\[")[0];
                TypeParser parser = TypeCaster.stringCast2Type(type);
                content = content.replaceAll("\\t|\\s|\\n", "");
                int dim = this.getDim();
                List<Integer> dimList = new ArrayList<>();
                for (int startIdx = 0; startIdx < dim; startIdx++) {
                    int num = 1;
                    int tag = 0;
                    for (int i = startIdx; i < content.length(); i++) {
                        if (content.charAt(i) == '[') {
                            tag += 1;
                        } else if (content.charAt(i) == ']') {
                            tag -= 1;
                            if (tag == 0)
                                break;
                        }
                        if (tag == 1 && content.charAt(i) == ',')
                            num += 1;
                    }
                    dimList.add(num);
                }
                int[] dimArray = new int[dim];
                for (int i = 0; i < dim; i++) {
                    dimArray[i] = dimList.get(i);
                }
                Object array = Array.newInstance(parser.getStatus().getType(type), dimArray);
                fillData(array, content, dim, 1, parser);
                return array;
            }
            @Override
            public String Object2String(Object array) {
                int dim = getDim();
                return Object2StringTool(array, dim, 1);
            }
        };


        private String type;
        private int dim;

        public int getDim() {
            return dim;
        }

        public void setDim(int dim) {
            this.dim = dim;
        }

        public String getTypeName() {
            return type;
        }

        public void setTypeName(String type) {
            this.type = type;
        }

        Status (String type) {
            this.type = type;
        }

        // 需要重写的方法
        public Class<?> getType(String name) {return null;} // 根据名字获得这个类的类型
        public String getReturnContent() {return "";} // 根据不同的Status类型产生不一样的return语句。void无return语句
        public Object String2Object(String content, String type) {return null;} // 将content文本表示的数据转化为type类型
        public void setArrayValue(Object array, int index, String value) {} // 利用反射，将数值填充到反射创建的数组中，基本类型会使用到它
        public String Object2String(Object object) {return object.toString();} // 将object转化为string文本


    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status, String typeName) {
        this.status = status;
        this.status.type = typeName;
        if (this.status == Status.ARRAY) {
            int dim = 0;
            boolean tag = true;
            dim = typeName.split("\\[").length - 1;
            this.status.dim = dim;
        } else {
            this.status.dim = 0;
        }
    }

    public String getReturnContent() {
        return status.getReturnContent();
    }

    public Object getString2ObjectContent(String content, String type) {
        return status.String2Object(content, type);
    }

    public String getObject2StringContent(Object array) {
        return status.Object2String(array);
    }

    public void setArrayValue(Object array, int index, String value) {
        status.setArrayValue(array, index, value);
    }



    public static void main(String[] args) {
        // 测试用例1
        String typeName = "int[][]";
        typeName = typeName.replaceAll("\\t|\\s|\\n", "");
        String input = "[[1,2], [3,4]]";
        TypeParser parser = TypeCaster.stringCast2Type(typeName);
        Object content = parser.getString2ObjectContent(input, typeName);
        System.out.println(Array.getInt(Array.get(content, 0), 1));

        // 测试用例2
//        String typeName2 = "String";
//        typeName2 = typeName2.replaceAll("\\t|\\s|\\n", "");
//        String input2 = "hello world!";
//        TypeParser parser2 = TypeCaster.stringCast2Type(typeName2);
//        System.out.println(parser2.getStatus());
//        Object content2 = parser2.getString2ObjectContent(input2, typeName2);
//        System.out.println(content2.toString());

        // 测试用例3

//        String typeName = "double";
//        TypeParser parser = TypeCaster.stringCast2Type(typeName);
////        Type t = parser.getStatus().getType(typeName);
//        Object content = parser.getString2ObjectContent("\t1.2");
//        System.out.println(content);

//        Object array1 = Array.get(array, 0);
//        Array.setInt(array1, 0, 1);
//
//        Object testArray = Array.newInstance(int.class, new int[]{3,2});
//        fillData(testArray, "[[1,2],[3,4],[5,6]]", 2, 1);
    }

}

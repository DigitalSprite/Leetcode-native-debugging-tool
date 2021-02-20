package tools;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeCaster {

    public static TypeParser stringCast2Type(String typeName) {
        TypeParser parser = new TypeParser();
        if (typeName.contains("[")) {
            parser.setStatus(TypeParser.Status.ARRAY, typeName);
        }
        else {
            switch (typeName) {
                case "void":
                    parser.setStatus(TypeParser.Status.VOID, typeName);
                    break;
                case "short":
                    parser.setStatus(TypeParser.Status.SHORT, typeName);
                    break;
                case "int":
                    parser.setStatus(TypeParser.Status.INT, typeName);
                    break;
                case "long":
                    parser.setStatus(TypeParser.Status.LONG, typeName);
                    break;
                case "double":
                    parser.setStatus(TypeParser.Status.DOUBLE, typeName);
                    break;
                case "float" :
                    parser.setStatus(TypeParser.Status.FLOAT, typeName);
                    break;
                case "byte":
                    parser.setStatus(TypeParser.Status.BYTE, typeName);
                    break;
                case "char":
                    parser.setStatus(TypeParser.Status.CHAR, typeName);
                    break;
                case "boolean":
                    parser.setStatus(TypeParser.Status.BOOLEAN, typeName);
                    break;
                case "String":
                    parser.setStatus(TypeParser.Status.STRING, typeName);
                    break;
            }
        }
        return parser;
    }

}

package com.ljf.ruleproject.util;

import com.ljf.ruleproject.poet.SQLField;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2020/7/1
 */
public class BusinessClassUtil {

    public static ClassInfo getData() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setName("Store1");
        classInfo.setPackageName("com.ljf.ruleproject.poet");

        List<Attr> attrList = new ArrayList<>();

        Attr attr1 = new Attr();
        attr1.setType("java.lang.String");
        attr1.setVar("id");
        attr1.setField("id");
        attrList.add(attr1);

        Attr attr2 = new Attr();
        attr2.setType("java.lang.Integer");
        attr2.setVar("signValue");
        attr2.setField("sign_value");
        attrList.add(attr2);

        Attr attr3 = new Attr();
        attr3.setType("java.lang.String");
        attr3.setVar("attribute");
        attr3.setField("attribute");
        attrList.add(attr3);

        Attr attr4 = new Attr();
        attr4.setType("java.lang.String");
        attr4.setVar("figure");
        attr4.setField("figure");
        attrList.add(attr4);

        Attr attr5 = new Attr();
        attr5.setType("java.lang.String");
        attr5.setVar("size");
        attr5.setField("size");
        attrList.add(attr5);

        Attr attr6 = new Attr();
        attr6.setType("java.lang.Integer");
        attr6.setVar("sales");
        attr6.setField("sales");
        attrList.add(attr6);

        Attr attr7 = new Attr();
        attr7.setType("java.lang.Integer");
        attr7.setVar("returns");
        attr7.setField("returns");
        attrList.add(attr7);

        Attr attr8 = new Attr();
        attr8.setType("java.lang.Integer");
        attr8.setVar("integral");
        attr8.setField("integral");
        attrList.add(attr8);

        classInfo.setAttrList(attrList);
        return classInfo;
    }

//    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, URISyntaxException {
//
//        JavaFile javaFile = JavaPoet.createJavaFile(getData());
//        Class clazz = ClassCreator.createClass(javaFile);
//
//        Object data = clazz.newInstance();
//
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            SQLField sqlField = field.getAnnotation(SQLField.class);
//            if (sqlField == null) {
//                continue;
//            }
//            Class fieldType = field.getType();
//            String dbField = sqlField.value();
//            System.out.println("fieldType" + fieldType + "dbField" + dbField);
//            field.setAccessible(true);
//            field.set(data, dbField);
//        }
//
//    }

    public static List coverList(ResultSet resultSet, Class<?> clazz) throws IllegalAccessException, InstantiationException, SQLException {
        List dataList = new ArrayList();

        while (resultSet.next()) {
            Object data = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                SQLField sqlField = field.getAnnotation(SQLField.class);
                if (sqlField == null) {
                    continue;
                }
                Class fieldType = field.getType();
                String dbField = sqlField.value();
                Object value = null;

                switch (fieldType.toString()) {
                    case "class java.lang.String":
                        value = resultSet.getString(dbField);
                        break;
                    case "class java.lang.Integer":
                        value = resultSet.getInt(dbField);
                        break;
                }

                field.setAccessible(true);
                field.set(data, value);
            }
            dataList.add(data);
        }

        return dataList;
    }

//    public static void main(String[] args) throws IllegalAccessException {
//        Store store = new Store();
//        store.setId("1");
//        store.setSignValue(2);
//        store.setAttribute("attribute");
//        store.setFigure("figure");
//        store.setSize("size");
//        store.setSales(3);
//        store.setReturns(4);
//        store.setIntegral(5);
//
//        System.out.println(getInsertSQL(store));
//    }

    public static String getUpdateSQL(Object data) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder(" set ");

        Class clazz = data.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Object id = null;
        for (Field field : fields) {
            SQLField sqlField = field.getAnnotation(SQLField.class);
            if (sqlField == null) {
                continue;
            }
            Class fieldType = field.getType();
            String dbField = sqlField.value();
            field.setAccessible(true);
            Object value = field.get(data);

            if (dbField.equals("id")) {
                id = value;
                continue;
            }

            stringBuilder.append(dbField);
            switch (fieldType.toString()) {
                case "class java.lang.String":
                    stringBuilder.append(" = ").append("\'").append(value).append("\' ,");
                    break;
                case "class java.lang.Integer":
                    stringBuilder.append(" = ").append(value).append(" ,");
                    break;
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("where id = ").append(id).append(";");

        return stringBuilder.toString();
    }

    public static String getInsertSQL(Object data) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder(" ( ");

        Class clazz = data.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String[] keys = new String[fields.length];
        String[] values = new String[fields.length];

        StringBuilder keysStr = new StringBuilder();
        StringBuilder valuesStr = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];

            SQLField sqlField = field.getAnnotation(SQLField.class);
            if (sqlField == null) {
                continue;
            }

            Class fieldType = field.getType();
            String dbField = sqlField.value();
            field.setAccessible(true);
            Object value = field.get(data);

            keysStr.append(dbField).append(",");


            switch (fieldType.toString()) {
                case "class java.lang.String":
                    valuesStr.append(" '").append(value).append("' ,");
                    break;
                case "class java.lang.Integer":
                    valuesStr.append(" ").append(value).append(" ,");
                    break;
            }
        }
        keysStr.deleteCharAt(keysStr.length() - 1);
        valuesStr.deleteCharAt(valuesStr.length() - 1);

        stringBuilder.append(keysStr)
                .append(" ) ")
                .append("values (")
                .append(valuesStr)
                .append(")");

        return stringBuilder.toString();
    }

}

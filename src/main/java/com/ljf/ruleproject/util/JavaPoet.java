package com.ljf.ruleproject.util;

import com.ljf.ruleproject.poet.SQLField;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

/**
 * Created by mr.lin on 2020/7/1
 * 类文件生成
 */
public class JavaPoet {

    public static JavaFile createJavaFile(ClassInfo classInfo) throws ClassNotFoundException, IOException {

        TypeSpec.Builder builder = TypeSpec.classBuilder(classInfo.getName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Class.forName("java.io.Serializable"))
                .addAnnotation(Data.class)
                .addJavadoc("Created by mr.lin \nengine by JavaPoet \n");


        for (Attr attr : classInfo.getAttrList()) {
            AnnotationSpec annotationSpec = AnnotationSpec
                    .builder(SQLField.class)
                    .addMember("value", "\"" + attr.getField() + "\"")
                    .build();
            FieldSpec fieldSpec = FieldSpec.builder(Class.forName(attr.getType()), attr.getVar(), Modifier.PRIVATE)
                    .addAnnotation(annotationSpec).build();
            builder.addField(fieldSpec);
        }

        JavaFile javaFile = JavaFile.builder(classInfo.getPackageName(), builder.build()).build();
        System.out.println(javaFile);
        return javaFile;
    }

}

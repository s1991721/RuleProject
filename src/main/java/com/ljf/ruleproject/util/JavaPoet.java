package com.ljf.ruleproject.util;

import com.ljf.ruleproject.poet.SQLField;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2020/7/1
 */
public class JavaPoet {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        new JavaPoet().test();
    }

    private void test() throws ClassNotFoundException, IOException {
        List<Attr> attrList = new ArrayList<>();
        Attr a = new Attr();
        a.setType("java.lang.String");
        a.setVar("aa");
        a.setField("_aa");
        attrList.add(a);

        TypeSpec.Builder builder = TypeSpec.classBuilder("test")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Class.forName("java.io.Serializable"))
                .addAnnotation(Data.class)
                .addJavadoc("Created by mr.lin \nengine by JavaPoet \n");


        for (Attr attr : attrList) {
            AnnotationSpec annotationSpec = AnnotationSpec
                    .builder(SQLField.class)
                    .addMember("value", "\"" + attr.getField() + "\"")
                    .build();
            FieldSpec fieldSpec = FieldSpec.builder(Class.forName(attr.getType()), attr.getVar(), Modifier.PRIVATE)
                    .addAnnotation(annotationSpec).build();
            builder.addField(fieldSpec);
        }


        JavaFile javaFile = JavaFile.builder("com.ljf.ruleproject.poet", builder.build()).build();

        File file = new File("src\\main\\java");
        javaFile.writeTo(file);

        System.out.println(javaFile);
    }

}

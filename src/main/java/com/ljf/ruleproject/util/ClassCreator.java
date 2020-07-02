package com.ljf.ruleproject.util;

import com.squareup.javapoet.JavaFile;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Created by mr.lin on 2020/7/2
 * 类生成器
 */
public class ClassCreator {

    public static Object createClass(JavaFile javaFile) throws URISyntaxException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
        ClassJavaFileManager classJavaFileManager = new ClassJavaFileManager(standardFileManager);
        ClassJavaFileManager.StringObject stringObject = new ClassJavaFileManager.StringObject(new URI(javaFile.typeSpec.name + ".java"), JavaFileObject.Kind.SOURCE, javaFile.toString());
        JavaCompiler.CompilationTask task = compiler.getTask(null, classJavaFileManager, null, null, null, Arrays.asList(stringObject));
        if (task.call()) {
            ClassJavaFileManager.ClassJavaFileObject javaFileObject = classJavaFileManager.getClassJavaFileObject();
            ClassLoader classLoader = new ClassJavaFileManager.MyClassLoader(javaFileObject);
            Object clazz = classLoader.loadClass(javaFile.typeSpec.name);
            return clazz;
        }
        return null;
    }

    /**
     * 自定义fileManager
     */
    private static class ClassJavaFileManager extends ForwardingJavaFileManager {

        private ClassJavaFileObject classJavaFileObject;

        public ClassJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public ClassJavaFileObject getClassJavaFileObject() {
            return classJavaFileObject;
        }

        //这个方法一定要自定义
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return (classJavaFileObject = new ClassJavaFileObject(className, kind));
        }

        /**
         * 存储源文件
         */
        static class StringObject extends SimpleJavaFileObject {

            private String content;

            public StringObject(URI uri, Kind kind, String content) {
                super(uri, kind);
                this.content = content;
            }

            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
                return this.content;
            }
        }

        /**
         * class文件（不需要存到文件中）
         */
        static class ClassJavaFileObject extends SimpleJavaFileObject {

            ByteArrayOutputStream outputStream;

            public ClassJavaFileObject(String className, Kind kind) {
                super(URI.create(className + kind.extension), kind);
                this.outputStream = new ByteArrayOutputStream();
            }

            //这个也要实现
            @Override
            public OutputStream openOutputStream() throws IOException {
                return this.outputStream;
            }

            public byte[] getBytes() {
                return this.outputStream.toByteArray();
            }
        }

        //自定义classloader
        static class MyClassLoader extends ClassLoader {
            private ClassJavaFileObject stringObject;

            public MyClassLoader(ClassJavaFileObject stringObject) {
                this.stringObject = stringObject;
            }

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                byte[] bytes = this.stringObject.getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
        }
    }
}

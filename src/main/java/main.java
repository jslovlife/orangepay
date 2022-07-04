import model.ThymeLeafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import util.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

class main {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        createModel();

        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/template/");
        resolver.setSuffix(".html");

        Object obj = new Object();

        var context = new Context();

        context.setVariable("firstName", "John");
        context.setVariable("lastName", "Lim");
        context.setVariable("level", "3");
        context.setVariable("class", "1J");

        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        var result = templateEngine.process("student", context);

        Writer writer = new FileWriter(Constant.filePath + "hello.java");
        writer.write(ThymeLeafConfig.getTemplateEngine().process("hello.java", context));
        writer.close();
        System.out.println(result);
    }

    static boolean createModel() throws FileNotFoundException, IOException {
        InputStream inputStream = null;
        File file = new File(Constant.schemaFilePath + "schema.txt");
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sbDeseliariser = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int index = 0;
            String className = null;
            while ((line = br.readLine()) != null) {

                if (index == 0) {
                    className = line.split(",")[0];
                    generateClass(sb, className);
                    generateDeserialiser(sbDeseliariser, className);
                } else {
                    String attribute = line.split(",")[0];
                    int deselialiserStart = parseInt(line.split(",")[1]) - 1;
                    int deselialiserEnd = parseInt(line.split(",")[2]) - 1;
                    generateObjAttribute(sb, line.split(",")[0], Constant.dataTypeStr);
                    generateDeseliariserAttribute(sbDeseliariser, attribute, deselialiserStart, deselialiserEnd);
                }

                index++;
            }

            //Generate last bracket to close object class
            generateClassClose(sb);
            generateDeserialiserClose(sbDeseliariser);

            //Generate java file for new class
            generateJavaClass(sb, className);
            generateJavaClass(sbDeseliariser, className + Constant.deserialiser);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private static void generateJavaClass(StringBuilder sb, String className) throws IOException {
        FileOutputStream fOS = new FileOutputStream(Constant.filePath + className + Constant.fileExtensionJava);
        fOS.write(sb.toString().getBytes());
    }

    private static void generateClass(StringBuilder sb, String className) {
        sb.append("package model;\n\n");
        sb.append("public class " + className + " {\n");
    }

    private static void generateClassClose(StringBuilder sb) {
        sb.append("}");
    }

    private static void generateDeserialiser(StringBuilder sb, String className) {
        sb.append("package model;\n\n");
        sb.append("public class " + className + "Deserialiser {\n");
        sb.append("\tpublic " + className + " parse(String lineFeed) {\n");
        sb.append("\t\t" + className + " record = new " + className + "();\n");
    }

    private static void generateDeserialiserClose(StringBuilder sb) {
        sb.append("\t\treturn record;\n\t}\n}");
    }

    private static void generateObjAttribute(StringBuilder sb, String attribute, String dataType) {
        sb.append("\tpublic " + dataType + " " + attribute + ";\n");
    }

    private static void generateDeseliariserAttribute(StringBuilder sb, String attribute, int start, int end) {
        sb.append("\t\trecord." + attribute + " = lineFeed.substring(" + start + "," + end + ").trim();\n");
    }

    /*private static List<StudentRecord> parseDummyDataFile() {
        List<StudentRecord> list = new ArrayList<>();
        File file = new File(Constant.schemaFilePath + "test.txt");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                StudentRecord studentRecord = new StudentRecord();
                studentRecord = StudentRecordDeserialiser.parse(line.toString());
                list.add(studentRecord);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }*/
}
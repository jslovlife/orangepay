import model.DeserialiserModel;
import util.ThymeLeafConfig;
import org.thymeleaf.context.Context;
import util.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

class main {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        createModel();
    }

    static boolean createModel() throws FileNotFoundException, IOException {
        InputStream inputStream = null;
        File file = new File(Constant.schemaFilePath + "schema.txt");
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sbAttribute = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int index = 0;
            String className = null;

            Context contextForClass = new Context();
            Context contextForDeserialiser = new Context();

            List<DeserialiserModel> deserialisers = new ArrayList<>();

            while ((line = br.readLine()) != null) {

                if (index == 0) {
                    className = line.split(",")[0];
                } else {
                    DeserialiserModel deserialiser = new DeserialiserModel();
                    String attribute = line.split(",")[0];
                    sbAttribute.append(attribute + ";");
                    deserialiser.setAttribute(line.split(",")[0]);
                    deserialiser.setFirstDigit(parseInt(line.split(",")[1]) - 1);
                    deserialiser.setLastDigit(parseInt(line.split(",")[2]) - 1);

                    deserialisers.add(deserialiser);

                }

                index++;
            }

            contextForClass.setVariable("class", className);
            contextForClass.setVariable("attributes", sbAttribute.toString().split(";"));

            contextForDeserialiser.setVariable("class", className);
            contextForDeserialiser.setVariable("deserialisers", deserialisers);

            String classTemplate = ThymeLeafConfig.getTemplateEngine().process("class-template", contextForClass);
            String deserialiserTemplate = ThymeLeafConfig.getTemplateEngine().process("deserialiser-template", contextForDeserialiser);

            generateJavaClass(classTemplate, className);
            generateJavaClass(deserialiserTemplate, className+Constant.deserialiser);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private static void generateJavaClass(String text, String className) throws IOException {
        FileOutputStream fOS = new FileOutputStream(Constant.filePath + className + Constant.fileExtensionJava);
        fOS.write(text.getBytes());
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
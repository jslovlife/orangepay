import model.DeserialiserModel;
import util.ThymeLeafConfig;
import org.thymeleaf.context.Context;
import util.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

import java.util.logging.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class main {
    //Creating the Logger object
    private static Logger logger = Logger.getLogger("main");

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

                String[] lineArr = line.split(",");

                validateLine(lineArr, line, index);

                if (index == 0) {
                    className = lineArr[0];
                } else {
                    DeserialiserModel deserialiser = new DeserialiserModel();

                    validateAttribute(lineArr[0]);

                    validationFirstAndLastDigit(lineArr[2], lineArr[3]);

                    sbAttribute.append(lineArr[0] + ";");
                    deserialiser.setAttribute(validateNull(Constant.ATTRIBUTE,lineArr[0]));
                    deserialiser.setDescription(validateNull(Constant.DESCRIPTION,lineArr[1]));

                    deserialiser.setFirstDigit(parseInt(validateNull(Constant.FIRSTDIGIT,lineArr[2])) - 1);
                    deserialiser.setLastDigit(parseInt(validateNull(Constant.LASTDIGIT,lineArr[3])) - 1);

                    deserialisers.add(deserialiser);

                }

                index++;
            }

            contextForClass.setVariable("class", className);
            //contextForClass.setVariable("attributes", sbAttribute.toString().split(";"));
            contextForClass.setVariable("deserialisers", deserialisers);

            contextForDeserialiser.setVariable("class", className);
            contextForDeserialiser.setVariable("deserialisers", deserialisers);

            String classTemplate = ThymeLeafConfig.getTemplateEngine().process("class-template", contextForClass);
            String deserialiserTemplate = ThymeLeafConfig.getTemplateEngine().process("deserialiser-template", contextForDeserialiser);

            generateJavaClass(classTemplate, className);
            generateJavaClass(deserialiserTemplate, className + Constant.deserialiser);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        return true;
    }

    private static void generateJavaClass(String text, String className) throws IOException {
        FileOutputStream fOS = new FileOutputStream(Constant.filePath + className + Constant.fileExtensionJava);
        fOS.write(text.getBytes());
    }

    private static void validateLine(String [] lineArr , String line, int index) throws Exception {

        if (index == 0) {
            if (lineArr.length != Constant.classLineLength) {
                throw new Exception("Class line contains extra or missing component: " + line);
            }
        } else{
            if (lineArr.length != Constant.attributeLineLength) {
                throw new Exception("Attribute line contains or missing extra component: " + line);
            }
        }
    }

    private static String validateNull(String attribute, String data) throws Exception {

        if (data.equals(null)){
            throw new Exception(" cannot null.");
        }

        return data;
    }

    private static void validateAttribute(String attribute) throws Exception {
        Pattern pattern = Pattern.compile("[^a-zA-Z]");
        Matcher matcher = pattern.matcher(attribute);

        if (matcher.find()) {
            throw new Exception("Error -> Attribute only can contain characters. File cannot process.");
        }

    }

    private static boolean validationFirstAndLastDigit(String firstDigit, String lastDigit) throws Exception {

        Pattern pattern = Pattern.compile("[0-9]+");
        boolean isFirstDigitValid = pattern.matcher(firstDigit).find();
        boolean isLastDigitValid = pattern.matcher(lastDigit).find();

        if (!isFirstDigitValid) {
            throw new Exception("First digit must be number, firstDigit = "+firstDigit);
        }

        if (!isLastDigitValid) {
            throw new Exception("Last digit must be number, lastDigit = "+lastDigit);
        }

        if (parseInt(firstDigit) > parseInt(lastDigit)) {
            throw new Exception("First digit must greater than last digit,  firstDigit = "+firstDigit+", lastDigit = "+lastDigit);
        }

        return false;
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
package model;

public class StudentRecordDeserialiser {
    public StudentRecord parse(String lineFeed) {
        StudentRecord record = new StudentRecord();
        
        record.firstName= lineFeed.substring(-1,19).trim();
        record.lastName= lineFeed.substring(20,39).trim();
        record.level= lineFeed.substring(40,44).trim();
        record.sublevel= lineFeed.substring(45,49).trim();
        record.studentClass= lineFeed.substring(50,59).trim();
        
        return record;
    }
}
package org.example.horse_management_system;

public class HorseDetails {

    String id;
    String name;
    String jockeyName;
    int age;
    String breed;
    String raceRecord;
    String imagePath;
    private int finishTime; // New field for storing finishing time


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJockeyName() {
        return jockeyName;
    }

    public int getAge() {
        return age;
    }

    public String getBreed() {
        return breed;
    }

    public String getRaceRecord() {
        return raceRecord;
    }

    public String getImagePath() {
        return imagePath;
    }

    public HorseDetails(String id, String name, String jockeyName, int age, String breed, String raceRecord, String imagePath) {
        this.id = id;
        this.name = name;
        this.jockeyName = jockeyName;
        this.age = age;
        this.breed = breed;
        this.raceRecord = raceRecord;
        this.imagePath = imagePath;
    }

    // Getter and setter for finishTime
    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }
}

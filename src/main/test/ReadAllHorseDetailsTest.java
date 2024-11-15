import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.example.horse_management_system.Horse.readAllHorseDetails;
import static org.junit.Assert.*;

public class ReadAllHorseDetailsTest {

    @Test
    public void testReadHorseDetails1() {
        // Create a sample test file with mock data
        String testFile = "test_horse_details.txt";
        String[] testData = {
                "Horse ID: A1",
                "Horse Name: Horse1",
                "Jockey Name: Jockey1",
                "Age: 5",
                "Breed: Breed1",
                "Race Record: Record1",
                "", // Empty line indicates the end of current horse details
                "Horse ID: B2",
                "Horse Name: Horse2",
                "Jockey Name: Jockey2",
                "Age: 6",
                "Breed: Breed2",
                "Race Record: Record2",
                "" // Empty line indicates the end of current horse details
        };

        // Write mock data to the test file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            for (String line : testData) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method to read horse details from the test file
        Map<String, Map<String, String>> horseDetailsMap = readAllHorseDetails("test_horse_details.txt");

        // Assert that the horseDetailsMap contains the expected entries
        assertEquals(2, horseDetailsMap.size()); // Check the size of the map
        assertTrue(horseDetailsMap.containsKey("A1")); // Check if the map contains horse with ID A1
        assertTrue(horseDetailsMap.containsKey("B2")); // Check if the map contains horse with ID B2

        // Verify details of horse with ID A1
        Map<String, String> horseA1Details = horseDetailsMap.get("A1");
        assertEquals("Horse1", horseA1Details.get("Horse Name"));
        assertEquals("Jockey1", horseA1Details.get("Jockey Name"));
        assertEquals("5", horseA1Details.get("Age"));
        assertEquals("Breed1", horseA1Details.get("Breed"));
        assertEquals("Record1", horseA1Details.get("Race Record"));

        // Verify details of horse with ID B2
        Map<String, String> horseB2Details = horseDetailsMap.get("B2");
        assertEquals("Horse2", horseB2Details.get("Horse Name"));
        assertEquals("Jockey2", horseB2Details.get("Jockey Name"));
        assertEquals("6", horseB2Details.get("Age"));
        assertEquals("Breed2", horseB2Details.get("Breed"));
        assertEquals("Record2", horseB2Details.get("Race Record"));

        // Clean up: delete the test file
        File testFileToDelete = new File(testFile);
        if (testFileToDelete.exists()) {
            testFileToDelete.delete();
        }
    }

    @Test
    public void testReadHorseDetails2() {
        // Prepare a test file with sample horse details
        prepareTestFile();

        // Call the method to read horse details from the test file
        Map<String, Map<String, String>> horseDetailsMap = readAllHorseDetails("test_horse_details.txt");

        // Verify the content of the horseDetailsMap
        assertNotNull("Horse details map should not be null", horseDetailsMap);
        //assertEquals("Horse details map should contain 2 entries", 2, horseDetailsMap.size());

        // Verify the details of the first horse
        Map<String, String> horse1Details = horseDetailsMap.get("A1");
        assertNotNull("Details of horse A1 should not be null", horse1Details);
        assertEquals("Horse name should be Horse1", "Horse1", horse1Details.get("Horse Name"));
        assertEquals("Jockey name should be Jockey1", "Jockey1", horse1Details.get("Jockey Name"));
        assertEquals("Age should be 5", "5", horse1Details.get("Age"));
        assertEquals("Breed should be Breed1", "Breed1", horse1Details.get("Breed"));
        assertEquals("Race record should be Record1", "Record1", horse1Details.get("Race Record"));
        assertEquals("Image path should be path1.jpg", "path1.jpg", horse1Details.get("Image Path"));

        // Verify the details of the second horse
        Map<String, String> horse2Details = horseDetailsMap.get("B2");
        assertNotNull("Details of horse B2 should not be null", horse2Details);
        assertEquals("Horse name should be Horse2", "Horse2", horse2Details.get("Horse Name"));
        assertEquals("Jockey name should be Jockey2", "Jockey2", horse2Details.get("Jockey Name"));
        assertEquals("Age should be 6", "6", horse2Details.get("Age"));
        assertEquals("Breed should be Breed2", "Breed2", horse2Details.get("Breed"));
        assertEquals("Race record should be Record2", "Record2", horse2Details.get("Race Record"));
        assertEquals("Image path should be path2.jpg", "path2.jpg", horse2Details.get("Image Path"));
    }

    // Helper method to prepare a test file with sample horse details
    private void prepareTestFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test_horse_details.txt"))) {
            // Write details of horse A1
            writer.write("Horse ID: A1\n");
            writer.write("Horse Name: Horse1\n");
            writer.write("Jockey Name: Jockey1\n");
            writer.write("Age: 5\n");
            writer.write("Breed: Breed1\n");
            writer.write("Race Record: Record1\n");
            writer.write("Image Path: path1.jpg\n");
            writer.write("\n"); // Add a blank line between horses

            // Write details of horse B2
            writer.write("Horse ID: B2\n");
            writer.write("Horse Name: Horse2\n");
            writer.write("Jockey Name: Jockey2\n");
            writer.write("Age: 6\n");
            writer.write("Breed: Breed2\n");
            writer.write("Race Record: Record2\n");
            writer.write("Image Path: path2.jpg\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


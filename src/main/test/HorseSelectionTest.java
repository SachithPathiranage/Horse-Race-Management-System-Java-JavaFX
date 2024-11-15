import org.example.horse_management_system.HorseDetails;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.example.horse_management_system.StartRace.readHorseDetailsFromFile;
import static org.example.horse_management_system.StartRace.selectRandomHorses;
import static org.junit.Assert.*;

public class HorseSelectionTest {
    private static final String TEST_FILE_PATH = "test_horse_details.txt";
    private static final int TEST_HORSE_COUNT = 4;
    private static final int TEST_RANDOM_COUNT = 4;

    private List<HorseDetails> testHorses;
    private List<HorseDetails> selectedHorses;

    @Before
    public void setUp() {
        // Prepare a test file with sample horse details
        prepareTestFile();

        // Read horse details from the test file
        testHorses = readHorseDetailsFromFile(TEST_FILE_PATH);
    }

    @Test
    public void testReadHorseDetailsFromFile() {
        assertNotNull("Test horses list should not be null", testHorses);
        assertEquals("Test horses list should contain 4 entries", TEST_HORSE_COUNT, testHorses.size());

        // Verify details of the first horse
        HorseDetails horse1 = testHorses.get(0);
        assertEquals("Horse ID of first horse should be A1", "A1", horse1.getId());
        assertEquals("Name of first horse should be Horse1", "Horse1", horse1.getName());
        assertEquals("Jockey name of first horse should be Jockey1", "Jockey1", horse1.getJockeyName());
        assertEquals("Age of first horse should be 5", 5, horse1.getAge());
        assertEquals("Breed of first horse should be Breed1", "Breed1", horse1.getBreed());
        assertEquals("Race record of first horse should be Record1", "Record1", horse1.getRaceRecord());
        assertEquals("Image path of first horse should be path1.jpg", "path1.jpg", horse1.getImagePath());
    }

    @Test
    public void testSelectRandomHorses() {
        // Select random horses from the test list
        selectedHorses = selectRandomHorses(testHorses, TEST_RANDOM_COUNT);

        assertNotNull("Selected horses list should not be null", selectedHorses);
        assertEquals("Selected horses list should contain 4 entries", TEST_RANDOM_COUNT, selectedHorses.size());
    }

    // Helper method to prepare a test file with sample horse details
    private void prepareTestFile() {
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("Horse ID: A1\n");
            writer.write("Horse Name: Horse1\n");
            writer.write("Jockey Name: Jockey1\n");
            writer.write("Age: 5\n");
            writer.write("Breed: Breed1\n");
            writer.write("Race Record: Record1\n");
            writer.write("Image Path: path1.jpg\n\n");

            writer.write("Horse ID: B2\n");
            writer.write("Horse Name: Horse2\n");
            writer.write("Jockey Name: Jockey2\n");
            writer.write("Age: 6\n");
            writer.write("Breed: Breed2\n");
            writer.write("Race Record: Record2\n");
            writer.write("Image Path: path2.jpg\n\n");

            writer.write("Horse ID: C3\n");
            writer.write("Horse Name: Horse3\n");
            writer.write("Jockey Name: Jockey3\n");
            writer.write("Age: 7\n");
            writer.write("Breed: Breed3\n");
            writer.write("Race Record: Record3\n");
            writer.write("Image Path: path3.jpg\n\n");

            writer.write("Horse ID: D4\n");
            writer.write("Horse Name: Horse4\n");
            writer.write("Jockey Name: Jockey4\n");
            writer.write("Age: 8\n");
            writer.write("Breed: Breed4\n");
            writer.write("Race Record: Record4\n");
            writer.write("Image Path: path4.jpg\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

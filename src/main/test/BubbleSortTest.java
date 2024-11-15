import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.horse_management_system.Horse;
import org.example.horse_management_system.ViewHorse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class BubbleSortTest {

    private ViewHorse viewHorse;

    @Before
    public void setUp() {
        viewHorse = new ViewHorse();
    }

    @Test
    public void testBubbleSort() {
        ObservableList<Horse> horseData = generateTestData();
        viewHorse.bubbleSort(horseData);
        List<String> sortedHorseIDs = extractHorseIDs(horseData);
        List<String> expectedSortedHorseIDs = List.of("A1", "A2", "B1", "B2", "C1", "D1");
        assertEquals("Bubble sort failed", expectedSortedHorseIDs, sortedHorseIDs);
    }

    private ObservableList<Horse> generateTestData() {
        ObservableList<Horse> horseData = FXCollections.observableArrayList();
        horseData.addAll(
                new Horse("C1", "C1 Horse", "C1 Jockey", "5", "C1 Breed", "C1 Race Record", "C1 Image"),
                new Horse("A2", "A2 Horse", "A2 Jockey", "3", "A2 Breed", "A2 Race Record", "A2 Image"),
                new Horse("B2", "B2 Horse", "B2 Jockey", "4", "B2 Breed", "B2 Race Record", "B2 Image"),
                new Horse("A1", "A1 Horse", "A1 Jockey", "2", "A1 Breed", "A1 Race Record", "A1 Image"),
                new Horse("B1", "B1 Horse", "B1 Jockey", "1", "B1 Breed", "B1 Race Record", "B1 Image"),
                new Horse("D1", "D1 Horse", "D1 Jockey", "6", "D1 Breed", "D1 Race Record", "D1 Image")
        );
        return horseData;
    }

    private List<String> extractHorseIDs(ObservableList<Horse> horseData) {
        List<String> horseIDs = new ArrayList<>();
        for (Horse horse : horseData) {
            horseIDs.add(horse.getHorseID());
        }
        return horseIDs;
    }
}

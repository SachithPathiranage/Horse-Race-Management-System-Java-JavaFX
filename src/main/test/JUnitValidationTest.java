import org.example.horse_management_system.AddHorse;
import org.example.horse_management_system.HorseDetails;
import org.example.horse_management_system.ValidationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JUnitValidationTest {

    @Test
    public void testIsValidName_ValidName() {
        assertTrue(ValidationUtils.isValidName("John Doe"));
        assertTrue(ValidationUtils.isValidName("Alice Smith"));
        assertTrue(ValidationUtils.isValidName("Jane"));
    }

    @Test
    public void testIsValidName_NullName() {
        assertFalse(ValidationUtils.isValidName(null));
    }

    @Test
    public void testIsValidName_EmptyName() {
        assertFalse(ValidationUtils.isValidName(""));
    }

    @Test
    public void testIsValidName_InvalidCharacters() {
        assertFalse(ValidationUtils.isValidName("John123"));
        assertFalse(ValidationUtils.isValidName("Jane@Doe"));
        assertFalse(ValidationUtils.isValidName("Alice."));
    }

    @Test
    public void testIsValidHorseId_ValidFormat() {
        AddHorse addHorse = new AddHorse();
        assertTrue(addHorse.isValidHorseId("A123"));
        assertTrue(addHorse.isValidHorseId("B456"));
        assertTrue(addHorse.isValidHorseId("C789"));
        assertTrue(addHorse.isValidHorseId("D012"));
    }

    @Test
    public void testIsValidHorseId_InvalidFormat() {
        AddHorse addHorse = new AddHorse();
        assertFalse(addHorse.isValidHorseId("123A"));
        assertFalse(addHorse.isValidHorseId("E123"));
        assertFalse(addHorse.isValidHorseId("A"));
        assertFalse(addHorse.isValidHorseId(""));
    }

    @Test
    public void testIsDuplicateHorseId_NotDuplicate() {
        AddHorse addHorse = new AddHorse();
        assertTrue(addHorse.isDuplicateHorseId("A1"));
    }

    @Test
    public void testIsDuplicateHorseId_nonExistingId() {
        assertFalse(new AddHorse().isDuplicateHorseId("Z999"));
    }

    @Test
    void constructorAndGetters_ValidArguments_ReturnsCorrectValues() {
        HorseDetails horse = new HorseDetails("H001", "Spirit", "Jack", 5, "Arabian", "5 wins", "/path/to/image.jpg");
        assertEquals("H001", horse.getId());
        assertEquals("Spirit", horse.getName());
        assertEquals("Jack", horse.getJockeyName());
        assertEquals(5, horse.getAge());
        assertEquals("Arabian", horse.getBreed());
        assertEquals("5 wins", horse.getRaceRecord());
        assertEquals("/path/to/image.jpg", horse.getImagePath());
    }
}

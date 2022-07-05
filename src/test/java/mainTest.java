import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class mainTest {

    @Test
    void main() throws IOException, FileNotFoundException {
        assertTrue(main.createModel());
        assert(true);
    }

    @Test
    void createModel() {
    }
}
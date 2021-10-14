import de.accso.library.datamanagement.model.Book;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void testToString() {
        List<String> authors = new ArrayList<>();
        authors.add("Peter");
        authors.add("Jane");

        Book buch = new Book(123, "titel", authors);

        assertThat(buch.toString()).isEqualTo(
                "Book[authors={Peter,Jane},id=123,instances={},title=titel]"
        );
    }
}
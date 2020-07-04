import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.BookFilter;
import static org.assertj.core.api.Assertions.assertThat;

public interface FilterBoundaryTests {
    BookFilter get();

    // Here the default method implements the complete test case.
    @Test
    @DisplayName("should not fail for null book.")
    default void nullTest(){
        assertThat(get().apply(null)).isFalse();
    }
}

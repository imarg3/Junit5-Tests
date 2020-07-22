import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;
import org.model.*;

import java.time.LocalDate;
import java.time.Month;

public class BookFilterSpec {
    private Book cleanCode;
    private Book codeComplete;

    @BeforeEach
    void init(){
        cleanCode = new Book("Clean Code", "Robert C. Martin", LocalDate.
                of(2008, Month.AUGUST, 1));
        codeComplete = new Book("Code Complete", "Steve McConnel", LocalDate.
                of(2004, Month.JUNE, 9));
    }

    @Nested
    @DisplayName("book published date")
    class BookPublishedFilterSpec{
        @Test
        @DisplayName("is after specified year")
        void validateBookPublishedDatePostAskedYear(){
            BookFilter bookFilter = BookPublishedYearFilter.After(2007);
            assertTrue(bookFilter.apply(cleanCode));
            assertFalse(bookFilter.apply(codeComplete));
        }
    }

    @Test
    @Disabled
    @DisplayName("Composite criteria does not invoke after first failure")
    void shouldNotInvokeAfterFirstFailure(){
        CompositeFilter compositeFilter = new CompositeFilter();

        BookFilter invokedMockedFilter = Mockito.mock(BookFilter.class);
        Mockito.when(invokedMockedFilter.apply(cleanCode)).thenReturn(false);
        compositeFilter.addFilter(invokedMockedFilter);

        BookFilter nonInvokedMockedFilter = Mockito.mock(BookFilter.class);
        Mockito.when(nonInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
        compositeFilter.addFilter(nonInvokedMockedFilter);

        assertFalse(compositeFilter.apply(cleanCode));
        Mockito.verify(invokedMockedFilter).apply(cleanCode);
        Mockito.verifyZeroInteractions(nonInvokedMockedFilter);
    }

    @Test
    @DisplayName("Composite criteria invokes all filters")
    void shouldInvokeAllFilters(){
        CompositeFilter compositeFilter = new CompositeFilter();
        BookFilter firstInvokedMockedFilter = Mockito.mock(BookFilter.class);
        Mockito.when(firstInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
        compositeFilter.addFilter(firstInvokedMockedFilter);

        BookFilter secondInvokedMockedFilter = Mockito.mock(BookFilter.class);
        Mockito.when(secondInvokedMockedFilter.apply(cleanCode)).thenReturn(true);
        compositeFilter.addFilter(secondInvokedMockedFilter);

        assertTrue(compositeFilter.apply(cleanCode));
        Mockito.verify(firstInvokedMockedFilter).apply(cleanCode);
        Mockito.verify(secondInvokedMockedFilter).apply(cleanCode);
    }
}

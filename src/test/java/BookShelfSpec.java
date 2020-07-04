import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.model.Book;
import org.model.BookShelf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A BookShelf :: Junit5 based Tests")
@ExtendWith(BooksParameterResolver.class)
public class BookShelfSpec {
    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    void init(Map<String, Book> books){
        shelf = new BookShelf();
        effectiveJava = books.get("Effective Java");
        codeComplete = books.get("Code Complete");
        mythicalManMonth = books.get("The Mythical Man-Month");
        cleanCode = books.get("Clean Code");
    }

    @Nested
    @DisplayName("is empty")
    class isEmpty{
        @DisplayName("when no book is added to it")
        @Test
        public void emptyBookShelfWhenNoBookAdded(){
            //BookShelf shelf = new BookShelf();
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty.");
        }

        @Test
        @DisplayName("when add is called without books")
        public void emptyBookShelfWhenAddIsCalledWithoutBooks(){
            //BookShelf shelf = new BookShelf();
            shelf.add();
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty.");
        }
    }

    @Nested
    @DisplayName("search")
    class BookShelfSearchSpec{
        @BeforeEach
        void setup(){
            shelf.add(codeComplete, effectiveJava, mythicalManMonth, cleanCode);
        }

        @Test
        @DisplayName("should find books with title containing text")
        void shouldFindBooksWithTitleContainingText(){
            List<Book> books = shelf.findBooksByTitle("code");
            assertThat(books.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("after adding books")
    class BooksAreAdded{
        @Test
        @DisplayName("Check whether Book Shelf contains two books")
        public void bookShelfContainsTwoBooksWhenTwoBooksAdded(){
            //BookShelf shelf = new BookShelf();
            //shelf.add("Effective Java");
            //shelf.add("Code Complete");
            //shelf.add("Effective Java", "Code Complete");
            shelf.add(effectiveJava, codeComplete);

            List<Book> books = shelf.books();
            assertEquals(2, books.size(), () -> "BookShelf should have two books.");
        }

        @Test
        public void booksReturnedFromBookShelfIsImmutableForClient(){
            //BookShelf shelf = new BookShelf();
            //shelf.add("Effective Java", "Code Complete");
            shelf.add(effectiveJava, codeComplete);
            List<Book> books = shelf.books();
            try{
                books.add(mythicalManMonth);
                fail(() -> "Should not be able to add book to books.");
            }catch (Exception e){
                assertTrue(e instanceof UnsupportedOperationException, () ->
                        "Should throw UnsupportedOperationException.");
            }
        }
    }

    @Nested
    @DisplayName("is arranged")
    class ArrangeBooks{
        @Test
        public void bookShelfArrangedByBookTitle(){
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange();
            assertEquals(asList(codeComplete, effectiveJava, mythicalManMonth),
                    books, () -> "Books in a bookshelf should be arranged lexicographically by book title");
        }

        @Test
        void booksInBookShelfAreInInsertionOrderAfterCallingArrange(){
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            shelf.arrange();
            List<Book> books = shelf.books();
            assertEquals(asList(effectiveJava, codeComplete, mythicalManMonth), books,
                    () -> "Books in bookshelf are in insertion order");
        }

        @Test
        @DisplayName("bookshelf is arranged by user provided criteria (by book title lexicographically)")
        void bookShelfArrangedByUserProvidedCriteria(){
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            //List<Book> books = shelf.arrange(Comparator.<Book>naturalOrder().reversed());
            Comparator<Book> reversed = Comparator.<Book>naturalOrder().reversed();
            List<Book> books = shelf.arrange(reversed);
        /*assertEquals(
                asList(mythicalManMonth, effectiveJava, codeComplete),
                books,
                () -> "Books in a bookshelf are arranged in descending order of book title");

         */
            assertThat(books).isSortedAccordingTo(reversed);
        }

        @Test
        @DisplayName("books inside bookshelf are grouped by publication year")
        void groupBooksInsideBookShelfByPublicationYear(){
            shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);

            Map<Year, List<Book>> booksByPublicationYear = shelf.groupByPublicationYear();

            assertThat(booksByPublicationYear)
                    .containsKey(Year.of(2008))
                    .containsValues(Arrays.asList(effectiveJava, cleanCode));

            assertThat(booksByPublicationYear)
                    .containsKey(Year.of(2004))
                    .containsValues(Collections.singletonList(codeComplete));

            assertThat(booksByPublicationYear)
                    .containsKey(Year.of(1975))
                    .containsValues(Collections.singletonList(mythicalManMonth));
        }

        @Test
        @DisplayName("books inside bookshelf are grouped according to user provided criteria (group by author name)")
        void groupBooksByUserProvidedCriteria(TestInfo testInfo){
            System.out.println("Display Name :: " + testInfo.getDisplayName());
            shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
            Map<String, List<Book>> booksByAuthor = shelf.groupBy(Book::getAuthor);

            assertThat(booksByAuthor)
                    .containsKey("Joshua Bloch")
                    .containsValues(Collections.singletonList(effectiveJava));

            assertThat(booksByAuthor)
                    .containsKey("Steve McConnel")
                    .containsValues(Collections.singletonList(codeComplete));

            assertThat(booksByAuthor)
                    .containsKey("Frederick Phillips Brooks")
                    .containsValues(Collections.singletonList(mythicalManMonth));

            assertThat(booksByAuthor)
                    .containsKey("Robert C. Martin")
                    .containsValues(Collections.singletonList(cleanCode));
        }
    }
}

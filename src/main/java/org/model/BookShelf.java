package org.model;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BookShelf {

    private List<Book> books = new ArrayList<>();

    public List<Book> books(){
        return Collections.unmodifiableList(books);
    }

    public void add(Book... booksToAdd){
        //books.add(bookToAdd);
        //Arrays.stream(booksToAdd).forEach(book -> books.add(book));
        Arrays.stream(booksToAdd).forEach(books::add);
    }

    public List<Book> arrange(){
        //books.sort(Comparator.naturalOrder());
        //return books;
        return books.stream().sorted().collect(Collectors.toList());
    }

    public List<Book> arrange(Comparator<Book> criteria){
        return books.stream().sorted(criteria).collect(Collectors.toList());
    }

    public Map<Year, List<Book>> groupByPublicationYear(){
        return books.stream()
                .collect(Collectors.groupingBy(book -> Year.of(book.getPublishedOn().getYear())));
    }

    public <K> Map<K, List<Book>> groupBy(Function<Book, K> fx){
        return books.stream()
                .collect(Collectors.groupingBy(fx));
    }

    public Progress progress(){
        int booksRead = Long.valueOf(books.stream().filter(Book::isRead).count()).intValue();
        int booksToRead = books.size() - booksRead;
        int percentageCompleted = booksRead * 100 / books.size();
        int percentageToRead = booksToRead * 100 / books.size();

        return new Progress(percentageCompleted, percentageToRead, 0);
    }

    public List<Book> findBooksByTitle(String title){
        return findBooksByTitle(title, book -> true);
    }

    public List<Book> findBooksByTitle(String title, BookFilter filter){
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title))
                .filter(book -> filter.apply(book))
                .collect(Collectors.toList());
    }
}

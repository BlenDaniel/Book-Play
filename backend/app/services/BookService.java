

package services;

import com.google.inject.ImplementedBy;
import java.util.List;
import models.dto.BookDto;
import models.request.BookCreateRequest;
import models.request.BookUpdateRequest;

@ImplementedBy(BookServiceImpl.class)
public interface BookService {

    BookDto create(BookCreateRequest request);
    
    BookDto getOne(String id);
    
    List<BookDto> getAll();
    
    BookDto update(BookUpdateRequest request);
    
    void delete(String id);
    
    List<BookDto> search(String query);
}
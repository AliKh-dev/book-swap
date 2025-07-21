package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.book.request.*;
import com.alikh.bookswap.dto.book.response.*;

import java.util.List;

public interface BookService {

    BookSummaryResponse create(BookCreateRequest dto, Long ownerId);

    BookDetailResponse get(Long id);

    List<BookSummaryResponse> list();

    void update(Long id, BookUpdateRequest dto);

    void patch(Long id, BookPatchRequest dto);

    void delete(Long id);
}

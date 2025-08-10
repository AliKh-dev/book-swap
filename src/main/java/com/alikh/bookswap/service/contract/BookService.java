package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.book.request.*;
import com.alikh.bookswap.dto.book.response.*;

import java.util.List;

public interface BookService {

    BookSummaryResponse create(BookCreateRequest dto, Long ownerId);

    List<BookSummaryResponse> list();

    List<BookSummaryResponse> listMine(Long ownerId);

    BookDetailResponse get(Long id);

    BookSummaryResponse update(Long id, BookUpdateRequest dto, Long currentUserId);

    BookSummaryResponse patch(Long id, BookPatchRequest dto, Long currentUserId);

    void softDelete(Long id, Long currentUserId);

    void hardDelete(Long id, Long currentUserId);
}

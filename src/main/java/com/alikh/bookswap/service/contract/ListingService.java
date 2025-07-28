package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.listing.request.*;
import com.alikh.bookswap.dto.listing.response.*;

import java.util.List;

public interface ListingService {

    ListingSummaryResponse create(ListingCreateRequest dto, Long bookOwnerId);

    List<ListingSummaryResponse> list();

    List<ListingSummaryResponse> list(Long bookOwnerId);

    ListingDetailResponse get(Long id);

    ListingSummaryResponse update(Long id, ListingUpdateRequest dto, Long currentUserId);

    ListingSummaryResponse patch(Long id, ListingPatchRequest dto, Long currentUserId);

    void softDelete(Long id, Long currentUserId);

    void hardDelete(Long id, Long currentUserId);
}

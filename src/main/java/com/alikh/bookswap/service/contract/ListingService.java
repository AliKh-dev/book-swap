package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.listing.request.*;
import com.alikh.bookswap.dto.listing.response.*;

import java.util.List;

public interface ListingService {

    ListingSummaryResponse create(ListingCreateRequest dto);

    ListingDetailResponse get(Long id);

    List<ListingSummaryResponse> list();

    void update(Long id, ListingUpdateRequest dto);

    void patch(Long id, ListingPatchRequest dto);

    void delete(Long id);
}

package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.listingtype.request.*;
import com.alikh.bookswap.dto.listingtype.response.*;

import java.util.List;

public interface ListingTypeService {
    ListingTypeSummaryResponse create(ListingTypeCreateRequest dto);

    List<ListingTypeSummaryResponse> list();

    ListingTypeDetailResponse get(Integer id);

    ListingTypeSummaryResponse update(Integer id, ListingTypeUpdateRequest dto);

    ListingTypeSummaryResponse patch(Integer id, ListingTypePatchRequest dto);

    void delete(Integer id);
}

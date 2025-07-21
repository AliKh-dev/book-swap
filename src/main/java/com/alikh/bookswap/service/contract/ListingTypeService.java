package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.listingtype.request.*;
import com.alikh.bookswap.dto.listingtype.response.*;

import java.util.List;

public interface ListingTypeService {
    ListingTypeSummaryResponse create(ListingTypeCreateRequest dto);

    ListingTypeDetailResponse get(Integer id);

    void update(Integer id, ListingTypeUpdateRequest dto);

    void patch(Integer id, ListingTypePatchRequest dto);

    void delete(Integer id);

    List<ListingTypeSummaryResponse> list();
}

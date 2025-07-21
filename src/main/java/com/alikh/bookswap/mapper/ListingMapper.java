package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.listing.request.*;
import com.alikh.bookswap.dto.listing.response.*;
import com.alikh.bookswap.entity.Book;
import com.alikh.bookswap.entity.Listing;
import com.alikh.bookswap.entity.ListingType;
import org.springframework.stereotype.Component;

@Component
public class ListingMapper {

    /* ---------- Entity ➜ DTO ---------- */

    public ListingSummaryResponse toSummary(Listing listing) {
        return new ListingSummaryResponse(
                listing.getId(),
                listing.getBook() != null ? listing.getBook().getId() : null,
                listing.getType() != null ? listing.getType().getId() : null,
                listing.getPrice(),
                listing.getIsActive()
        );
    }

    public ListingDetailResponse toDetail(Listing listing) {
        return new ListingDetailResponse(
                listing.getId(),
                listing.getBook() != null ? listing.getBook().getId() : null,
                listing.getType() != null ? listing.getType().getId() : null,
                listing.getPrice(),
                listing.getRentalDays(),
                listing.getIsActive(),
                listing.getActiveBookId() != null ? listing.getActiveBookId() : null,
                listing.getCreatedAt()
        );
    }

    /* ---------- Create / Update requests ➜ Entity ---------- */

    public Listing fromCreate(ListingCreateRequest request,
                              Long listingId,
                              Book book,
                              ListingType type) {

        return Listing.builder()
                .id(listingId)
                .book(book)
                .type(type)
                .price(request.price())
                .rentalDays(request.rentalDays())
                .isActive(Boolean.TRUE)          // new listings start active
                .build();
    }

    public void applyUpdate(Listing listing,
                            ListingUpdateRequest request,
                            ListingType type) {

        listing.setType(type);
        listing.setPrice(request.price());
        listing.setRentalDays(request.rentalDays());
        listing.setIsActive(request.isActive());
    }

    public void applyPatch(Listing listing,
                           ListingPatchRequest request,
                           ListingType type) {

        if (request.typeId() != null)   listing.setType(type);
        if (request.price() != null)    listing.setPrice(request.price());
        if (request.rentalDays() != null) listing.setRentalDays(request.rentalDays());
        if (request.isActive() != null) listing.setIsActive(request.isActive());
    }
}

package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.listing.request.*;
import com.alikh.bookswap.dto.listing.response.*;
import com.alikh.bookswap.entity.*;
import com.alikh.bookswap.exception.AccessDeniedException;
import com.alikh.bookswap.exception.DuplicateActiveListingException;
import com.alikh.bookswap.exception.InvalidListingException;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.ListingMapper;
import com.alikh.bookswap.repository.*;

import com.alikh.bookswap.service.contract.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ListingServiceImpl implements ListingService {

    private final ListingRepository repo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final ListingTypeRepository typeRepo;
    private final ListingMapper mapper;

    @Override
    public ListingSummaryResponse create(ListingCreateRequest dto, Long bookId, Long currentUserId) {
        var book = fetchBookOrThrow(bookId);
        checkBookOwnerOrThrow(currentUserId, book);

        if (repo.existsByBookIdAndIsActiveTrue(book.getId())) {
            throw new DuplicateActiveListingException(book.getId());
        }

        var type = fetchTypeOrThrow(dto.typeId());

        if ("LEND".equals(type.getCode()) && dto.rentalDays() == null) {
            throw new InvalidListingException("rentalDays is required for LEND listings");
        }

        if (dto.price() != null && dto.price().signum() < 0) {
            throw new InvalidListingException("price cannot be negative");
        }

        var entity = mapper.fromCreate(dto, book, type);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingSummaryResponse> listMine(Long currentUserId) {
        return repo.findByBookOwnerIdAndIsDeletedFalse(currentUserId).stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public List<ListingSummaryResponse> listByBook(Long bookId) {
        return repo.findByBookIdAndIsDeletedFalse(bookId)
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingSummaryResponse> list() {
        return repo.findByIsDeletedFalse().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ListingDetailResponse get(Long id) {
        return mapper.toDetail(fetchActiveListing(id));
    }

    @Override
    public ListingSummaryResponse update(Long id, ListingUpdateRequest dto, Long currentUserId) {
        var entity = fetchActiveListing(id);
        checkBookOwnerOrThrow(currentUserId, entity.getBook());

        ListingType type = fetchTypeOrThrow(dto.typeId());

        mapper.applyUpdate(entity, dto, type);
        return mapper.toSummary(entity);
    }

    @Override
    public ListingSummaryResponse patch(Long id, ListingPatchRequest dto, Long currentUserId) {
        var entity = fetchActiveListing(id);
        checkBookOwnerOrThrow(currentUserId, entity.getBook());

        ListingType type = null;
        if (dto.typeId() != null)
            type = fetchTypeOrThrow(dto.typeId());

        mapper.applyPatch(entity, dto, type);
        return mapper.toSummary(entity);
    }

    @Override
    public void softDelete(Long id, Long currentUserId) {
        var entity = fetchActiveListing(id);
        checkBookOwnerOrThrow(currentUserId, entity.getBook());

        entity.setDeletedBy(fetchUserOrThrow(currentUserId).getEmail());
        entity.setDeletedAt(LocalDateTime.now());
        entity.setIsDeleted(true);
        entity.setIsActive(false);
    }

    @Override
    public void hardDelete(Long id, Long currentUserId) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Listing", id));
        checkDeletePermission(entity.getBook().getOwner().getId(), currentUserId);
        repo.deleteById(id);
    }

    private Book fetchBookOrThrow(Long bookId) {
        return bookRepo.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book", bookId));
    }

    private ListingType fetchTypeOrThrow(Integer typeId) {
        return typeRepo.findById(typeId)
                .orElseThrow(() -> new NotFoundException("ListingType", typeId));
    }

    private Listing fetchActiveListing(Long id) {
        return repo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Listing", id));
    }

    private AppUser fetchUserOrThrow(Long ownerId) {
        return userRepo.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User", ownerId));
    }

    private void checkBookOwnerOrThrow(Long currentUserId, Book book) {
        if (!book.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("This book does not belong to you.");
        }
    }

    private void checkDeletePermission(Long ownerId, Long currentUserId) {
        boolean isAdmin = "ADMIN".equals(fetchUserOrThrow(currentUserId).getRole().getCode());
        if (!isAdmin && !ownerId.equals(currentUserId)) {
            throw new AccessDeniedException("You don't have enough authority to delete this listing");
        }
    }
}

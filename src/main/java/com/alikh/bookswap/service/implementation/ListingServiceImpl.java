package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.listing.request.*;
import com.alikh.bookswap.dto.listing.response.*;
import com.alikh.bookswap.entity.*;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.ListingMapper;
import com.alikh.bookswap.repository.*;

import com.alikh.bookswap.service.contract.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ListingServiceImpl implements ListingService {

    private final ListingRepository repo;
    private final BookRepository bookRepo;
    private final ListingTypeRepository typeRepo;
    private final ListingMapper mapper;

    @Override
    public ListingSummaryResponse create(ListingCreateRequest dto) {

        Book book = bookRepo.findById(dto.bookId())
                .orElseThrow(() -> new NotFoundException("Book", dto.bookId()));

        ListingType type = typeRepo.findById(dto.typeId())
                .orElseThrow(() -> new NotFoundException("ListingType", dto.typeId()));

        Listing entity = mapper.fromCreate(dto, book, type);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ListingDetailResponse get(Long id) {
        Listing listing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Listing", id));
        return mapper.toDetail(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public void update(Long id, ListingUpdateRequest dto) {

        Listing listing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Listing", id));

        ListingType type = typeRepo.findById(dto.typeId())
                .orElseThrow(() -> new NotFoundException("ListingType", dto.typeId()));

        mapper.applyUpdate(listing, dto, type);   // uses dirty-checking
    }

    @Override
    public void patch(Long id, ListingPatchRequest dto) {

        Listing listing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Listing", id));

        ListingType type = listing.getType();
        if (dto.typeId() != null) {
            type = typeRepo.findById(dto.typeId())
                    .orElseThrow(() -> new NotFoundException("ListingType", dto.typeId()));
        }

        mapper.applyPatch(listing, dto, type);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new NotFoundException("Listing", id);
        repo.deleteById(id);
    }
}

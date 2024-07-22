package com.example.market.service;

import com.example.market.domain.Item;
import com.example.market.domain.ItemImage;
import com.example.market.domain.UserAccount;
import com.example.market.domain.type.ItemSearchType;
import com.example.market.dto.ItemDto;
import com.example.market.repository.ItemImageRepository;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserAccountRepository userAccountRepository;
    private final FileService fileService;
    private final ItemImageRepository itemImageRepository;


    // 상세보기 + 수정 get
    @Transactional(readOnly = true)
    public ItemDto getItem(Long itemId){
        return itemRepository.findById(itemId)
                .map(ItemDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - itemId: " + itemId));
    }

    // 수정 - post
    public void updateItem(Long itemId, ItemDto dto) throws IOException {
        try {
            Item item = itemRepository.getReferenceById(itemId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            if (item.getUserAccount().equals(userAccount)) {
                if (dto.itemName() != null) { item.setItemName(dto.itemName()); }
                if (dto.price() != null) { item.setPrice(dto.price()); }
                if (dto.stockNumber() != null) { item.setStockNumber(dto.stockNumber()); }
                if (dto.itemDetail() != null) { item.setItemDetail(dto.itemDetail()); }
                if (dto.hashtag() != null) { item.setHashtag(dto.hashtag()); }

                // 첨부파일 처리
                if (dto.imageFiles() != null && !dto.imageFiles().isEmpty()) {
                    // 기존 파일 삭제
                    List<ItemImage> existingFiles = itemImageRepository.findByItemId(itemId);
                    for (ItemImage existingFile : existingFiles) {
                        fileService.deleteFile(existingFile.getSavePath());
                    }
                    itemImageRepository.deleteAll(existingFiles);

                    // 새 파일 저장
                    List<ItemImage> itemImages = storeFiles(dto.imageFiles(), item);
                    itemImageRepository.saveAll(itemImages);
                }

            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }



    // 조회
    @Transactional(readOnly = true)
    public Page<ItemDto> searchItems(ItemSearchType searchType, String searchKeyword, Pageable pageable) {

        if (searchKeyword == null || searchKeyword.isBlank()) {
            return itemRepository.findAll(pageable).map(ItemDto::from);
        }
        return switch (searchType) {
            case ITEMNAME -> itemRepository.findByItemNameContaining(searchKeyword, pageable).map(ItemDto::from);
            case HASHTAG -> itemRepository.findByHashtag("#" + searchKeyword, pageable).map(ItemDto::from);
        };
    }


    // 저장
    public void saveItem(ItemDto dto) throws IOException {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

        Item item = dto.toEntity(userAccount);
        itemRepository.save(item);

        List<ItemImage> itemImages = storeFiles(dto.imageFiles(), item);
        itemImageRepository.saveAll(itemImages);

    }

    // 삭제
    public void deleteItem(Long itemId, String userId) {
        itemRepository.deleteByIdAndUserAccount_UserId(itemId, userId);
    }

    // 이미지 추가
    public List<ItemImage> storeFiles(List<MultipartFile> multipartFiles, Item item) throws IOException {
        List<ItemImage> itemImages = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                String storedFileName = fileService.storeFile(multipartFile);
                String originalFilename = multipartFile.getOriginalFilename();
                String fullPath = fileService.getFullPath(storedFileName);

                ItemImage itemImage = ItemImage.of(originalFilename, storedFileName, fullPath, item);
                itemImages.add(itemImage);
            }
        }
        return itemImages;
    }

    // 해시태그
    public List<String> getHashtags() {
        return itemRepository.findAllDistinctHashtags();
    }

    public long getItemCount() {
        return itemRepository.count();
    }


    // 해시태그 통해서 검색
    @Transactional(readOnly = true)
    public Page<ItemDto> searchItemViaHashtag(String hashtag, Pageable pageable){
        if(hashtag == null || hashtag.isBlank()){
            return Page.empty(pageable);
        }
        return itemRepository.findByHashtag(hashtag, pageable).map(ItemDto::from);
    }

    // 아이템 전체 조회 - ADMIN
    @Transactional(readOnly = true)
    public List<ItemDto> itemFindAll() {
        return itemRepository.findAll().stream().map(ItemDto::from).toList();
    }

}

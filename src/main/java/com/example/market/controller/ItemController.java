package com.example.market.controller;

import com.example.market.domain.type.ItemSearchType;
import com.example.market.domain.type.SearchType;
import com.example.market.dto.request.ItemRequest;
import com.example.market.dto.response.ItemResponse;
import com.example.market.dto.security.BoardPrincipal;
import com.example.market.service.ItemService;
import com.example.market.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/item")
@Controller
public class ItemController {

    private final ItemService itemService;
    private final PaginationService paginationService;

    // 목록 - 조회
    @GetMapping
    public String items(
            @RequestParam(required = false) ItemSearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<ItemResponse> item = itemService.searchItems(searchType, searchValue, pageable).map(ItemResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), item.getTotalPages());

        model.addAttribute("item", item);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchTypes", ItemSearchType.values());

        return "item/index";
    }

    // 해시태그 검색
    @GetMapping("/search-hashtag")
    public String searchArticleHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<ItemResponse> item = itemService.searchItemViaHashtag(searchValue, pageable).map(ItemResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), item.getTotalPages());
        List<String> hashtags = itemService.getHashtags();

        model.addAttribute("item", item);
        model.addAttribute("hashtags", hashtags);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchType", SearchType.HASHTAG);

        return "item/search-item-hashtag";
    }


    // 상세보기
    @GetMapping("/{itemId}")
    public String article(@PathVariable Long itemId, Model model) {
        ItemResponse item = ItemResponse.from(itemService.getItem(itemId));

        model.addAttribute("item", item);
        model.addAttribute("totalCount", itemService.getItemCount());        // 페이지 갯수
        return "item/detail";
    }

    // 수정
    @GetMapping("/{itemId}/update")
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        ItemResponse item = ItemResponse.from(itemService.getItem(itemId));
        model.addAttribute("item", item);

        return "item/update";
    }

    @PostMapping ("/{itemId}/update")
    public String updateItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            @Validated ItemRequest itemRequest, BindingResult bindingResult, Model model
    ) throws IOException {
        if(bindingResult.hasErrors()){
            return "item/form";
        }
        try{
            itemService.updateItem(itemId, itemRequest.toDto(boardPrincipal.toDto()));
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "item/form";
        }
        return "redirect:/item/" + itemId;
    }


    // 저장
    @GetMapping("/form")
    public String itemForm(Model model) {
        model.addAttribute("itemRequest", ItemRequest.of());
        return "item/form";
    }

    @PostMapping ("/form")
    public String postNewItem(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            @Validated ItemRequest itemRequest, BindingResult bindingResult, Model model
    ) throws IOException {
        if(bindingResult.hasErrors()){
            return "item/form";
        }
        try{
            itemService.saveItem(itemRequest.toDto(boardPrincipal.toDto()));
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "item/form";
        }
        return "redirect:/item";
    }


    // 삭제
    @PostMapping("/{itemId}/delete")
    public String deleteArticle(
            @PathVariable Long itemId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        itemService.deleteItem(itemId, boardPrincipal.getUsername());

        return "redirect:/item";
    }
}

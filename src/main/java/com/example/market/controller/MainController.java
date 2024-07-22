package com.example.market.controller;

import com.example.market.domain.type.ItemSearchType;
import com.example.market.dto.response.ItemResponse;
import com.example.market.service.ItemService;
import com.example.market.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ItemService itemService;
    private final PaginationService paginationService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) ItemSearchType searchType,
                       @RequestParam(required = false) String searchValue,
                       @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model
    ) {
        Page<ItemResponse> item = itemService.searchItems(searchType, searchValue, pageable).map(ItemResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), item.getTotalPages());

        model.addAttribute("item", item);
        model.addAttribute("paginationBarNumbers", barNumbers);
//        model.addAttribute("searchTypes", ItemSearchType.values());
//        return "forward:/";
        return "index";
    }

}

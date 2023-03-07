package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.MenuRequest;
import com.programmingtechie.productservice.dto.MenuResponse;
import com.programmingtechie.productservice.dto.MenusResponse;
import com.programmingtechie.productservice.model.Menu;
import com.programmingtechie.productservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuResponse createMenu(MenuRequest menuRequest) {
        MenuResponse result = null;

        Menu menu = Menu.builder()
                .name(menuRequest.getName())
                .description(menuRequest.getDescription())
                .price(menuRequest.getPrice())
                .build();

        menu = menuRepository.save(menu);
        if(menu != null) {
            result = MenuResponse.builder()
                    .code(200)
                    .message("Menu created")
                    .data(menu)
                    .build();
        }

        return result;
    }

    public MenusResponse getAllMenus() {
        MenusResponse result = null;

        List<Menu> menus = menuRepository.findAll();

        menus = menus.stream().map(this::mapToMenuResponse).toList();
        result = MenusResponse.builder()
                .code(200)
                .message("Menu retrieved")
                .data(menus)
                .build();

        return result;
    }

    private Menu mapToMenuResponse(Menu menu) {
        return Menu.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .build();
    }

    public MenuResponse updateMenu(@PathVariable String id, @RequestBody MenuRequest menuRequest) {
        MenuResponse result = null;

        Optional<Menu> menuRepo = menuRepository.findById(id);
        if(menuRepo.isEmpty()) {
            return result;
        }

        Menu menu = Menu.builder()
                .id(menuRepo.get().getId())
                .name(menuRequest.getName())
                .description(menuRequest.getDescription())
                .price(menuRequest.getPrice())
                .build();

        menu = menuRepository.save(menu);
        if(menu != null) {
            result = MenuResponse.builder()
                    .code(200)
                    .message("Menu updated")
                    .data(menu)
                    .build();
        }

        return result;
    }

    public MenuResponse getMenu(String id) {
        MenuResponse result = null;

        Optional<Menu> menu = menuRepository.findById(id);
        result = MenuResponse.builder()
                .code(200)
                .message("Menu with id " + id + " retrieved")
                .data(menu.get())
                .build();

        return result;
    }

    public MenuResponse deleteMenu(String id) {
        try {
            Optional<Menu> menu = menuRepository.findById(id);
            menuRepository.deleteById(id);
            return MenuResponse.builder()
                    .code(200)
                    .message("Menu with id " + id + " deleted")
                    .data(menu.get())
                    .build();
        } catch (Exception e) {
            return MenuResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }
}

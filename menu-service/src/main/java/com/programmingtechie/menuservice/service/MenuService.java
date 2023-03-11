package com.programmingtechie.menuservice.service;

import com.programmingtechie.menuservice.dto.MenuRequest;
import com.programmingtechie.menuservice.dto.MenuResponse;
import com.programmingtechie.menuservice.dto.MenusResponse;
import com.programmingtechie.menuservice.model.Menu;
import com.programmingtechie.menuservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuResponse createMenu(MenuRequest menuRequest) {
        try {
            Menu menu = Menu.builder()
                    .name(menuRequest.getName())
                    .description(menuRequest.getDescription())
                    .price(menuRequest.getPrice())
                    .build();

            menu = menuRepository.save(menu);
            return MenuResponse.builder()
                    .code(200)
                    .message("Menu created")
                    .data(menu)
                    .build();
        } catch (DataAccessException e) {
            return MenuResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return MenuResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public MenusResponse getAllMenus() {
        try {
            List<Menu> menus = menuRepository.findAll();

            menus = menus.stream().map(this::mapToMenuResponse).toList();
            return MenusResponse.builder()
                    .code(200)
                    .message("Menus retrieved")
                    .data(menus)
                    .build();
        } catch (DataAccessException e) {
            return MenusResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return MenusResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
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
        try {
            Optional<Menu> menuRepo = menuRepository.findById(id);
            if(menuRepo.isEmpty()) {
                return null;
            }

            Menu menu = Menu.builder()
                    .id(menuRepo.get().getId())
                    .name(menuRequest.getName())
                    .description(menuRequest.getDescription())
                    .price(menuRequest.getPrice())
                    .build();

            menu = menuRepository.save(menu);
            if(menu != null) {
                return MenuResponse.builder()
                        .code(200)
                        .message("Menu updated")
                        .data(menu)
                        .build();
            } else {
                return MenuResponse.builder()
                        .code(500)
                        .message("Failed to update menu")
                        .build();
            }
        } catch (DataAccessException e) {
            return MenuResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return MenuResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public MenuResponse getMenu(String id) {
        try {
            Optional<Menu> menu = menuRepository.findById(id);
            return MenuResponse.builder()
                    .code(200)
                    .message("Menu with id " + id + " retrieved")
                    .data(menu.get())
                    .build();
        } catch (NoSuchElementException e) {
            return MenuResponse.builder()
                    .code(404)
                    .message("Menu with id " + id + " not found")
                    .build();
        } catch (Exception e) {
            return MenuResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    public MenuResponse deleteMenu(String id) {
        try {
            Optional<Menu> menu = menuRepository.findById(id);
            if (!menu.isPresent()) {
                return MenuResponse.builder()
                        .code(404)
                        .message("Menu with id " + id + " not found")
                        .build();
            }
            menuRepository.deleteById(id);
            return MenuResponse.builder()
                    .code(200)
                    .message("Menu with id " + id + " deleted")
                    .data(menu.get())
                    .build();
        } catch (DataAccessException e) {
            return MenuResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return MenuResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }
}

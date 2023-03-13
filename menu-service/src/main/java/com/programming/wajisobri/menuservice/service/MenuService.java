package com.programming.wajisobri.menuservice.service;

import com.programming.wajisobri.menuservice.model.Menu;
import com.programming.wajisobri.menuservice.dto.MenuRequest;
import com.programming.wajisobri.menuservice.dto.MenuResponse;
import com.programming.wajisobri.menuservice.dto.MenusResponse;
import com.programming.wajisobri.menuservice.model.Ingredient;
import com.programming.wajisobri.menuservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public MenuResponse updateMenu(String id, MenuRequest menuRequest) {
        try {
            Optional<Menu> menuRepo = menuRepository.findById(id);
            if(menuRepo.isEmpty()) {
                return null;
            }

            Menu existingMenu = menuRepo.get();
            String name = menuRequest.getName() != null ? menuRequest.getName() : existingMenu.getName();
            String description = menuRequest.getDescription() != null ? menuRequest.getDescription() : existingMenu.getDescription();
            BigDecimal price = menuRequest.getPrice() != null ? menuRequest.getPrice() : existingMenu.getPrice();
            List<Ingredient> requiredIngredient = menuRequest.getRequiredIngredient() != null ? menuRequest.getRequiredIngredient() : existingMenu.getRequiredIngredient();

            Menu menu = Menu.builder()
                    .id(menuRepo.get().getId())
                    .name(name)
                    .description(description)
                    .price(price)
                    .requiredIngredient(requiredIngredient)
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

    public MenuResponse addRequiredIngredient(String menuId, Ingredient ingredient) {
        try {
            Optional<Menu> menuRepo = menuRepository.findById(menuId);
            if(menuRepo.isEmpty()) {
                return null;
            }

            List<Ingredient> addedIngredient = new ArrayList<>();
            if (menuRepo.get().getRequiredIngredient() != null) {
                addedIngredient.addAll(menuRepo.get().getRequiredIngredient());
            }
            addedIngredient.add(ingredient);

            Menu menu = Menu.builder()
                    .id(menuRepo.get().getId())
                    .name(menuRepo.get().getName())
                    .description(menuRepo.get().getDescription())
                    .price(menuRepo.get().getPrice())
                    .requiredIngredient(addedIngredient)
                    .build();

            menu = menuRepository.save(menu);
            if(menu != null) {
                return MenuResponse.builder()
                        .code(200)
                        .message("Menu updated, ingredient added")
                        .data(menu)
                        .build();
            } else {
                return MenuResponse.builder()
                        .code(500)
                        .message("Failed to update menu, ingredient not added")
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
                    .message(e.getMessage())
                    .build();
        }
    }

    public List<Ingredient> getRequiredIngredients(String menuId) {
        // Retrieve the menu item from the repository
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("Menu item not found"));

        // Return the list of required ingredients for the menu item
        List<Ingredient> ingredients = menu.getRequiredIngredient();
        return ingredients;
    }
}

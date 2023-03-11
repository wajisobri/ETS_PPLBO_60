package com.programmingtechie.menuservice.controller;

import com.programmingtechie.menuservice.dto.MenuRequest;
import com.programmingtechie.menuservice.dto.MenuResponse;
import com.programmingtechie.menuservice.dto.MenusResponse;
import com.programmingtechie.menuservice.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping(value="/menus")
    @ResponseStatus(HttpStatus.OK)
    public MenusResponse getAllMenus() {
        return menuService.getAllMenus();
    }

    @GetMapping(value="/menu/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse getMenu(@PathVariable String id) {
        return menuService.getMenu(id);
    }

    @PostMapping(value="/menu")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuResponse createMenu(@RequestBody MenuRequest menuRequest) {
        return menuService.createMenu(menuRequest);
    }

    @PutMapping(value="/menu/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse updateMenu(@PathVariable String id, @RequestBody MenuRequest menuRequest) {
        return menuService.updateMenu(id, menuRequest);
    }

    @DeleteMapping(value="/menu/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse deleteMenu(@PathVariable String id) {
        return menuService.deleteMenu(id);
    }

}

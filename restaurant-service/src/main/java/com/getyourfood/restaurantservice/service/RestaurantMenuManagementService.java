package com.getyourfood.restaurantservice.service;

import com.getyourfood.restaurantservice.controller.dto.MenuItemDto;
import com.getyourfood.restaurantservice.controller.dto.MenuItemResponse;
import com.getyourfood.restaurantservice.controller.dto.RestaurantMenuResponse;
import com.getyourfood.restaurantservice.entity.MenuItem;
import com.getyourfood.restaurantservice.entity.Restaurant;
import com.getyourfood.restaurantservice.repository.MenuItemRepository;
import com.getyourfood.restaurantservice.repository.RestaurantRepository;
import com.getyourfood.restaurantservice.service.exception.RestaurantNotFoundException;
import com.getyourfood.restaurantservice.service.exception.UnexpectedServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RestaurantMenuManagementService {

  private final RestaurantRepository restaurantRepository;
  private final MenuItemRepository menuItemRepository;

  public RestaurantMenuManagementService(
      RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
    this.restaurantRepository = restaurantRepository;
    this.menuItemRepository = menuItemRepository;
  }

  public RestaurantMenuResponse addNewItems(String userId, List<MenuItemDto> menuItemDto) {
    try {
      Restaurant restaurant =
          getRestaurantById(userId)
              .orElseThrow(
                  () ->
                      new RestaurantNotFoundException("No Restaurant found for userId: " + userId));

      List<MenuItem> menuItems = new ArrayList<>();
      for (MenuItemDto dto : menuItemDto) {
        MenuItem item = mapToEntity(dto, restaurant);
        menuItems.add(item);
      }

      List<MenuItem> savedItems = menuItemRepository.saveAll(menuItems);
      List<MenuItemResponse> menuItemResponses =
          savedItems.stream().map(this::mapToMenuItemResponse).collect(Collectors.toList());

      return new RestaurantMenuResponse(restaurant.getName(), menuItemResponses);
    } catch (DataAccessException e) {
      log.error("Database error for user {}: {}", userId, e.getMessage(), e);
      throw new UnexpectedServiceException("Service temporarily unavailable", e);
    } catch (RestaurantNotFoundException e) {
      log.error("No Restaurant found for userId: {}", e.getMessage(), e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error for user {}: {}", userId, e.getMessage(), e);
      throw new UnexpectedServiceException("An unexpected error occurred");
    }
  }

  private MenuItemResponse mapToMenuItemResponse(MenuItem menuItem) {
    return new MenuItemResponse(
        menuItem.getId(),
        menuItem.getName(),
        menuItem.getPrice(),
        menuItem.getCategory().name(),
        menuItem.getStatus().name(),
        menuItem.getDescription(),
        menuItem.getImageUrl());
  }

  private MenuItem mapToEntity(MenuItemDto itemDto, Restaurant restaurant) {
    MenuItem menuItem = new MenuItem();
    menuItem.setName(itemDto.getName());
    menuItem.setPrice(itemDto.getPrice());
    MenuItem.Category category = MenuItem.Category.valueOf(itemDto.getCategory().toUpperCase());
    menuItem.setCategory(category);
    MenuItem.ItemStatus itemStatus = MenuItem.ItemStatus.valueOf(itemDto.getStatus().toUpperCase());
    menuItem.setStatus(itemStatus);
    menuItem.setDescription(itemDto.getDescription());
    menuItem.setImageUrl(itemDto.getImageUrl());
    menuItem.setRestaurant(restaurant);
    return menuItem;
  }

  private Optional<Restaurant> getRestaurantById(String userId) {
    return restaurantRepository.findById(Long.parseLong(userId));
  }
}

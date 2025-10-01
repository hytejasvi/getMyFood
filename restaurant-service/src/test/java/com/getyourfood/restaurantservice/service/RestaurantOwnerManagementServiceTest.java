package com.getyourfood.restaurantservice.service;

import static com.getyourfood.restaurantservice.fixtures.RestaurantOwnerBuilder.DEFAULT_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.getyourfood.restaurantservice.controller.dto.OnboardingRequestDto;
import com.getyourfood.restaurantservice.entity.Restaurant;
import com.getyourfood.restaurantservice.fixtures.RestaurantOwnerBuilder;
import com.getyourfood.restaurantservice.repository.RestaurantRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RestaurantOwnerManagementServiceTest {

  @Mock private RestaurantRepository restaurantRepository;

  @InjectMocks private RestaurantOwnerManagementService ownerManagementService;

  @Test
  void test_Successful_Restaurant_Owner_Onboarding() {
    OnboardingRequestDto requestDto = RestaurantOwnerBuilder.buildOnboardingRequestDto();
    Restaurant existingRestaurant = RestaurantOwnerBuilder.buildRestaurantEntity();
    Restaurant updatedRestaurant = RestaurantOwnerBuilder.buildOnboardedRestaurantEntity();

    when(restaurantRepository.findById(DEFAULT_USER_ID))
        .thenReturn(Optional.of(existingRestaurant));
    when(restaurantRepository.save(any())).thenReturn(updatedRestaurant);

    Restaurant result =
        ownerManagementService.completeRestaurantOnboarding(DEFAULT_USER_ID.toString(), requestDto);

    verify(restaurantRepository, times(1)).findById(DEFAULT_USER_ID);
    verify(restaurantRepository, times(1)).save(existingRestaurant);

    assertNotNull(result, "Result should not be null");
    assertEquals(updatedRestaurant, result, "Should return the saved restaurant");
    assertEquals(Restaurant.OnboardingStatus.ACTIVE, result.getStatus(), "Status should be ACTIVE");
    assertEquals(requestDto.restaurantName(), result.getName(), "Name should be updated");
    assertEquals(requestDto.address(), result.getAddress(), "Address should be updated");
    assertEquals(requestDto.zipcode(), result.getZipcode(), "Zipcode should be updated");
    assertEquals(
        requestDto.registrationNumber(),
        result.getRegistrationNumber(),
        "Registration number should be updated");

    /*verify(restaurantRepository).findById(DEFAULT_USER_ID);
    verify(restaurantRepository).save(existingRestaurant);*/
  }
}

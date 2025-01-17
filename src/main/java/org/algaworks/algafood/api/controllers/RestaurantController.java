package org.algaworks.algafood.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.algaworks.algafood.api.mapper.AddressMapper;
import org.algaworks.algafood.api.mapper.RestaurantMapper;
import org.algaworks.algafood.api.models.RestaurantModel;
import org.algaworks.algafood.api.models.RestaurantSimpleModel;
import org.algaworks.algafood.api.models.input.AddressInput;
import org.algaworks.algafood.api.models.input.RestaurantInput;
import org.algaworks.algafood.core.security.security_annotations.CheckSecurity;
import org.algaworks.algafood.domain.exceptions.BusinessException;
import org.algaworks.algafood.domain.exceptions.CityNotFoundException;
import org.algaworks.algafood.domain.exceptions.KitchenNotFoundException;
import org.algaworks.algafood.domain.exceptions.RestaurantNotFoundException;
import org.algaworks.algafood.domain.models.Restaurant;
import org.algaworks.algafood.domain.services.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final AddressMapper addressMapper;

    @CheckSecurity.Restaurant.Consult
    @GetMapping("/{restaurantCode}")
    public ResponseEntity<RestaurantModel> findByCode(@PathVariable String restaurantCode) {
        Restaurant restaurant = restaurantService.findByCode(restaurantCode);
        return ResponseEntity.ok(restaurantMapper.toRestaurantModel(restaurant));
    }

    @CheckSecurity.Restaurant.Consult
    @GetMapping(path = "/by-name", params = "name")
    public ResponseEntity<Page<RestaurantSimpleModel>> findByLikeName(@RequestParam String name,
                                                                      @PageableDefault Pageable pageable) {
        Page<Restaurant> restaurantModelPage = restaurantService.findByLikeName(name, pageable);
        return ResponseEntity.ok(restaurantModelPage.map(restaurantMapper::toRestaurantSimpleModel));
    }

    @CheckSecurity.Restaurant.Consult
    @GetMapping
    public ResponseEntity<Page<RestaurantSimpleModel>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(restaurantService.findAll(pageable).map(restaurantMapper::toRestaurantSimpleModel));
    }

    @CheckSecurity.Restaurant.Consult
    @GetMapping("/free-delivery")
    public ResponseEntity<Page<RestaurantSimpleModel>> restaurantsWithFreeDelivery(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(restaurantService.restaurantsWithFreeDelivery(pageable)
                .map(restaurantMapper::toRestaurantSimpleModel));
    }

    @CheckSecurity.Restaurant.Manage
    @PostMapping
    public ResponseEntity<RestaurantSimpleModel> add(@RequestBody @Valid RestaurantInput restaurantInput) {
        try {
            Restaurant restaurant = restaurantService.add(restaurantMapper.toRestaurant(restaurantInput));
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurantMapper.toRestaurantSimpleModel(restaurant));
        } catch (CityNotFoundException | KitchenNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @CheckSecurity.Restaurant.Manage
    @PutMapping("/{restaurantCode}")
    public ResponseEntity<RestaurantSimpleModel> update(@PathVariable String restaurantCode,
                                                        @Valid @RequestBody RestaurantInput restaurantInput) {
        try {
            Restaurant restaurant = restaurantService.findByCode(restaurantCode);
            restaurantMapper.copyToDomainObject(restaurantInput, restaurant);
            return ResponseEntity.ok(restaurantMapper.toRestaurantSimpleModel(restaurantService.add(restaurant)));
        } catch (CityNotFoundException | KitchenNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @CheckSecurity.Restaurant.Manage
    @PutMapping("/{restaurantCode}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable String restaurantCode) {
        restaurantService.activate(restaurantCode);
    }

    @CheckSecurity.Restaurant.Manage
    @DeleteMapping("/{restaurantCode}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivate(@PathVariable String restaurantCode) {
        restaurantService.inactivate(restaurantCode);
    }

    @CheckSecurity.Restaurant.Manage
    @PutMapping("/active-multiples")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateMultiples(@RequestBody List<String> restaurantCodes) {
        try {
            restaurantService.activateMultiples(restaurantCodes);
        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @CheckSecurity.Restaurant.Manage
    @DeleteMapping("/active-multiples")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivateMultiples(@RequestBody List<String> restaurantCodes) {
        try {
            restaurantService.inactivateMultiples(restaurantCodes);
        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @CheckSecurity.Restaurant.Manage
    @PutMapping("/{restaurantCode}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void opening(@PathVariable String restaurantCode) {
        restaurantService.opening(restaurantCode);
    }

    @CheckSecurity.Restaurant.Manage
    @DeleteMapping("/{restaurantCode}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closing(@PathVariable String restaurantCode) {
        restaurantService.closing(restaurantCode);
    }

    @CheckSecurity.Restaurant.Manage
    @PutMapping("/{restaurantCode}/update-address")
    public ResponseEntity<RestaurantModel> updateAddress(@PathVariable String restaurantCode,
                                                         @Valid @RequestBody AddressInput addressInput) {
        try {
            Restaurant restaurant = restaurantService.updateAddress(restaurantCode, addressMapper.toAddress(addressInput));
            return ResponseEntity.ok(restaurantMapper.toRestaurantModel(restaurant));
        } catch (CityNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @CheckSecurity.Restaurant.Manage
    @DeleteMapping("/{restaurantCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(@PathVariable String restaurantCode) {
        restaurantService.deleteByCode(restaurantCode);
    }
}

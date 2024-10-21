package org.algaworks.algafood.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.algaworks.algafood.api.mapper.KitchenMapper;
import org.algaworks.algafood.api.models.KitchenModel;
import org.algaworks.algafood.api.models.input.KitchenInput;
import org.algaworks.algafood.domain.models.Kitchen;
import org.algaworks.algafood.domain.services.KitchenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kitchens")
@AllArgsConstructor
public class KitchenController {

    private final KitchenService kitchenService;
    private final KitchenMapper kitchenMapper;

    @GetMapping("/{id}")
//    @PreAuthorize("isAuthenticated() and hasAuthority('CONSULT_KITCHEN' and hasAuthority('SCOPE_READ'))")
    public ResponseEntity<KitchenModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(kitchenMapper.toKitchenModel(kitchenService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<KitchenModel>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(kitchenService.findAll(pageable).map(kitchenMapper::toKitchenModel));
    }

    @PostMapping
    public ResponseEntity<KitchenModel> add(@RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen kitchen = kitchenService.add(kitchenMapper.toKitchen(kitchenInput));
        return ResponseEntity.status(HttpStatus.CREATED).body(kitchenMapper.toKitchenModel(kitchen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitchenModel> update(@PathVariable Long id, @RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen kitchen = kitchenService.findById(id);
        kitchenMapper.copyToDomainObject(kitchenInput, kitchen);
        return ResponseEntity.ok(kitchenMapper.toKitchenModel(kitchenService.add(kitchen)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        kitchenService.deleteById(id);
    }
}

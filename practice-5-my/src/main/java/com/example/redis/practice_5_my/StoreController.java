package com.example.redis.practice_5_my;

import com.example.redis.practice_5_my.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public StoreDto create(@RequestBody StoreDto storeDto) {
        return storeService.create(storeDto);
    }

    @GetMapping("/{id}")
    public StoreDto readOne(@PathVariable Long id) {

        return storeService.readOne(id);
    }

    @GetMapping
    public List<StoreDto> readAll() {
        return storeService.readAll();
    }


    @PutMapping("/{id}")
    public StoreDto update(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        return storeService.update(id, storeDto);
    }

    @DeleteMapping("{/id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storeService.delete(id);
    }
}

package org.csc.chessclub.utils;

import org.csc.chessclub.dto.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CustomUtils<T> implements PageUtils<T> {


    @Override
    public PageResponseDto<T> populatePageResponseDto(Page<T> pageResult) {
        return new PageResponseDto<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );
    }
}

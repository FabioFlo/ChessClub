package org.csc.chessclub.utils;

import org.csc.chessclub.dto.PageResponseDto;
import org.springframework.data.domain.Page;

public interface PageUtils<T> {

    PageResponseDto<T> populatePageResponseDto(Page<T> pageResult);
}

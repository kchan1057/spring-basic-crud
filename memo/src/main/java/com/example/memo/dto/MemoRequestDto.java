package com.example.memo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {
    //요청받을 데이터는 title과 contents이다. id는 기본키므로 서버에서 관리.
    private String title;
    private String contents;
}

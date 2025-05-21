package com.example.memo.dto;

import com.example.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {

    private Long id; //식별자는 null을 받을 수 있는 Wrapper 클래스로 이용
    private String title;
    private String contents;

    //생성자
    public MemoResponseDto(Memo memo){
        this.id = memo.getId();
        this.title = memo.getTitle();
        this.contents = memo.getContents();
    }
}

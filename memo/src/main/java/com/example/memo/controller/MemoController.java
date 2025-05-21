package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController //json 형태로 통신을 해야하므로
@RequestMapping("/memos")
public class MemoController {

        private final Map<Long, Memo> memoList = new HashMap<>();

        @PostMapping
        public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto){
            // 식별자가 1씩 증가하도록 만듦.
            Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;
            //요청받은 데이터로 Memo 객체 생성
            Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());
            // Inmemory DB에 Memo 메모
            memoList.put(memoId, memo);
            return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);
        }

        @GetMapping
        public List<MemoResponseDto> findAllMemos(){

            //List 선언 및 초기화
            List<MemoResponseDto> responseList = new ArrayList<>();

            //HashMap<Memo> -> List<MemoResponseDto>
            for (Memo memo : memoList.values()){
                MemoResponseDto responseDto = new MemoResponseDto(memo);
                responseList.add(responseDto);
            }
            return responseList; //List 안에 JSON이 나열된 형태로 출력된다.
        }

        @GetMapping("/{id}")
        public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id){
                Memo memo = memoList.get(id);
                if(memo == null){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); //NFE(NOT FOUND ERROR)를 방지하기 위해 Exception 처리를 해줘야 한다.
                }
                return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        }

        @PutMapping("/{id}")
        public ResponseEntity<MemoResponseDto> updateMemo(
                @PathVariable Long id,
                @RequestBody MemoRequestDto requestDto
        ){
            Memo memo = memoList.get(id);

            if (memo == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if(requestDto.getTitle() == null || requestDto.getContents() == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            memo.update(requestDto);
            return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        }

        @PatchMapping("/{id}")
        public ResponseEntity<MemoResponseDto> updateTitle(
                @PathVariable Long id,
                @RequestBody MemoRequestDto dto
        ){
            Memo memo = memoList.get(id);
            if(memo == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if(dto.getTitle() == null || dto.getContents() != null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            memo.updateTitle(dto);
            return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        }
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteMemo(@PathVariable Long id) {

            //memoList Key 값에 id를 포함하고 있다면
            if (memoList.containsKey(id)){
                memoList.remove(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}

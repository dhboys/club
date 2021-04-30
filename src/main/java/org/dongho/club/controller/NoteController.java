package org.dongho.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dongho.club.dto.NoteDTO;
import org.dongho.club.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO){
        log.info("----------------------register");
        Long num = noteService.register(noteDTO);
        // 201(action)
        return new ResponseEntity<>(num , HttpStatus.CREATED);
    }
    @GetMapping(value = "/all" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email){
        log.info("-------------getList........." + email);

        return new ResponseEntity<>(noteService.getAllWithWriter(email) , HttpStatus.OK);
    }
    // 리턴이 단순 문자열이므로 MediaType이 Text/Plain
    @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("num") Long num){

        log.info("-----------remove-------------------------------");
        log.info(num);

        noteService.remove(num);

        return new ResponseEntity<>("removed", HttpStatus.OK);

    }

    @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO){

        log.info("-----------modify-------------------------------");
        log.info(noteDTO);

        noteService.modify(noteDTO);

        return new ResponseEntity<>("modified", HttpStatus.OK);

    }
}

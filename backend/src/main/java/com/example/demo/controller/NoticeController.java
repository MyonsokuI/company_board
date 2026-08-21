package com.example.demo.controller;

import com.example.demo.dto.NoticeCreateRequestDto;
import com.example.demo.dto.NoticeListResponseDto;
import com.example.demo.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public ResponseEntity<NoticeListResponseDto> getNotices() {
        NoticeListResponseDto response = noticeService.getLatestNotices();
        return ResponseEntity.ok(response);
    }

    // 新規登録エンドポイント
    @PostMapping
    public ResponseEntity<String> createNotice(@RequestBody NoticeCreateRequestDto request) {
        noticeService.createNotice(request);
        return ResponseEntity.ok("Notice created successfully");
    }

    // 削除エンドポイント
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<String> deleteNotice(@PathVariable Integer noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok("Notice deleted successfully");
    }
}
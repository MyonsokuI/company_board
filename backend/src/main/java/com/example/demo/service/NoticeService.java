package com.example.demo.service;

import com.example.demo.dto.NoticeItemDto;
import com.example.demo.dto.NoticeListResponseDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Notice;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.dto.NoticeCreateRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public NoticeListResponseDto getLatestNotices() {
        LocalDate now = LocalDate.now();

        // 最新10件に制限するためのPageableを作成
        Pageable pageable = PageRequest.of(0, 10);

        // 期限内、または期限未設定の最新10件を取得
        List<Notice> notices = noticeRepository.findValidNotices(now, pageable);

        List<NoticeItemDto> noticeItemDtos = notices.stream().map(notice -> {
            String createUserName = null;
            if (notice.getCreatedBy() != null) {
                Employee employee = employeeRepository.findById(notice.getCreatedBy()).orElse(null);
                if (employee != null) {
                    createUserName = employee.getName();
                }
            }

            return new NoticeItemDto(
                    notice.getNoticeId(),
                    notice.getTitle(),
                    notice.getBody(),
                    notice.getPriority(),
                    notice.getPublishedUntil(),
                    notice.getCreatedAt() != null ? notice.getCreatedAt().toString() : null,
                    createUserName);
        }).collect(Collectors.toList());

        return new NoticeListResponseDto(noticeItemDtos);
    }

    public void createNotice(NoticeCreateRequestDto request) {
        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setBody(request.getBody());
        notice.setPriority(request.getPriority());
        notice.setCreatedBy(request.getCreatedBy());
        notice.setPublishedUntil(request.getPublishedUntil());

        // 作成日時と更新日時のセット
        LocalDateTime now = LocalDateTime.now();
        notice.setCreatedAt(now);
        notice.setUpdatedAt(now);

        noticeRepository.save(notice);
    }

    public void deleteNotice(Integer noticeId) {
        // 存在するか確認してから削除（必要に応じて例外処理）
        if (!noticeRepository.existsById(noticeId)) {
            throw new RuntimeException("Notice not found with id: " + noticeId);
        }
        noticeRepository.deleteById(noticeId);
    }
}
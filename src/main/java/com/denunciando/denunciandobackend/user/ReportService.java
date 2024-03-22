package com.denunciando.denunciandobackend.user;

import com.denunciando.denunciandobackend.report.Report;
import com.denunciando.denunciandobackend.report.ReportRepository;
import com.denunciando.denunciandobackend.report.ReportUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ReportService {


    @Autowired
    private ReportRepository reportRepository;

    public ImageUploadResponse uploadImage(MultipartFile file) throws IOException {

        reportRepository.save(Report.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ReportUtil.compressImage(file.getBytes())).build());

        return new ImageUploadResponse("Image uploaded successfully: " +
                file.getOriginalFilename());

    }

    @Transactional
    public Report getInfoByImageByName(String name) {
        Optional<Report> dbImage = reportRepository.findByName(name);

        return Report.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(ReportUtil.decompressImage(dbImage.get().getReport())).build();

    }

    @Transactional
    public byte[] getImage(String name) {
        Optional<Report> dbImage = reportRepository.findByName(name);
        byte[] image = ReportUtil.decompressImage(dbImage.get().getReport());
        return image;
    }
}

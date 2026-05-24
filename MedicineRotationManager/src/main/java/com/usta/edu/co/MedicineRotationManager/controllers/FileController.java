package com.usta.edu.co.MedicineRotationManager.controllers;

import java.io.IOException;

import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.EntryPracticeResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.FileUpdateDTO;

import com.usta.edu.co.MedicineRotationManager.services.FileService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(
            FileService fileService
    ) {

        this.fileService = fileService;
    }

    /*
     * UPLOAD FILE
     */
    @PostMapping(
            value = "/upload/{personId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "hasRole('DOCTOR') or " +
                    "hasRole('STUDENT')"
    )
    public ResponseEntity<EntryPracticeResponseDTO.FileResponseDTO> save(

            @RequestParam("file")
                    MultipartFile multipartFile,

            @PathVariable
                    String personId

    ) throws IOException {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        fileService.save(
                                multipartFile,
                                personId
                        )
                );
    }

    /*
     * FIND ALL
     */
    @GetMapping
    @PreAuthorize(
            "hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('STUDENT')"
    )
    public ResponseEntity<Page<EntryPracticeResponseDTO.FileResponseDTO>> findAll(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                fileService.findAll(pageable)
        );
    }

    /*
     * FIND BY ID
     */
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "hasRole('DOCTOR') or " +
                    "hasRole('STUDENT')"
    )
    public ResponseEntity<EntryPracticeResponseDTO.FileResponseDTO> findById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                fileService.findById(id)
        );
    }

    /*
     * UPDATE FILE METADATA
     */
    @PatchMapping("/{id}/metadata")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "hasRole('STUDENT')"
    )
    public ResponseEntity<EntryPracticeResponseDTO.FileResponseDTO> updateMetadata(

            @PathVariable
                    String id,

            @RequestBody
                    FileUpdateDTO dto

    ) {

        return ResponseEntity.ok(
                fileService.updateMetadata(
                        id,
                        dto
                )
        );
    }

    /*
     * REPLACE FILE
     */
    @PutMapping(
            value = "/{id}/replace",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "hasRole('STUDENT')"
    )
    public ResponseEntity<EntryPracticeResponseDTO.FileResponseDTO> replaceFile(

            @PathVariable
                    String id,

            @RequestParam("file")
                    MultipartFile multipartFile

    ) throws IOException {

        return ResponseEntity.ok(
                fileService.replaceFile(
                        id,
                        multipartFile
                )
        );
    }

    /*
     * DELETE FILE
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) throws IOException {

        fileService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
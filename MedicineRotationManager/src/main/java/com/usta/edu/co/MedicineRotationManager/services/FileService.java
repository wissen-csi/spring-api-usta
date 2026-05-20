package com.usta.edu.co.MedicineRotationManager.services;

import java.io.IOException;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.FileResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.FileUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.File;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.usta.edu.co.MedicineRotationManager.models.Person;
import com.usta.edu.co.MedicineRotationManager.repositories.FileRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final ServiceCloudinaryImpl cloudinaryService;

    private final ServicePerson personService;

    /**
     * Guarda archivo
     */
    @Transactional
    public FileResponseDTO save(
            MultipartFile multipartFile,
            String personId
    ) throws IOException {

        Person person =
                personService.findById(personId);

        var response =
                cloudinaryService.upload(multipartFile);

        File file =
                File.builder()

                        .id(UUIDGenerator.generateNewId())

                        .publicId(
                                response.get("id")
                                        .toString()
                        )

                        .secureUrl(
                                response.get("secure_url")
                                        .toString()
                        )

                        .originalName(
                                multipartFile.getOriginalFilename()
                        )

                        .format(
                                response.get("format")
                                        .toString()
                        )

                        .resourceType(
                                response.get("resource_type")
                                        .toString()
                        )

                        .size(
                                multipartFile.getSize()
                        )

                        .person(person)

                        .build();

        fileRepository.save(file);

        return mapToResponseDTO(file);
    }

    /**
     * Busca entidad internamente
     */
    @Transactional(readOnly = true)
    private File findEntityById(String id) {

        return fileRepository.findById(id)

                .orElseThrow(() ->

                        new EntityNotFoundException(
                                "File not found with id: " + id
                        )
                );
    }

    /**
     * Busca archivo para API
     */
    @Transactional(readOnly = true)
    public FileResponseDTO findById(String id) {

        File file = findEntityById(id);

        return mapToResponseDTO(file);
    }

    /**
     * Lista paginada
     */
    @Transactional(readOnly = true)
    public Page<FileResponseDTO> findAll(Pageable pageable) {

        return fileRepository.findAll(pageable)

                .map(this::mapToResponseDTO);
    }

    /**
     * Elimina archivo
     */
    @Transactional
    public void delete(String id)
            throws IOException {

        File file =
                findEntityById(id);

        cloudinaryService.delete(
                file.getPublicId()
        );

        fileRepository.delete(file);
    }

    /**
     * Reemplaza archivo físico
     */
    @Transactional
    public FileResponseDTO replaceFile(
            String id,
            MultipartFile newFile
    ) throws IOException {

        File existingFile =
                findEntityById(id);

        /*
         * Elimina archivo viejo
         */
        cloudinaryService.delete(
                existingFile.getPublicId()
        );

        /*
         * Sube nuevo archivo
         */
        var response =
                cloudinaryService.upload(newFile);

        /*
         * Actualiza metadata
         */
        existingFile.setPublicId(
                response.get("id")
                        .toString()
        );

        existingFile.setSecureUrl(
                response.get("secure_url")
                        .toString()
        );

        existingFile.setFormat(
                response.get("format")
                        .toString()
        );

        existingFile.setResourceType(
                response.get("resource_type")
                        .toString()
        );

        existingFile.setOriginalName(
                newFile.getOriginalFilename()
        );

        existingFile.setSize(
                newFile.getSize()
        );

        fileRepository.save(existingFile);

        return mapToResponseDTO(existingFile);
    }

    /**
     * Actualiza metadata
     */
    @Transactional
    public FileResponseDTO updateMetadata(
            String id,
            FileUpdateDTO dto
    ) {

        File file =
                findEntityById(id);

        file.setOriginalName(
                dto.originalName()
        );

        fileRepository.save(file);

        return mapToResponseDTO(file);
    }

    /**
     * Mapper Entity -> DTO
     */
    private FileResponseDTO mapToResponseDTO(File file) {

        return new FileResponseDTO(

                file.getId(),

                file.getSecureUrl(),

                file.getOriginalName()
        );
    }
}
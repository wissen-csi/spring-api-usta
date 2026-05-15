package com.usta.edu.co.MedicineRotationManager.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.File;

public interface FileRepository extends JpaRepository<File,String> {

}

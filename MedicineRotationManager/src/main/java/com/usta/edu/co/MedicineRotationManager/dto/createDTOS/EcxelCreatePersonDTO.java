package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;


import com.poiji.annotation.ExcelCellName;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class EcxelCreatePersonDTO {
    @ExcelCellName("name")
    protected String name;
    @ExcelCellName("last_name")
    protected String lastName;
    @ExcelCellName("dni")
    protected String dni;
    @ExcelCellName("marital_Status")
    protected MaritalStatus maritalStatus;
    @ExcelCellName("place_birth_address")
    protected String placeBirthAddress;
    @ExcelCellName("place_birth_city")
    protected String placeBirthCity;
    @ExcelCellName("place_birth_deparment")
    protected String placeBirthDepartment;
    @ExcelCellName("residence_address")
    protected String residenceAddress;
    @ExcelCellName("residence_city")
    protected String residenceCity;
    @ExcelCellName("residence_deparment")
    protected String residenceDepartment;
    @ExcelCellName("phone_number")
    protected String phoneNumber;
    @ExcelCellName("email")
    protected String email;
    @ExcelCellName("type_blood")
    protected TypeBlood typeBlood;
    @ExcelCellName("weight")
    protected double weight;
    @ExcelCellName("imc")
    protected double imc;
    @ExcelCellName("password")
    protected String password;
}

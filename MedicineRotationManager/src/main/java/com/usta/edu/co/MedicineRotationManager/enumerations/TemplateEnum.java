package com.usta.edu.co.MedicineRotationManager.enumerations;

import lombok.Getter;

@Getter
public enum TemplateEnum {
    ARL("arl"),
    PASSWORD("password")
    ;
    final String name;
    private TemplateEnum(String name){
        this.name=name;
    
    }
}

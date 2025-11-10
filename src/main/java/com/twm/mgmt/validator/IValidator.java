package com.twm.mgmt.validator;

import java.util.List;

/**
 * Validator interface untuk validasi input
 */
public interface IValidator {

    /**
     * Melakukan validasi dan mengembalikan list error messages
     * 
     * @return List<String> daftar error messages, kosong jika valid
     */
    List<String> validate();

    /**
     * Mendapatkan nama field yang divalidasi
     * 
     * @return String nama field
     */
    String getFieldName();
}
package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.DepartmentRequest;
import com.wtmsbackend.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse getDepartmentById(Integer id);
    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(Integer id, DepartmentRequest request);
    void deleteDepartment(Integer id);
}
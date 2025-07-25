package com.integrador.empleau.service;

import com.integrador.empleau.model.Empresa;

public interface EmpresaService {

    Empresa registrarEmpresa(Empresa empresa);
    Empresa obtenerPorEmail(String email);
    Empresa actualizarEmpresa(Empresa empresa);
}

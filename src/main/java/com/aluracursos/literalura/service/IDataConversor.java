package com.aluracursos.literalura.service;

public interface IDataConversor {
    <T> T obtainData(String json, Class<T> aClass);
}

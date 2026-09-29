package com.poo2ex6.model;

public class ImcRegistry {
    private Long id;
    private String name;
    private Float height;
    private Float weight;
    private Float imc;

    public ImcRegistry() {
    }

    public ImcRegistry(Long id, String name, Float height, Float weight, Float imc) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.imc = imc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getImc() {
        return imc;
    }

    public void setImc(Float imc) {
        this.imc = imc;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
}

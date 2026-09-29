package com.poo2ex6.service;

import com.poo2ex6.gateway.ImcRepositoryGateway;
import com.poo2ex6.model.ImcRegistry;

import java.util.List;

public class ImcService {
    private final ImcRepositoryGateway imcRepositoryGateway;
    private ImcRegistry lastCalculation = null;

    public ImcService(ImcRepositoryGateway imcRepositoryGateway) {
        this.imcRepositoryGateway = imcRepositoryGateway;
    }

    public Float calculateImc(String name, Float height, Float weight){
        float imc = (float)(weight / Math.pow(height, 2));

        lastCalculation = new ImcRegistry();
        lastCalculation.setName(name);
        lastCalculation.setHeight(height);
        lastCalculation.setWeight(weight);
        lastCalculation.setImc(imc);

        return imc;
    }

    public void saveLastCalculation(){
        if (lastCalculation == null){
            throw new RuntimeException("Nenhum cálculo para salvar");
        }
        imcRepositoryGateway.save(lastCalculation);
    }

    public List<ImcRegistry> getAllCalculations(){
        return imcRepositoryGateway.findAll();
    }
}

package com.poo2ex6.gateway;

import com.poo2ex6.model.ImcRegistry;

import java.util.List;

public interface ImcRepositoryGateway {
    List<ImcRegistry> findAll();

    ImcRegistry save(ImcRegistry imc);
}

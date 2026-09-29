package com.poo2ex6.persistence;

import com.poo2ex6.gateway.ImcRepositoryGateway;
import com.poo2ex6.model.ImcRegistry;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ImcFileRepository implements ImcRepositoryGateway {
    private final String IMC_FILE_NAME = "imc.csv";
    private final Path FILE_PATH = Path.of(IMC_FILE_NAME);

    private List<String> readFileLines(){
        if (Files.notExists(FILE_PATH)){
            createFile();
        }

        try {
            return Files.readAllLines(FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createFile() {
        try {
            Files.createFile(FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void writeOnFile(List<String> lines){
        if (Files.notExists(FILE_PATH))
            createFile();

        try {
            Files.write(FILE_PATH, lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ImcRegistry> findAll() {
        List<String> lines = readFileLines();

        return lines.stream()
                .map(ImcParser::toEntity)
                .toList();
    }

    @Override
    public ImcRegistry save(ImcRegistry imc) {
        List<String> lines = readFileLines();

        imc.setId(getNextId(lines));

        lines.add(ImcParser.toCsvLine(imc));

        writeOnFile(lines);

        return imc;
    }

    private long getNextId(List<String> lines) {
        if (lines.isEmpty())
            return 0;

        return ImcParser.toEntity(lines.getLast())
                .getId() + 1;
    }

    private static class ImcParser {
        static String toCsvLine(ImcRegistry imc){
            return imc.getId() + "," + imc.getName() + "," + imc.getHeight() + "," + imc.getWeight() + "," + imc.getImc();
        }

        static ImcRegistry toEntity(String csvLine){
            String[] attributes = csvLine.split(",");

            var imcRegistry = new ImcRegistry();

            try {
                imcRegistry.setId(Long.parseLong(attributes[0]));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid id attribute: " + attributes[0]);
            }

            imcRegistry.setName(attributes[1]);

            try {
                imcRegistry.setHeight(Float.parseFloat(attributes[2]));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid height attribute: " + attributes[2]);
            }

            try {
                imcRegistry.setWeight(Float.parseFloat(attributes[3]));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid weight attribute: " + attributes[3]);
            }

            try {
                imcRegistry.setImc(Float.parseFloat(attributes[4]));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid imc attribute: " + attributes[4]);
            }

            return imcRegistry;
        }
    }
}

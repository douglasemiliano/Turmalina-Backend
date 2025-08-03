package com.ifpb.turmalina.DTO;

public record ResgatarDesafioRequestDTO(String alunoId, String desafioId) {

    public ResgatarDesafioRequestDTO {
        if (alunoId == null || alunoId.isBlank()) {
            throw new IllegalArgumentException("O ID do aluno não pode ser nulo ou vazio.");
        }
        if (desafioId == null || desafioId.isBlank()) {
            throw new IllegalArgumentException("O ID do desafio não pode ser nulo ou vazio.");
        }
    }
}
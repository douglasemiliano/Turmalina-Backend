package com.ifpb.turmalina.service;

import com.ifpb.turmalina.Entity.Desafio;
import com.ifpb.turmalina.Repository.DesafioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesafioService {


    @Autowired
    private DesafioRepository desafioRepository;

    @Autowired
    private PerfilAlunoService perfilAlunoService;

    public Desafio criarDesafio(Desafio desafio){
        return desafioRepository.save(desafio);
    }

    public Desafio buscarDesafioPorId(String id) {
        return desafioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desafio não encontrado"));
    }

    public Desafio atualizarDesafio(String id, Desafio desafioAtualizado) {
        Desafio desafioExistente = buscarDesafioPorId(id);
        desafioExistente.setTitulo(desafioAtualizado.getTitulo());
        desafioExistente.setDescricao(desafioExistente.getDescricao());
        return desafioRepository.save(desafioExistente);
    }

    public List<Desafio> getDesafioByUserId(String idUser) {
        List<Desafio> desafios = desafioRepository.findByCreatedBy(idUser);
        if (desafios.isEmpty()) {
            throw new RuntimeException("Nenhum desafio encontrado para o usuário com ID: " + idUser);
        }
        return desafios;
    }

    public List<Desafio> getDesafioByCursoId(String idCurso) {
        List<Desafio> desafios = desafioRepository.findByCursoId(idCurso);
        if (desafios.isEmpty()) {
            return null;
        }
        return desafios;
    }

}

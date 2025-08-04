package com.ifpb.turmalina.controller;

import com.ifpb.turmalina.DTO.ResgatarDesafioRequestDTO;
import com.ifpb.turmalina.DTO.Response.Response;
import com.ifpb.turmalina.Entity.Desafio;
import com.ifpb.turmalina.Entity.DesafioAluno;
import com.ifpb.turmalina.Entity.Premio;
import com.ifpb.turmalina.service.DesafioAlunoService;
import com.ifpb.turmalina.service.DesafioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/turmalina/desafio")
public class DesafioController {

    @Autowired
    private DesafioService service;

    @Autowired
    private DesafioAlunoService desafioAlunoService;
    @Autowired
    private DesafioService desafioService;


    @PostMapping("")
    public ResponseEntity<Desafio> criarDesafio(@RequestBody Desafio desafio) throws GeneralSecurityException, IOException {
        System.err.println(desafio.getPremio());
        return ResponseEntity.ok(this.service.criarDesafio(desafio));
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<Desafio>> getDesafiosByUserId(@PathVariable String idUser) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.service.getDesafioByUserId(idUser));
    }

    @GetMapping("/{idCurso}")
    public ResponseEntity<List<Desafio>> getBadgeById(@PathVariable String idCurso) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.service.getDesafioByCursoId(idCurso));
    }

    @GetMapping("aluno/{idAluno}")
    public ResponseEntity<List<DesafioAluno>> getDesafiosAlunoByAlunoId(@PathVariable String idAluno){
        List<DesafioAluno> desafioAlunos = desafioAlunoService.buscarPorAlunoId(idAluno);
        if (desafioAlunos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(desafioAlunos);
    }

    @GetMapping("/{idCurso}/{idAluno}")
    public ResponseEntity<List<DesafioAluno>> getDesafiosAlunoByAlunoIdAndCursoId(@PathVariable String idCurso, @PathVariable String idAluno){
        List<DesafioAluno> desafioAlunos = desafioAlunoService.getDesafioalunoByCursoIdAndIdAluno(idCurso, idAluno);
        if (desafioAlunos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(desafioAlunos);
    }

    @PostMapping("/resgatar")
    public ResponseEntity<Response<List<Premio>>> resgatarPremio(@RequestBody ResgatarDesafioRequestDTO resgatarDesafioRequestDTO) {

        Desafio desafio = service.buscarDesafioPorId(resgatarDesafioRequestDTO.desafioId());
        if(desafio == null) {
            return ResponseEntity.notFound().build();
        }
        return desafioAlunoService.resgatarPremio(resgatarDesafioRequestDTO.alunoId(), desafio);
    }


}
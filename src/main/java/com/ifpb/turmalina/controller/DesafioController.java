package com.ifpb.turmalina.controller;

import com.ifpb.turmalina.Entity.Desafio;
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

    @PostMapping("")
    public ResponseEntity<Desafio> criarDesafio(@RequestBody Desafio desafio) throws GeneralSecurityException, IOException {
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

}
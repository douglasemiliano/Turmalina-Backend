package com.ifpb.turmalina.controller;

import com.ifpb.turmalina.Entity.Badge;
import com.ifpb.turmalina.Entity.PerfilAluno;
import com.ifpb.turmalina.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/turmalina/badge")
public class BadgeController {

    @Autowired
    private BadgeService service;

    @PostMapping("")
    public ResponseEntity<Badge> criarBadge(@RequestBody Badge badge) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.service.criarBadge(badge));
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<Badge>> getBadgesByUserId(@PathVariable String idUser) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.service.getBadgesByUserId(idUser));
    }

    @GetMapping("/{idBadge}")
    public ResponseEntity<Badge> getBadgeById(@PathVariable String idBadge) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(this.service.buscarBadgePorId(idBadge));
    }

    @PutMapping("/{idBadge}/user/{idUser}")
    public ResponseEntity<String> addBadgeToUser(@PathVariable String idBadge, @PathVariable String idUser) throws GeneralSecurityException, IOException {

        return ResponseEntity.ok(this.service.addBadgeToUser(idBadge, idUser));
    }

}
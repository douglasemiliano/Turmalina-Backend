package com.ifpb.turmalina.controller;


import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.Student;
import com.ifpb.turmalina.DTO.AlunoRankingDto;
import com.ifpb.turmalina.Entity.PerfilAluno;
import com.ifpb.turmalina.Entity.Ranking;
import com.ifpb.turmalina.service.AuthService;
import com.ifpb.turmalina.service.GamificationClass;
import com.ifpb.turmalina.service.GoogleClassroomService;
import com.ifpb.turmalina.service.PerfilAlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/turmalina")
@CrossOrigin(origins = "http://localhost:4200, https://turma-lina-app.vercel.app/, turma-lina-app.vercel.app ")
public class TurmalinaController {

    @Autowired
    private GoogleClassroomService googleClassroomService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GamificationClass gamificationClass;

    @Autowired
    private PerfilAlunoService perfilAlunoService;

    @GetMapping()
    public ResponseEntity<String> getTurmalina() {
        return ResponseEntity.ok("Turmalina is running!");
    }

    @GetMapping("/cursos")
    public ResponseEntity<?> listCourses(@RequestHeader String accessToken) throws GeneralSecurityException, IOException {
        System.err.println(accessToken);
        return ResponseEntity.ok(this.googleClassroomService.listCourses(accessToken));
    }

    /* lista os cursos do usuário que não são de sua propriedade */
    @GetMapping("/cursos/{userId}")
    public ResponseEntity<?> listarCursosFacoParte(@RequestHeader String accessToken, @PathVariable String userId) throws GeneralSecurityException, IOException {
        System.err.println(accessToken);
        return ResponseEntity.ok(this.googleClassroomService.listarCursosOwnerIdDiferentThanMe(userId, accessToken));
    }

    @GetMapping("/atividades/{courseId}")
    public ResponseEntity<List<CourseWork>> listarAtividades(@PathVariable String courseId, @RequestHeader String accessToken) throws GeneralSecurityException, IOException {
        System.err.println("coursework");
        List<CourseWork> courseWorks = googleClassroomService.listarAtividades(courseId, accessToken);
        return ResponseEntity.ok(courseWorks);
    }

    @GetMapping("/alunos/{courseId}")
    public ResponseEntity<List<Student>> listStudentSubmissions(@PathVariable String courseId, @RequestHeader String accessToken) throws GeneralSecurityException, IOException {
        System.err.println("entrou");
        List<Student> submissions = googleClassroomService.listStudents(courseId, accessToken);

        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/ranking/{courseId}")
    public ResponseEntity<List<AlunoRankingDto>> getRanking(@PathVariable String courseId, @RequestHeader String accessToken) throws GeneralSecurityException, IOException {
        List<Student> alunos = googleClassroomService.listStudents(courseId, accessToken);
        List<AlunoRankingDto> ranking = this.gamificationClass.atualizarRankingAlunos(courseId, accessToken);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/v2/ranking/{courseId}")
    public ResponseEntity<Ranking> getRankingv2(@PathVariable String courseId, @RequestHeader String accessToken) throws GeneralSecurityException, IOException {
        Ranking ranking = this.gamificationClass.obterRankingCurso(courseId, accessToken);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/perfil/{alunoId}")
    public ResponseEntity<PerfilAluno> getPerfilAluno(@PathVariable String alunoId) throws GeneralSecurityException, IOException {
        PerfilAluno perfilAluno = this.perfilAlunoService.getPerfilAluno(alunoId);
        return ResponseEntity.ok(perfilAluno);
    }

    @PostMapping("/perfil")
    public ResponseEntity<PerfilAluno> criarPerfilAluno(@RequestBody PerfilAluno perfilAluno) {
        PerfilAluno novoPerfil = this.perfilAlunoService.criarPerfilAluno(perfilAluno);
        return ResponseEntity.ok(novoPerfil);
    }

    @GetMapping("atividades/{courseId}/{userId}")
    public ResponseEntity<List<CourseWork>> listarMinhasAtividades(@PathVariable String courseId, @PathVariable String userId, @RequestHeader String accessToken) throws GeneralSecurityException, IOException {

        List<CourseWork> response = this.googleClassroomService.listarAtividadesComStatus(courseId, userId, accessToken);

        return ResponseEntity.ok(response);
    }
}

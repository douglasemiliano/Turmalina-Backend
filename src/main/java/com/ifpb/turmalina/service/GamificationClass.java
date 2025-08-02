package com.ifpb.turmalina.service;

import com.google.api.services.classroom.model.*;
import com.ifpb.turmalina.DTO.AlunoRankingDto;
import com.ifpb.turmalina.Entity.PerfilAluno;
import com.ifpb.turmalina.Entity.Ranking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class GamificationClass {

    @Autowired
    private GoogleClassroomService classroomService;

    @Autowired
    private PerfilAlunoService perfilAlunoService;

    @Autowired
    private RankingService rankingService;

    /**
     * Método para obter a pontuação de uma submissão de aluno.
     * Se a submissão estiver "TURNED_IN" ou "RETURNED", retorna a nota atribuída ou 10.0 se não houver nota.
     * Se a submissão não foi feita, retorna 0.0.
     *
     * @param submissao A submissão do aluno.
     * @return A pontuação obtida pela submissão.
     */
    private int obterPontuacao(StudentSubmission submissao) {
        String estado = submissao.getState();

        if ("TURNED_IN".equals(estado) || "RETURNED".equals(estado)) {
            return 1;
        }

        // Se não entregou, 0 ponto
        return 0;
    }

    public List<AlunoRankingDto> atualizarArrayAlunos(List<AlunoRankingDto> alunos, AlunoRankingDto aluno) {
        for (AlunoRankingDto a : alunos) {
            if (aluno.getAlunoId().equals(a.getAlunoId())) {
                a.setPontuacaoTotal(aluno.getPontuacaoTotal());
                return alunos;
            }
        }
        alunos.add(aluno);
        return alunos;
    }

    public void ajustarRanking(PerfilAluno perfilAluno, Course curso, int pontuacao){
        AlunoRankingDto alunoRankingDto = new AlunoRankingDto(perfilAluno.getAlunoId(), perfilAluno.getNome(), pontuacao, perfilAluno.getNivel(), perfilAluno.getFoto());


        Ranking ranking = rankingService.buscarRankingPorCursoId(curso.getId());
        List<AlunoRankingDto> alunoRankingList = atualizarArrayAlunos(ranking.getAlunos(), alunoRankingDto);

        ranking.setAlunos(alunoRankingList);
        alunoRankingList.sort(Comparator.comparingDouble(AlunoRankingDto::getPontuacaoTotal).reversed());
        rankingService.salvarRanking(ranking);
    }

    public PerfilAluno atualizarPontuacaoGlobal(PerfilAluno perfil, String accessToken) throws GeneralSecurityException, IOException {
        int pontuacaoGlobal = 0;
        List<Course> cursos = classroomService.listarCursosOwnerIdDiferentThanMe(perfil.getAlunoId(), accessToken);
        if (cursos == null || cursos.isEmpty()) {
            System.err.println("Nenhum curso encontrado para o aluno: " + perfil.getAlunoId());
            return null;
        }
        for (Course curso : cursos) {
            int pontuacaoPorCurso = 0;
            List<CourseWork> atividades = classroomService.listarAtividades(curso.getId(), accessToken);
            if (atividades == null || atividades.isEmpty()) {
                System.err.println("Nenhuma atividade encontrada no curso: " + curso.getName());
                continue;
            }
            for (CourseWork atividade : atividades) {
                List<StudentSubmission> submissao = classroomService.listStudentSubmissions(curso.getId(), atividade.getId(), accessToken);
                for (StudentSubmission sub : submissao) {
                    if (sub.getUserId().equals(perfil.getAlunoId())) {
                        System.err.println("Submissão do aluno: " + sub.getUserId() + " - Estado: " + sub.getState());
                        pontuacaoGlobal += obterPontuacao(sub);
                        pontuacaoPorCurso += obterPontuacao(sub);
                    }
                }
            }
            ajustarRanking(perfil, curso, pontuacaoPorCurso);
        }

        PerfilAluno response = perfilAlunoService.atualizarPontuacaoGlobal(perfil.getAlunoId(), pontuacaoGlobal);

        return response;
    }
//    public List<AlunoRankingDto> atualizarRankingAlunos(String courseId, String accessToken) throws IOException, GeneralSecurityException {
//        Map<String, Double> pontuacoesAlunos = new HashMap<>();
//
//        // Passo 1: Mapear alunos (id → nome)
//        List<Student> alunos = classroomService.listStudents(courseId, accessToken); // novo método necessário
//        Map<String, String> mapaIdParaNome = new HashMap<>();
//        if(alunos == null || alunos.isEmpty()) {
//            return null;
//        }
//        for (Student aluno : alunos) {
//            mapaIdParaNome.put(aluno.getUserId(), aluno.getProfile().getName().getFullName());
//        }
//
//        // Passo 2: Calcular pontuação por aluno
//        List<CourseWork> atividades = classroomService.listarAtividades(courseId, accessToken);
//        if (atividades == null || atividades.isEmpty()) {
//            // Nenhuma atividade encontrada, pode retornar lista vazia ou continuar conforme sua lógica
//            return Collections.emptyList();
//        }
//
//        for (CourseWork atividade : atividades) {
//            List<StudentSubmission> response = classroomService.listStudentSubmissions(courseId, atividade.getId(), accessToken);
//
//            for (StudentSubmission submissao : response) {
//                String alunoId = submissao.getUserId();
//                double pontuacao = obterPontuacao(submissao);
//                System.err.println("Submissão do aluno: " + alunoId + " - Pontuação: " + pontuacao);
//                pontuacoesAlunos.merge(alunoId, pontuacao, Double::sum);
//            }
//        }
//
//        for (Map.Entry<String, Double> entry : pontuacoesAlunos.entrySet()) {
//            String alunoId = entry.getKey();
//            double pontuacaoTotal = entry.getValue();
//            perfilAlunoService.atualizarPontuacaoGlobal(alunoId, pontuacaoTotal);
//        }
//
//        // Passo 3: Montar ranking
//        Ranking ranking = rankingService.buscarRankingPorCursoId(courseId);
//        List<AlunoRankingDto> alunoRankingList = new ArrayList<>();
//        for (Map.Entry<String, Integer> entry : pontuacoesAlunos.entrySet()) {
//            String alunoId = entry.getKey();
//            String nome = mapaIdParaNome.getOrDefault(alunoId, "Aluno desconhecido");
//            alunoRankingList.add(new AlunoRankingDto(alunoId, entry.getValue()));
//            if (ranking.getAlunos() == null || ranking.getAlunos().isEmpty()) {
//                ranking.setAlunos(new ArrayList<>());
//            }
//            atualizarArrayAlunos(ranking.getAlunos(), new AlunoRankingDto(alunoId, entry.getValue()));
//        }
//        rankingService.salvarRanking(ranking);
//        alunoRankingList.sort(Comparator.comparingDouble(AlunoRankingDto::getPontuacaoTotal).reversed());
//        return alunoRankingList;
//    }





    public Ranking obterRankingCurso(String courseId) {
        return rankingService.buscarRankingPorCursoId(courseId);
    }

//    public String atualizarRanking(String userId, String accessToken) throws GeneralSecurityException, IOException {
//        List<Course> cursos =  classroomService.listarCursosOwnerIdDiferentThanMe(userId, accessToken);
//        if(cursos == null || cursos.isEmpty()) {
//            return "Nenhum curso encontrado para o usuário: " + userId;
//        }
//        for(Course curso: cursos) {
//            atualizarRankingAlunos(curso.getId(), accessToken);
//        }
//        return "Ranking atualizado com sucesso para o usuário: " + userId + " nos cursos: " + cursos.size();
//    }


}

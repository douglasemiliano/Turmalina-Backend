package com.ifpb.turmalina.service;

import com.google.api.services.classroom.model.*;
import com.ifpb.turmalina.DTO.AlunoRankingDto;
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

    public int calcularPontuacao(List<CourseWork> atividadesConcluidas) {
        // Exemplo: 10 pontos por atividade concluída
        return atividadesConcluidas.size() * 10;

    }

    public String atribuirBadge(int pontuacao) {
        if (pontuacao >= 100) {
            return "Master Badge";
        } else if (pontuacao >= 50) {
            return "Intermediate Badge";
        } else {
            return "Beginner Badge";
        }
    }

    public String gerarRanking(List<Student> alunos, String courseId, String accessToken) throws GeneralSecurityException, IOException, GeneralSecurityException, IOException {
        StringBuilder ranking = new StringBuilder("Ranking:\n");
        for (Student aluno : alunos) {
            List<CourseWork> atividades = classroomService.listarAtividadesConcluidas(courseId, accessToken);
            int pontuacao = calcularPontuacao(atividades);
            ranking.append(aluno.getProfile().getName().getFullName())
                    .append(" - Pontos: ")
                    .append(pontuacao)
                    .append("\n");
        }
        return ranking.toString();
    }

//    private double obterPontuacao(StudentSubmission submissao) {
//        Double grade = submissao.getAssignedGrade() != null ? submissao.getAssignedGrade() : submissao.getDraftGrade();
//        return grade != null ? grade : 0.0;
//    }

    private double obterPontuacao(StudentSubmission submissao) {
        String estado = submissao.getState();

        if ("TURNED_IN".equals(estado) || "RETURNED".equals(estado)) {
            Double nota = submissao.getAssignedGrade();
            return nota != null ? nota : 10.0; // se não tiver nota, dá 1 ponto pela entrega
        }

        // Se não entregou, 0 ponto
        return 0.0;
    }


//    public List<AlunoRankingDto> obterRankingAlunos(String courseId, String accessToken) throws IOException, GeneralSecurityException {
//        Map<String, Double> pontuacoesAlunos = new HashMap<>();
//        List<CourseWork> atividades = classroomService.listarAtividades(courseId, accessToken);
//        for (CourseWork atividade : atividades) {
//            List<StudentSubmission> response = classroomService.listStudentSubmissions(courseId, atividade.getId(), accessToken);
//
//            for (StudentSubmission submissao : response) {
//                String alunoId = submissao.getId();
//                double pontuacao = obterPontuacao(submissao);
//                pontuacoesAlunos.merge(alunoId, pontuacao, Double::sum);
//            }
//        }
//        List<AlunoRankingDto> ranking = new ArrayList<>();
//        for (Map.Entry<String, Double> entry : pontuacoesAlunos.entrySet()) {
//            String alunoId = entry.getKey();
//            UserProfile perfilAluno = classroomService.getUserProfile(alunoId, accessToken);
//            ranking.add(new AlunoRankingDto(alunoId, perfilAluno.getName().getFullName(), entry.getValue(), 0));
//        }
//        ranking.sort(Comparator.comparingDouble(AlunoRankingDto::getPontuacaoTotal).reversed());
//        return ranking;
//
//    }

    public List<AlunoRankingDto> atualizarRankingAlunos(String courseId, String accessToken) throws IOException, GeneralSecurityException {
        Map<String, Double> pontuacoesAlunos = new HashMap<>();

        // Passo 1: Mapear alunos (id → nome)
        List<Student> alunos = classroomService.listStudents(courseId, accessToken); // novo método necessário
        Map<String, String> mapaIdParaNome = new HashMap<>();
        for (Student aluno : alunos) {
            mapaIdParaNome.put(aluno.getUserId(), aluno.getProfile().getName().getFullName());
        }

        // Passo 2: Calcular pontuação por aluno
        List<CourseWork> atividades = classroomService.listarAtividades(courseId, accessToken);
        for (CourseWork atividade : atividades) {
            List<StudentSubmission> response = classroomService.listStudentSubmissions(courseId, atividade.getId(), accessToken);

            for (StudentSubmission submissao : response) {
                String alunoId = submissao.getUserId(); // estava errado: era submissao.getId()
                double pontuacao = obterPontuacao(submissao);
                pontuacoesAlunos.merge(alunoId, pontuacao, Double::sum);
                perfilAlunoService.atualizarPerfilAluno(alunoId, mapaIdParaNome.getOrDefault(alunoId, "Aluno desconhecido"), pontuacao, Arrays.asList("Nota Máxima 🏆", "Pontual ⏱️", "Participativo 🤝", "Maratonista 🎓"));
            }
        }

        // Passo 3: Montar ranking
        Ranking ranking = rankingService.buscarRankingPorCursoId(courseId);
        List<AlunoRankingDto> alunoRankingList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : pontuacoesAlunos.entrySet()) {
            String alunoId = entry.getKey();
            String nome = mapaIdParaNome.getOrDefault(alunoId, "Aluno desconhecido");
            alunoRankingList.add(new AlunoRankingDto(alunoId, nome, entry.getValue(), 0));
            System.err.println("Aluno: " + nome + ", Pontuação: " + entry.getValue());
            System.err.println("ranking: " + ranking.toString());
            if (ranking.getAlunos() == null || ranking.getAlunos().isEmpty()) {
                ranking.setAlunos(new ArrayList<>());
            }
            atualizarArrayAlunos(ranking.getAlunos(), new AlunoRankingDto(alunoId, nome, entry.getValue(), 0));
        }
        rankingService.salvarRanking(ranking);
        alunoRankingList.sort(Comparator.comparingDouble(AlunoRankingDto::getPontuacaoTotal).reversed());
        return alunoRankingList;
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

    public Ranking obterRankingCurso(String courseId, String accessToken) {
        return rankingService.buscarRankingPorCursoId(courseId);
    }
}

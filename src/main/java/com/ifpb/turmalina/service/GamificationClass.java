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
    private double obterPontuacao(StudentSubmission submissao) {
        String estado = submissao.getState();

        if ("TURNED_IN".equals(estado) || "RETURNED".equals(estado)) {
            Double nota = submissao.getAssignedGrade();
            return nota != null ? nota : 10.0; // se não tiver nota, dá 1 ponto pela entrega
        }

        // Se não entregou, 0 ponto
        return 0.0;
    }


    /**
     * Atualiza o ranking dos alunos de um curso específico.
     * Passo 1: Mapeia os alunos (id → nome).
     * Passo 2: Calcula a pontuação total de cada aluno com base nas submissões.
     * Passo 3: Monta o ranking e atualiza o perfil do aluno.
     *
     * @param courseId      O ID do curso.
     * @param accessToken   O token de acesso para autenticação.
     * @return Lista de AlunoRankingDto com as pontuações atualizadas.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public List<AlunoRankingDto> atualizarRankingAlunos(String courseId, String accessToken) throws IOException, GeneralSecurityException {
        Map<String, Double> pontuacoesAlunos = new HashMap<>();

        // Passo 1: Mapear alunos (id → nome)
        List<Student> alunos = classroomService.listStudents(courseId, accessToken); // novo método necessário
        Map<String, String> mapaIdParaNome = new HashMap<>();
        if(alunos == null || alunos.isEmpty()) {
            return null;
        }
        for (Student aluno : alunos) {
            mapaIdParaNome.put(aluno.getUserId(), aluno.getProfile().getName().getFullName());
        }

        // Passo 2: Calcular pontuação por aluno
        List<CourseWork> atividades = classroomService.listarAtividades(courseId, accessToken);
        for (CourseWork atividade : atividades) {
            List<StudentSubmission> response = classroomService.listStudentSubmissions(courseId, atividade.getId(), accessToken);

            for (StudentSubmission submissao : response) {
                String alunoId = submissao.getUserId();
                double pontuacao = obterPontuacao(submissao);
                pontuacoesAlunos.merge(alunoId, pontuacao, Double::sum);
            }
        }

        for (Map.Entry<String, Double> entry : pontuacoesAlunos.entrySet()) {
            String alunoId = entry.getKey();
            double pontuacaoTotal = entry.getValue();
            perfilAlunoService.atualizarPontuacaoGlobal(alunoId, pontuacaoTotal);
        }

        // Passo 3: Montar ranking
        Ranking ranking = rankingService.buscarRankingPorCursoId(courseId);
        List<AlunoRankingDto> alunoRankingList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : pontuacoesAlunos.entrySet()) {
            String alunoId = entry.getKey();
            String nome = mapaIdParaNome.getOrDefault(alunoId, "Aluno desconhecido");
            alunoRankingList.add(new AlunoRankingDto(alunoId, nome, entry.getValue(), 0));
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

    public String atualizarRanking(String userId, String accessToken) throws GeneralSecurityException, IOException {
        List<Course> cursos =  classroomService.listarCursosOwnerIdDiferentThanMe(userId, accessToken);
        if(cursos == null || cursos.isEmpty()) {
            return "Nenhum curso encontrado para o usuário: " + userId;
        }
        for(Course curso: cursos) {
            atualizarRankingAlunos(curso.getId(), accessToken);
        }
        return "Ranking atualizado com sucesso para o usuário: " + userId + " nos cursos: " + cursos.size();
    }


}

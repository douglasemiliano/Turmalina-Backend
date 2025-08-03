package com.ifpb.turmalina.service;

import com.google.api.services.classroom.model.*;
import com.ifpb.turmalina.DTO.AlunoRankingDto;
import com.ifpb.turmalina.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GamificationClass {

    @Autowired
    private GoogleClassroomService classroomService;

    @Autowired
    private PerfilAlunoService perfilAlunoService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private DesafioService desafioService;

    @Autowired DesafioAlunoService desafioAlunoService;

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
        int quantidadeAtividadesConcluidas = 0;
        List<Course> cursos = classroomService.listarCursosOwnerIdDiferentThanMe(perfil.getAlunoId(), accessToken);
        if (cursos == null || cursos.isEmpty()) {
            System.err.println("Nenhum curso encontrado para o aluno: " + perfil.getAlunoId());
            return null;
        }
        for (Course curso : cursos) {
            int pontuacaoPorCurso = 0;

            List<CourseWork> atividades = classroomService.listarAtividades(curso.getId(), accessToken);
            List<StudentSubmission> submissaoPorAtividade = new ArrayList<>();

            if (atividades != null && !atividades.isEmpty()){
                for (CourseWork atividade : atividades) {
                    List<StudentSubmission> submissao = classroomService.listStudentSubmissions(curso.getId(), atividade.getId(), accessToken);
                    for (StudentSubmission sub : submissao) {
                        if (sub.getUserId().equals(perfil.getAlunoId())) {
                            System.err.println("Submissão do aluno: " + sub.getUserId() + " - Estado: " + sub.getState());
                            quantidadeAtividadesConcluidas += obterPontuacao(sub);
                            pontuacaoPorCurso += obterPontuacao(sub);
                            submissaoPorAtividade.add(sub);
                        }
                    }
                }
            }

            List<Desafio> desafiosCurso = desafioService.getDesafioByCursoId(curso.getId());

            if (desafiosCurso != null) {
                for (Desafio desafio : desafiosCurso) {
                    DesafioAluno desafioAluno = desafioAlunoService.buscarPorAlunoEdesafio(perfil.getAlunoId(), desafio.getId());
                    if (desafioAluno == null) {
                        // Vínculo inicial do desafio ao aluno
                        desafioAluno = new DesafioAluno();
                        desafioAluno.setAlunoId(perfil.getAlunoId());
                        desafioAluno.setDesafioId(desafio.getId());
                        desafioAluno.setStatus(DesafioStatus.INCOMPLETO);
                        desafioAluno.setCursoId(curso.getId());
                        desafioAlunoService.salvar(desafioAluno);
                    }

                    // Verificar se já está completo para evitar reprocessar

                    System.err.println(desafioAluno.getStatus());
                    if (!(desafioAluno.getStatus().equals(DesafioStatus.COMPLETO)) && !(desafioAluno.getStatus().equals(DesafioStatus.REIVINDICADO))) {
                        boolean cumpriu = avaliarWinCondition(desafio, atividades, submissaoPorAtividade);
                        if (cumpriu) {
                            desafioAluno.setStatus(DesafioStatus.COMPLETO);
                            desafioAluno.setDataConclusao(LocalDateTime.now());
                            desafioAlunoService.salvar(desafioAluno);
                        }
                    }
                }
            }

            ajustarRanking(perfil, curso, pontuacaoPorCurso);
        }

        PerfilAluno response = perfilAlunoService.atualizarPontuacaoGlobal(perfil.getAlunoId(), quantidadeAtividadesConcluidas);

        return response;
    }

    public Ranking obterRankingCurso(String courseId) {
        return rankingService.buscarRankingPorCursoId(courseId);
    }

    private boolean avaliarWinCondition(Desafio desafio, List<CourseWork> atividades, List<StudentSubmission> submissions) {
        if (desafio.getWinCondition() == null || desafio.getWinCondition().isEmpty()) return false;

        for (WinCondition condicao : desafio.getWinCondition()) {
            switch (condicao.getTipo()) {
                case ENTREGA_ATIVIDADES:
                    long entregues = submissions.stream()
                            .filter(s -> "TURNED_IN".equals(s.getState()) || "RETURNED".equals(s.getState()))
                            .count();
                    if (entregues < condicao.getQuantidade()) return false;
                    break;

                case ENTREGA_ATIVIDADE_ID:
                    boolean atividadeEntregue = atividades.stream()
                            .anyMatch(a -> a.getId().equals(condicao.getItemId()) &&
                                    submissions.stream().anyMatch(s -> s.getCourseWorkId().equals(a.getId()) &&
                                            ("TURNED_IN".equals(s.getState()) || "RETURNED".equals(s.getState()))));
                    if (!atividadeEntregue) return false;
                    break;

                case NOTA_MINIMA:
                    boolean algumaNotaAlcancada = submissions.stream()
                            .filter(s -> s.getAssignedGrade() != null)
                            .anyMatch(s -> s.getAssignedGrade() >= condicao.getQuantidade());
                    if (!algumaNotaAlcancada) return false;
                    break;

                case MEDIA_MINIMA:
                    List<Double> notas = submissions.stream()
                            .map(StudentSubmission::getAssignedGrade)
                            .filter(Objects::nonNull)
                            .toList();
                    if (notas.isEmpty()) return false;

                    double media = notas.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    if (media < condicao.getQuantidade()) return false;
                    break;

                case ENTREGOU_ANTES_DO_PRAZO_X_VEZES:
                    long pontuais = submissions.stream()
                            .filter(s -> s.getAssignmentSubmission() != null &&
                                    s.getLate() != null && !s.getLate())
                            .count();
                    if (pontuais < condicao.getQuantidade()) return false;
                    break;

                case ENTREGOU_COM_NOTA_MAIOR_QUE_X:
                    long boasNotas = submissions.stream()
                            .filter(s -> s.getAssignedGrade() != null && s.getAssignedGrade() > condicao.getQuantidade())
                            .count();
                    if (boasNotas == 0) return false;
                    break;
                case PRIMEIRA_ATIVIDADE:
                    System.err.println("Verificando se a primeira atividade foi entregue...");
                    // Verifica se a primeira atividade foi entregue
                    if (atividades.isEmpty()) return false;
                    CourseWork primeiraAtividade = atividades.get(0);
                    boolean primeiraEntregue = submissions.stream()
                            .anyMatch(s -> s.getCourseWorkId().equals(primeiraAtividade.getId()) &&
                                    ("TURNED_IN".equals(s.getState()) || "RETURNED".equals(s.getState())));
                    System.err.println("Primeira atividade entregue: " + primeiraEntregue);
                    if (!primeiraEntregue) return false;
                    break;
                default:
                    System.err.println("Tipo de condição não reconhecido: " + condicao.getTipo());
                    return false;
            }
        }

        // Se passou por todas as condições sem retornar false, venceu!
        return true;
    }


}
